package com.example.patient.enum;

public enum GenderCode {
    M("남"),
    F("여");

    private final String desc;

    GenderCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}