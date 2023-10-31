package ru.practicum.annotation;

import ru.practicum.model.event.StateAction;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class StateActionOneOfValidator implements ConstraintValidator<StateActionOneOf, StateAction> {
    StateAction[] stateActions;

    @Override
    public void initialize(StateActionOneOf stateActionOneOf) {
        this.stateActions = stateActionOneOf.anyOf();
    }

    @Override
    public boolean isValid(StateAction stateAction, ConstraintValidatorContext cxt) {
        return stateAction == null || Arrays.asList(stateActions).contains(stateAction);
    }

}
