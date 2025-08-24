package com.example.patient.dto;

import com.example.patient.entity.Patient;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class PatientDto {
    @Getter
    @Builder
    public static class Request {
        private Long hospitalId;
        private String name;
        private String registrationNumber;
        private String genderCode;
        private String birthDate;
        private String mobilePhoneNumber;

        public static Request toDto(Patient entity) {
            return Request.builder()
                    .hospitalId(entity.getHospital().getId())
                    .name(entity.getName())
                    .registrationNumber(entity.getRegistrationNumber())
                    .genderCode(entity.getGenderCode().name())
                    .birthDate(entity.getBirthDate() != null ? entity.getBirthDate().toString() : null)
                    .mobilePhoneNumber(entity.getMobilePhoneNumber())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private Long hospitalId;
        private String name;
        private String registrationNumber;
        private String genderCode;
        private String birthDate;
        private String mobilePhoneNumber;

        public static Response toDto(Patient entity) {
            return Response.builder()
                    .id(entity.getId())
                    .hospitalId(entity.getHospital().getId())
                    .name(entity.getName())
                    .registrationNumber(entity.getRegistrationNumber())
                    .genderCode(entity.getGenderCode().name())
                    .birthDate(entity.getBirthDate() != null ? entity.getBirthDate().toString() : null)
                    .mobilePhoneNumber(entity.getMobilePhoneNumber())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Update {
        private Long id;
        private String name;
        private String genderCode;
        private String birthDate;
        private String mobilePhoneNumber;

        public static Update toDto(Patient entity) {
            return Update.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .genderCode(entity.getGenderCode().name())
                    .birthDate(entity.getBirthDate() != null ? entity.getBirthDate().toString() : null)
                    .mobilePhoneNumber(entity.getMobilePhoneNumber())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class PatientDetailResponse {
        private PatientDto.Response patient;
        private List<VisitDto.Response> visits;
    }
}