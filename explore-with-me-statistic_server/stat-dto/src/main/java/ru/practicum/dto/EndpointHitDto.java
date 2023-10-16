package ru.practicum.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class EndpointHitDto {

    private Long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;

}
