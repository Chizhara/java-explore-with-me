package ru.practicum.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LocalDateTimeAfterValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalDateTimeAfter {
    String message() default "must contain a date that not yet occurred";

    int hours();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
