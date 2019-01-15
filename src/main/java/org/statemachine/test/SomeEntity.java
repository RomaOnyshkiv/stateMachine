package org.statemachine.test;

import lombok.Getter;
import lombok.Setter;
import org.statemachine.test.enums.MyStates;

@Setter
@Getter
public class SomeEntity {

    private MyStates state;

    SomeEntity() {
        state = MyStates.NEW;
    }

}
