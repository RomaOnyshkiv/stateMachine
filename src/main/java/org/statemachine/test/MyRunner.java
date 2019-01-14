package org.statemachine.test;

import lombok.extern.java.Log;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import org.statemachine.test.enums.MyEvents;
import org.statemachine.test.enums.MyStates;

@Log
@Component
public class MyRunner implements ApplicationRunner {

    private final StateMachineFactory<MyStates, MyEvents> factory;

    MyRunner(StateMachineFactory<MyStates, MyEvents> factory) {
        this.factory = factory;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

        StateMachine<MyStates, MyEvents> stateMachine = this.factory.getStateMachine();

        stateMachine.start();
        log.info("current state " + stateMachine.getState().getId().name());

        stateMachine.sendEvent(MyEvents.REQUEST_REVIEW);
        log.info("current state " + stateMachine.getState().getId().name());

        stateMachine.sendEvent(MyEvents.REQUEST_APPROVE);
        log.info("current state " + stateMachine.getState().getId().name());

        Message<MyEvents> myMessage = MessageBuilder.withPayload(MyEvents.APPROVE)
                .setHeader("a", "b").build();

        stateMachine.sendEvent(myMessage);
        log.info("current state " + stateMachine.getState().getId().name());

        stateMachine.sendEvent(MyEvents.DELETE);
        log.info("current state " + stateMachine.getState().getId().name());


    }
}
