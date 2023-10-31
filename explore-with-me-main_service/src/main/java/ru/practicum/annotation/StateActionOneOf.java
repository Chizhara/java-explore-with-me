package ru.practicum.annotation;

import ru.practicum.model.event.StateAction;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = StateActionOneOfValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(StateActionOneOf.List.class)
public @interface StateActionOneOf {
    String message() default "Invalid StateAction value";

    StateAction[] anyOf();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        StateActionOneOf[] value();
    }
}
