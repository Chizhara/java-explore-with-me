package ru.practicum.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class EndpointHit {

    private Long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;

}
