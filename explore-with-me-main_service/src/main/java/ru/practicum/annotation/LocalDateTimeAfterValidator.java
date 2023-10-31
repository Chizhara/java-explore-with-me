package ru.practicum.annotation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
public class LocalDateTimeAfterValidator implements ConstraintValidator<LocalDateTimeAfter, LocalDateTime> {
    int hours;

    @Override
    public void initialize(LocalDateTimeAfter localDateTimeAfter) {
        this.hours = localDateTimeAfter.hours();
    }

    @Override
    public boolean isValid(LocalDateTime date,
                           ConstraintValidatorContext cxt) {
        log.info("VALIDATION date = {}", date);
        return date == null || date.isAfter(LocalDateTime.now().plusHours(2));
    }
}
