package com.example.patient.code;

import lombok.Getter;

import java.util.Arrays;

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

    public static VisitStatusCode fromCode(String code) {
        return Arrays.stream(VisitStatusCode.values())
                .filter(g -> g.name().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 방문 상태 코드입니다: " + code));
    }
}