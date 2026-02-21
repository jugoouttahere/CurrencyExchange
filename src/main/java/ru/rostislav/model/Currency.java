package ru.rostislav.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Currency {
    private int id;
    private String code;
    private String name;
    private String sign;

}
