package org.statemachine.test;

import lombok.extern.java.Log;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.statemachine.test.enums.MyEvents;
import org.statemachine.test.enums.MyStates;

@Log
@Component
public class MyRunner implements ApplicationRunner {

    private final EntityService service;

    MyRunner(EntityService service) {
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        MyEntity entity = this.service.create();

        StateMachine<MyStates, MyEvents> requestReviewMachine = service.requestReview(entity.getId());
        log.info("Entity: " + service.byId(entity.getId()));

        StateMachine<MyStates, MyEvents> requestApproveMachine = service.requestApprove(entity.getId());
        log.info("Entity: " + service.byId(entity.getId()));

        StateMachine<MyStates, MyEvents> approveMachine = service.approve(entity.getId());
        log.info("Entity: " + service.byId(entity.getId()));

        StateMachine<MyStates, MyEvents> deleteMachine = service.delete(entity.getId());
        log.info("Entity: " + service.byId(entity.getId()));


    }
}
