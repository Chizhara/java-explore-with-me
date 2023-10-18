package ru.practicum.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class ViewStats implements Serializable {

    private String app;
    private String uri;
    private Long hits;

}
