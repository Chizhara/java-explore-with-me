package ru.practicum.model.compilation.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class NewCompilationDto {

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
    private Boolean pinned;
    private Set<Long> events;

}
