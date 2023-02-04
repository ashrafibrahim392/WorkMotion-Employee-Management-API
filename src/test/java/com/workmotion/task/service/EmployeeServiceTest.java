package com.workmotion.task.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.workmotion.task.dto.EmployeeResponseDTO;
import com.workmotion.task.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import com.workmotion.task.dto.EmployeeRequestDTO;
import com.workmotion.task.exception.FunctionalException;
import com.workmotion.task.model.Employee;
import com.workmotion.task.model.EmployeeState;
import com.workmotion.task.model.EmployeeStateEvent;
import com.workmotion.task.repository.EmployeeRepository;

import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;
    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    void whenAddingEmployee() {
        EmployeeRequestDTO e1 = EmployeeRequestDTO.builder()
                .firstName("firstname")
                .lastName("lastname")
                .salary(2001.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .build();
        Employee employee = Employee.builder()
                .firstName("firstname")
                .lastName("lastname")
                .salary(2001.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .state(EmployeeState.ADDED)
                .build();
        Mockito.when(employeeRepository.save(EmployeeMapper.INSTANCE.toEmployee(e1))).thenReturn(employee);
        employeeService.saveEmployee(e1);
        assertEquals(EmployeeState.ADDED, employee.getState());

    }


    @Test
    void whenChangeStateSuccessAndSecurityFirst() throws FunctionalException {
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("firstname")
                .lastName("lastname")
                .salary(2001.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .state(EmployeeState.ADDED)
                .build();
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.BEGIN_CHECK);
        assertEquals(EmployeeState.IN_CHECK, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_STARTED, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_STARTED, employee.getSecurityState());

        employee.setState(EmployeeState.IN_CHECK);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_STARTED);
        employee.setWorkPermitState(EmployeeState.WORK_PERMIT_CHECK_STARTED);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.FINISH_SECURITY_CHECK);
        assertEquals(EmployeeState.IN_CHECK, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_STARTED, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_FINISHED, employee.getSecurityState());

        employee.setState(EmployeeState.IN_CHECK);
        employee.setWorkPermitState(EmployeeState.WORK_PERMIT_CHECK_STARTED);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_FINISHED);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.COMPLETE_INITIAL_WORK_PERMIT_CHECK);
        assertEquals(EmployeeState.IN_CHECK, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_FINISHED, employee.getSecurityState());

        employee.setState(EmployeeState.IN_CHECK);
        employee.setWorkPermitState(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_FINISHED);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.FINISH_WORK_PERMIT_CHECK);
        assertEquals(EmployeeState.APPROVED, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_FINISHED, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_FINISHED, employee.getSecurityState());

        employee.setState(EmployeeState.APPROVED);
        employee.setWorkPermitState(EmployeeState.WORK_PERMIT_CHECK_FINISHED);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_FINISHED);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.ACTIVATE);
        assertEquals(EmployeeState.ACTIVE, employee.getState());
    }

    @Test
    void whenChangeStateSuccessAndWorkFirst() throws FunctionalException {
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("firstname")
                .lastName("lastname")
                .salary(2001.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .state(EmployeeState.ADDED)
                .build();
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.BEGIN_CHECK);
        assertEquals(EmployeeState.IN_CHECK, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_STARTED, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_STARTED, employee.getSecurityState());

        employee.setState(EmployeeState.IN_CHECK);
        employee.setSecurityState(EmployeeState.WORK_PERMIT_CHECK_STARTED);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_STARTED);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.COMPLETE_INITIAL_WORK_PERMIT_CHECK);
        assertEquals(EmployeeState.IN_CHECK, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_STARTED, employee.getSecurityState());

        employee.setState(EmployeeState.IN_CHECK);
        employee.setSecurityState(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_STARTED);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.FINISH_SECURITY_CHECK);
        assertEquals(EmployeeState.IN_CHECK, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_FINISHED, employee.getSecurityState());

        employee.setState(EmployeeState.IN_CHECK);
        employee.setSecurityState(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_FINISHED);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.FINISH_WORK_PERMIT_CHECK);
        assertEquals(EmployeeState.APPROVED, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_FINISHED, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_FINISHED, employee.getSecurityState());

        employee.setState(EmployeeState.APPROVED);
        employee.setSecurityState(EmployeeState.WORK_PERMIT_CHECK_FINISHED);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_FINISHED);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.ACTIVATE);
        employee = employeeRepository.findById(1L).get();
        assertEquals(EmployeeState.ACTIVE, employee.getState());
    }


    @Test
    void whenChangeStateSuccessAndFinsihWorkFirst() throws FunctionalException {
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("firstname")
                .lastName("lastname")
                .salary(2001.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .state(EmployeeState.ADDED)
                .build();
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.BEGIN_CHECK);
        assertEquals(EmployeeState.IN_CHECK, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_STARTED, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_STARTED, employee.getSecurityState());

        employee.setState(EmployeeState.IN_CHECK);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_STARTED);
        employee.setWorkPermitState(EmployeeState.WORK_PERMIT_CHECK_STARTED);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.COMPLETE_INITIAL_WORK_PERMIT_CHECK);
        assertEquals(EmployeeState.IN_CHECK, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_STARTED, employee.getSecurityState());


        employee.setState(EmployeeState.IN_CHECK);
        employee.setWorkPermitState(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_STARTED);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.FINISH_WORK_PERMIT_CHECK);
        assertEquals(EmployeeState.IN_CHECK, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_FINISHED, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_STARTED, employee.getSecurityState());

        employee.setState(EmployeeState.IN_CHECK);
        employee.setWorkPermitState(EmployeeState.WORK_PERMIT_CHECK_FINISHED);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_STARTED);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.FINISH_SECURITY_CHECK);
        assertEquals(EmployeeState.APPROVED, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_FINISHED, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_FINISHED, employee.getSecurityState());

        employee.setState(EmployeeState.APPROVED);
        employee.setWorkPermitState(EmployeeState.WORK_PERMIT_CHECK_FINISHED);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_FINISHED);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.ACTIVATE);
        assertEquals(EmployeeState.ACTIVE, employee.getState());
    }

    @Test
    void whenChangeStatusFailedToActivate() throws FunctionalException {
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("firstname")
                .lastName("lastname")
                .salary(2001.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .state(EmployeeState.ADDED)
                .build();
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.BEGIN_CHECK);
        assertEquals(EmployeeState.IN_CHECK, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_STARTED, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_STARTED, employee.getSecurityState());

        employee.setState(EmployeeState.IN_CHECK);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_STARTED);
        employee.setWorkPermitState(EmployeeState.WORK_PERMIT_CHECK_STARTED);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.FINISH_SECURITY_CHECK);
        assertEquals(EmployeeState.IN_CHECK, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_STARTED, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_FINISHED, employee.getSecurityState());

        employee.setState(EmployeeState.IN_CHECK);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_FINISHED);
        employee.setWorkPermitState(EmployeeState.WORK_PERMIT_CHECK_STARTED);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        assertThrows(FunctionalException.class, () -> employeeService.changeState(4L, EmployeeStateEvent.ACTIVATE));

    }


    @Test
    void whenChangeStatusFailedToFinishWorkPermit() throws FunctionalException {
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("firstname")
                .lastName("lastname")
                .salary(2001.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .state(EmployeeState.ADDED)
                .build();
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.changeState(1L, EmployeeStateEvent.BEGIN_CHECK);
        assertEquals(EmployeeState.IN_CHECK, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_STARTED, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_STARTED, employee.getSecurityState());

        employee.setState(EmployeeState.IN_CHECK);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_STARTED);
        employee.setWorkPermitState(EmployeeState.WORK_PERMIT_CHECK_STARTED);
        employeeService.changeState(1L, EmployeeStateEvent.FINISH_SECURITY_CHECK);
        assertEquals(EmployeeState.IN_CHECK, employee.getState());
        assertEquals(EmployeeState.WORK_PERMIT_CHECK_STARTED, employee.getWorkPermitState());
        assertEquals(EmployeeState.SECURITY_CHECK_FINISHED, employee.getSecurityState());

        employee.setState(EmployeeState.IN_CHECK);
        employee.setSecurityState(EmployeeState.SECURITY_CHECK_FINISHED);
        employee.setWorkPermitState(EmployeeState.WORK_PERMIT_CHECK_STARTED);
        assertThrows(FunctionalException.class, () -> employeeService.changeState(5L, EmployeeStateEvent.FINISH_WORK_PERMIT_CHECK));

    }

    @Test
    void whenListEmployeePage() {
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("firstname")
                .lastName("lastname")
                .salary(2001.5)
                .age(20)
                .phoneNumber("01008888882")
                .email("test@test.com")
                .state(EmployeeState.ADDED)
                .build();
        Page<Employee> page = new PageImpl<>(Arrays.asList(employee));
        Mockito.when(employeeRepository.findAll(PageRequest.of(0, 3, Sort.by("id").descending()))).thenReturn(page);
        EmployeeResponseDTO employeeResponseDTO = employeeService.listEmployeePage(3, 0);
        assertEquals(employeeResponseDTO.getEmployees().size(), 1);

    }
}
