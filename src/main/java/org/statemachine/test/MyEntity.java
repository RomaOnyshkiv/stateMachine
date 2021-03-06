package org.statemachine.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.statemachine.test.enums.MyStates;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Some")
@Data
@NoArgsConstructor
@AllArgsConstructor
class MyEntity {


    @Id
    @GeneratedValue
    private Long id;
    private String state;

    MyEntity(MyStates s) {
        this.setState(s);
    }

    MyStates getState() {
        return MyStates.valueOf(state);
    }

    void setState(MyStates s) {
        this.state = s.name();
    }




}
