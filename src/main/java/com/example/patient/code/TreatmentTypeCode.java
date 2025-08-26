package com.example.patient.code;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TreatmentTypeCode {
    D("약처방"),
    T("검사");

    private final String desc;

    TreatmentTypeCode(String desc) {
        this.desc = desc;
    }

    public static TreatmentTypeCode fromCode(String code) {
        return Arrays.stream(TreatmentTypeCode.values())
                .filter(g -> g.name().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 진료 유형 코드입니다: " + code));
    }
}