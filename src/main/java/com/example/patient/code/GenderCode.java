package com.example.patient.code;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum GenderCode {
    M("남"),
    F("여");

    private final String desc;

    GenderCode(String desc) {
        this.desc = desc;
    }

    public static GenderCode fromCode(String code) {
        return Arrays.stream(GenderCode.values())
                .filter(g -> g.name().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 성별 코드입니다: " + code));
    }
}