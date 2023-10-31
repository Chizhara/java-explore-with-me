package ru.practicum.model.compilation.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Data
public class NewCompilationDto {

    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
    private Boolean pinned;
    private Collection<Long> events;

}
