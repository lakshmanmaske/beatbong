package com.app.beatbong.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Roles {

    USER(1,"user","User Description");

    private Integer id;
    private String role;
    private String description;
}
