package com.workmotion.task.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import com.workmotion.task.model.EmployeeState;
import com.workmotion.task.model.EmployeeStateEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableStateMachineFactory
@Configuration
public class WorkMotionAppConfig extends StateMachineConfigurerAdapter<EmployeeState, EmployeeStateEvent> {

	@Override
	public void configure(StateMachineStateConfigurer<EmployeeState, EmployeeStateEvent> states) throws Exception {

		states.withStates().initial(EmployeeState.ADDED).state(EmployeeState.IN_CHECK).join(EmployeeState.APPROVED)
				.fork(EmployeeState.IN_CHECK).and().withStates().parent(EmployeeState.IN_CHECK)
				.initial(EmployeeState.SECURITY_CHECK_STARTED).end(EmployeeState.SECURITY_CHECK_FINISHED).and()
				.withStates().parent(EmployeeState.IN_CHECK).initial(EmployeeState.WORK_PERMIT_CHECK_STARTED)
				.state(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION)
				.end(EmployeeState.WORK_PERMIT_CHECK_FINISHED).state(EmployeeState.APPROVED).end(EmployeeState.ACTIVE);
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<EmployeeState, EmployeeStateEvent> transitions)
			throws Exception {
		transitions.withExternal().source(EmployeeState.ADDED).target(EmployeeState.IN_CHECK)
				.event(EmployeeStateEvent.BEGIN_CHECK).and().withJoin().source(EmployeeState.SECURITY_CHECK_FINISHED)
				.source(EmployeeState.WORK_PERMIT_CHECK_FINISHED).target(EmployeeState.APPROVED)
				.and()
				.withFork()
				.source(EmployeeState.IN_CHECK)
				.target(EmployeeState.WORK_PERMIT_CHECK_STARTED)
				.target(EmployeeState.SECURITY_CHECK_STARTED)
				.and()
				.withLocal()
				.source(EmployeeState.SECURITY_CHECK_STARTED)
				.target(EmployeeState.SECURITY_CHECK_FINISHED)
				.event(EmployeeStateEvent.FINISH_SECURITY_CHECK)
				.and()
				.withLocal()
				.source(EmployeeState.WORK_PERMIT_CHECK_STARTED)
				.target(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION)
				.event(EmployeeStateEvent.COMPLETE_INITIAL_WORK_PERMIT_CHECK)
				.and()
				.withLocal()
				.source(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION)
				.target(EmployeeState.WORK_PERMIT_CHECK_FINISHED)
				.event(EmployeeStateEvent.FINISH_WORK_PERMIT_CHECK)
				.and()
				.withExternal()
				.target(EmployeeState.ACTIVE)
				.source(EmployeeState.APPROVED)
				.event(EmployeeStateEvent.ACTIVATE);
	}

	@Override
	public void configure(StateMachineConfigurationConfigurer<EmployeeState, EmployeeStateEvent> config)
			throws Exception {
		StateMachineListenerAdapter<EmployeeState, EmployeeStateEvent> adapter = new StateMachineListenerAdapter<EmployeeState, EmployeeStateEvent>() {
			@Override
			public void stateChanged(State<EmployeeState, EmployeeStateEvent> from,
					State<EmployeeState, EmployeeStateEvent> to) {
				log.info(String.format("State Changed from : %s, to: %s", from, to));
			}		
		};
		config.withConfiguration().listener(adapter);
	}

}
