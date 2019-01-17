package org.statemachine.test;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;
import org.statemachine.test.enums.MyEvents;
import org.statemachine.test.enums.MyStates;

import java.util.*;

@Service
public class EntityService {

    private final EntityRepo entityRepo;
    private final StateMachineFactory<MyStates, MyEvents> factory;

    private static final String ID_HEADER = "Entity_ID";

    EntityService(EntityRepo entityRepo, StateMachineFactory<MyStates, MyEvents> factory) {
        this.entityRepo = entityRepo;
        this.factory = factory;
    }


    MyEntity create() {
        return this.entityRepo.save(new MyEntity(MyStates.NEW));
    }

    List<MyEntity> allEnt() {
        return this.entityRepo.findAll();
    }

    MyEntity byId(Long id) {
        return this.entityRepo.findAllById(Collections.singletonList(id)).get(0);
    }


    StateMachine<MyStates, MyEvents> delete(Long entityId) {
        StateMachine<MyStates, MyEvents> sm = this.build(entityId);

        Message<MyEvents> deleteMsg = MessageBuilder.withPayload(MyEvents.DELETE)
                .setHeader(ID_HEADER, entityId).build();

        sm.sendEvent(deleteMsg);
        return sm;
    }

    StateMachine<MyStates, MyEvents> approve(Long entityId) {
        StateMachine<MyStates, MyEvents> sm = this.build(entityId);

        Message<MyEvents> approveMsg = MessageBuilder.withPayload(MyEvents.APPROVE)
                .setHeader(ID_HEADER, entityId).build();

        sm.sendEvent(approveMsg);
        return sm;
    }

    StateMachine<MyStates, MyEvents> requestApprove(Long entityId) {
        StateMachine<MyStates, MyEvents> sm = this.build(entityId);

        Message<MyEvents> requestApproveMsg = MessageBuilder.withPayload(MyEvents.REQUEST_APPROVE)
                .setHeader(ID_HEADER, entityId).build();

        sm.sendEvent(requestApproveMsg);
        return sm;
    }

    StateMachine<MyStates, MyEvents> requestReview(Long entityId) {

        StateMachine<MyStates, MyEvents> sm = this.build(entityId);

        Message<MyEvents> reviewMsg = MessageBuilder.withPayload(MyEvents.REQUEST_REVIEW)
                .setHeader(ID_HEADER, entityId).build();

        sm.sendEvent(reviewMsg);
        return sm;
    }



    private StateMachine<MyStates, MyEvents> build(Long id) {
        List<MyEntity> entities = this.entityRepo.findAllById(Collections.singletonList(id));
        MyEntity entity = entities.get(0);
        String entityKey = Long.toString(entity.getId());


        StateMachine<MyStates, MyEvents> sm = this.factory.getStateMachine(entityKey);
        sm.stop();
        sm.getStateMachineAccessor().doWithAllRegions(sma -> {
            sma.addStateMachineInterceptor(new StateMachineInterceptorAdapter<>() {
                @Override
                public void preStateChange(State<MyStates, MyEvents> state, Message<MyEvents> message, Transition<MyStates, MyEvents> transition, StateMachine<MyStates, MyEvents> stateMachine) {

                    Optional.ofNullable(message).ifPresent(msg -> Optional.ofNullable((Long) msg.getHeaders().getOrDefault(ID_HEADER, -1L))
                            .ifPresent(entityKey1 -> {
                                MyEntity entity1 = entityRepo.getOne(entityKey1);
                                entity1.setState(state.getId());
                                entityRepo.save(entity1);
                            }));
                }
            });
            sma.resetStateMachine(new DefaultStateMachineContext<>(entity.getState(), null, null, null));
        });
        sm.start();
        return sm;
    }

}
