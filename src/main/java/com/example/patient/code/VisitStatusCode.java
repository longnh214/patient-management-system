package com.example.patient.code;

import lombok.Getter;

@Getter
public enum VisitStatusCode {
    VISITING("1", "방문중"),
    COMPLETE("2", "종료"),
    CANCELED("3", "취소");

    private final String code;
    private final String desc;

    VisitStatusCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}