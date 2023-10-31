package ru.practicum.model.request.dto;

import lombok.Data;
import ru.practicum.model.request.ParticipationRequestStatus;

import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto {

    private Long id;
    private Long event;
    private Long requester;
    private ParticipationRequestStatus status;
    private LocalDateTime created;

}
