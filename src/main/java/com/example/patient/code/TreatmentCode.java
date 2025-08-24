package com.example.patient.code;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TreatmentCode {
    D("약처방"),
    T("검사");

    private final String description;

    TreatmentCode(String description) {
        this.description = description;
    }

    public static TreatmentCode fromCode(String code) {
        return Arrays.stream(TreatmentCode.values())
                .filter(g -> g.name().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 진료 유형 코드입니다: " + code));
    }
}