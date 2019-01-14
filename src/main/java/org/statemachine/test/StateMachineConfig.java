package org.statemachine.test;

import lombok.extern.java.Log;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.statemachine.test.enums.MyEvents;
import org.statemachine.test.enums.MyStates;


@Log
@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<MyStates, MyEvents> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<MyStates, MyEvents> config) throws Exception {
        StateMachineListenerAdapter<MyStates, MyEvents> adapter = new StateMachineListenerAdapter<MyStates, MyEvents>() {
            @Override
            public void stateChanged(State<MyStates, MyEvents> from, State<MyStates, MyEvents> to) {
                log.info(String.format("state changed (from: %s to %s)", from + " ", to + " "));
            }
        };

        config
                .withConfiguration()
                .autoStartup(false)
                .listener(adapter);
    }


    @Override
    public void configure(StateMachineStateConfigurer<MyStates, MyEvents> states) throws Exception {

        states.withStates()
                .initial(MyStates.NEW)
                .state(MyStates.PENDING_REVIEW)
                .end(MyStates.PENDING_APPROVE)
                .end(MyStates.APPROVED);

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<MyStates, MyEvents> transitions) throws Exception {

        transitions.withExternal()
                .source(MyStates.NEW).target(MyStates.PENDING_REVIEW).event(MyEvents.REQUEST_REVIEW)
                .and().withExternal()
                .source(MyStates.PENDING_REVIEW).target(MyStates.PENDING_APPROVE).event(MyEvents.REQUEST_APPROVE)
                .and().withExternal()
                .source(MyStates.PENDING_APPROVE).target(MyStates.APPROVED).event(MyEvents.APPROVE);
    }
}
