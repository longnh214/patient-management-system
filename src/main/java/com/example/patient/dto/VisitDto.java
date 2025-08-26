package com.example.patient.dto;

import com.example.patient.entity.Visit;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class VisitDto {
    @Getter
    @Builder
    public static class Request {
        private Long hospitalId;
        private Long patientId;
        private LocalDateTime receptionDateTime;
        private String visitStatusCode;

        public static Request toDto(Visit entity) {
            return Request.builder()
                    .hospitalId(entity.getHospital().getId())
                    .patientId(entity.getPatient().getId())
                    .receptionDateTime(entity.getReceptionDateTime())
                    .visitStatusCode(entity.getVisitStatusCode().name())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private Long hospitalId;
        private Long patientId;
        private LocalDateTime receptionDateTime;
        private String visitStatusCode;

        public static Response toDto(Visit entity) {
            return Response.builder()
                    .id(entity.getId())
                    .hospitalId(entity.getHospital().getId())
                    .patientId(entity.getPatient().getId())
                    .receptionDateTime(entity.getReceptionDateTime())
                    .visitStatusCode(entity.getVisitStatusCode().name())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Update {
        private Long id;
        private String visitStatusCode;
        private LocalDateTime receptionDateTime;

        public static Update toDto(Visit entity) {
            return Update.builder()
                    .id(entity.getId())
                    .visitStatusCode(entity.getVisitStatusCode().name())
                    .receptionDateTime(entity.getReceptionDateTime())
                    .build();
        }
    }
}