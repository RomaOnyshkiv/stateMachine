package org.statemachine.test;

import org.springframework.web.bind.annotation.*;


@RestController
public class WorkFlow {

    private EntityService entityService;

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
        StringBuilder builder = new StringBuilder();

        entityService.allEnt().forEach(e -> builder.append("Entity: '")
                .append(e.getId()).append("' with status: '").append(e.getState()).append("'\t"));

        return String.valueOf(builder);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create() {
        entityService.create();
        return "created";
    }


}
