package org.statemachine.test;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class WorkFlow {

    EntityService entityService;

    WorkFlow(EntityService entityService) {
        this.entityService = entityService;
    }


    @RequestMapping(value = "/reqReview/{id}", method = RequestMethod.GET)
    public String changeState(@PathVariable int id) {
        entityService.requestReview((long) id);
        return "done";
    }


    @GetMapping(value = "/ent")
    public String allEnt() {
        List<MyEntity> entities = entityService.allEnt();
        StringBuilder builder = new StringBuilder();

        for (MyEntity e : entities) {
            builder.append("Ent: ").append(e.getId()).append(" with status ").append(e.getState());
        }

        return String.valueOf(builder);
    }


}
