package ru.practicum.model;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats implements Serializable {

    private String app;
    private String uri;
    private Long hits;

}
