package com.example.patient.code;

import lombok.Getter;

@Getter
public enum TreatmentCode {
    D("약처방"),
    T("검사");

    private final String description;

    TreatmentCode(String description) {
        this.description = description;
    }
}