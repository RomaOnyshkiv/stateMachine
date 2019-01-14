package org.statemachine.test;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import org.statemachine.test.enums.MyEvents;
import org.statemachine.test.enums.MyStates;

@Log
@Component
public class MyRunner implements ApplicationRunner {

    private final StateMachineFactory<MyStates, MyEvents> factory;

    public MyRunner(StateMachineFactory<MyStates, MyEvents> factory) {
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


        stateMachine.sendEvent(MyEvents.APPROVE);
        log.info("current state " + stateMachine.getState().getId().name());


    }
}
