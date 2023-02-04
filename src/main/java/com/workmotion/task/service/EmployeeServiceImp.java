package com.workmotion.task.service;

import com.workmotion.task.constant.Constant;
import com.workmotion.task.dto.EmployeeDTO;
import com.workmotion.task.dto.EmployeeRequestDTO;
import com.workmotion.task.dto.EmployeeResponseDTO;
import com.workmotion.task.exception.FunctionalException;
import com.workmotion.task.interceptor.EmployeeStateChangeInterceptor;
import com.workmotion.task.mapper.EmployeeMapper;
import com.workmotion.task.model.Employee;
import com.workmotion.task.model.EmployeeState;
import com.workmotion.task.model.EmployeeStateEvent;
import com.workmotion.task.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImp implements EmployeeService {


    private final StateMachineFactory<EmployeeState, EmployeeStateEvent> stateMachineFactory;

    private final EmployeeStateChangeInterceptor employeeStateChangeInterceptor;

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public void saveEmployee(EmployeeRequestDTO employeeRequestDTO) {
        // TODO any required validation
        log.info("Start save Employee");
        Employee employee = EmployeeMapper.INSTANCE.toEmployee(employeeRequestDTO);
        employeeRepository.save(employee);
    }

    @Override
    public EmployeeResponseDTO listEmployeePage(Integer limit, Integer offset) {
        log.info("Start list Employee page");
        Page<Employee> employeesPage = employeeRepository.findAll(PageRequest.of(offset, limit, Sort.by("id").descending()));
        List<EmployeeDTO> employeesList = employeesPage.map(employeeMapper::toEmployeeDTo).stream().collect(Collectors.toList());
        return EmployeeResponseDTO.builder().employees(employeesList).totalCount(employeesPage.getTotalElements()).build();
    }

    @Override
    @Transactional
    public void changeState(Long employeeId, EmployeeStateEvent event) throws FunctionalException {
        log.info("Start change employee state");
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new FunctionalException(HttpStatus.NOT_FOUND,
                        String.format("No Employee with id %d", employeeId)));
        if (!sendEvent(employee, build(employee, event), event))
            throw new FunctionalException(HttpStatus.FORBIDDEN, "Transaction not accepted");
    }

    private Boolean sendEvent(Employee employee, StateMachine<EmployeeState, EmployeeStateEvent> sm,
                              EmployeeStateEvent event) {
        return sm.sendEvent(MessageBuilder.withPayload(event).setHeader(Constant.EMPLOYEE, employee).build());
    }

    private StateMachine<EmployeeState, EmployeeStateEvent> build(Employee employee, EmployeeStateEvent event) {
        EmployeeState currentState = getSourceState(employee, event);
        return generateStateMachine(employee, currentState);
    }

    private StateMachine<EmployeeState, EmployeeStateEvent> generateStateMachine(Employee employee,
                                                                                 EmployeeState currentState) {
        StateMachine<EmployeeState, EmployeeStateEvent> sm = stateMachineFactory
                .getStateMachine(Long.toString(employee.getId()));
        sm.stop();
        sm.getStateMachineAccessor().doWithAllRegions(sma -> {
            sma.addStateMachineInterceptor(employeeStateChangeInterceptor);
            sma.resetStateMachine(new DefaultStateMachineContext<>(currentState, null, null, null));
        });
        sm.start();
        return sm;
    }

    private EmployeeState getSourceState(Employee employee, EmployeeStateEvent event) {
        if (EmployeeStateEvent.FINISH_SECURITY_CHECK.equals(event) && employee.getSecurityState() != null)
            return employee.getSecurityState();
        if (EmployeeStateEvent.FINISH_WORK_PERMIT_CHECK.equals(event) && employee.getWorkPermitState() != null)
            return employee.getWorkPermitState();

        return employee.getState();
    }

}
