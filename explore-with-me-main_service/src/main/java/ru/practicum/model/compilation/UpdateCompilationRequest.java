package ru.practicum.model.compilation;

import lombok.Data;

@Data
public class UpdateCompilationRequest {

    private String title;
    private Long[] events;
    private Boolean pinned;

}
