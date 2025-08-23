package com.example.patient.code;

import lombok.Getter;

@Getter
public enum GenderCode {
    M("남"),
    F("여");

    private final String desc;

    GenderCode(String desc) {
        this.desc = desc;
    }
}