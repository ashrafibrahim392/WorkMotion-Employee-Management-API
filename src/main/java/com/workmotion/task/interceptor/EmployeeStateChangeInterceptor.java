package com.workmotion.task.interceptor;

import java.util.Optional;

import com.workmotion.task.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import com.workmotion.task.model.Employee;
import com.workmotion.task.model.EmployeeState;
import com.workmotion.task.model.EmployeeStateEvent;
import com.workmotion.task.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class EmployeeStateChangeInterceptor extends StateMachineInterceptorAdapter<EmployeeState, EmployeeStateEvent> {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void preStateChange(State<EmployeeState, EmployeeStateEvent> state, Message<EmployeeStateEvent> message,
                               Transition<EmployeeState, EmployeeStateEvent> transition,
                               StateMachine<EmployeeState, EmployeeStateEvent> stateMachine) {
        Optional.ofNullable(message).ifPresent(msg -> {
            Optional.ofNullable(
                            Employee.class.cast(msg.getHeaders().getOrDefault(Constant.EMPLOYEE, null)))
                    .ifPresent(employee -> {
                        updateEmployeeState(state, employee);
                        employeeRepository.save(employee);
                    });
        });
    }

    private void updateEmployeeState(State<EmployeeState, EmployeeStateEvent> state, Employee employee) {
        if (EmployeeState.SECURITY_CHECK_STARTED.equals(state.getId()) || EmployeeState.SECURITY_CHECK_FINISHED.equals(state.getId())) {
            EmployeeState newState = isReadyForApprove(employee, state.getId()) ? EmployeeState.APPROVED : EmployeeState.IN_CHECK;
            employee.setSecurityState(state.getId());
            employee.setState(newState);
        } else if (EmployeeState.WORK_PERMIT_CHECK_STARTED.equals(state.getId()) ||
                EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION.equals(state.getId()) ||
                EmployeeState.WORK_PERMIT_CHECK_FINISHED.equals(state.getId())
        ) {
            EmployeeState newState = isReadyForApprove(employee, state.getId()) ? EmployeeState.APPROVED : EmployeeState.IN_CHECK;
            employee.setWorkPermitState(state.getId());
            employee.setState(newState);
        } else {
            employee.setState(state.getId());
        }
    }

    private boolean isReadyForApprove(Employee employee, EmployeeState state) {
        return (EmployeeState.SECURITY_CHECK_FINISHED.equals(state) &&
                EmployeeState.WORK_PERMIT_CHECK_FINISHED.equals(employee.getWorkPermitState()) ||
                EmployeeState.SECURITY_CHECK_FINISHED.equals(employee.getSecurityState()) &&
                        EmployeeState.WORK_PERMIT_CHECK_FINISHED.equals(state)
        );
    }
}
