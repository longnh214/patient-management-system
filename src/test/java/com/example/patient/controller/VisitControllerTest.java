package com.example.patient.controller;

import com.example.patient.code.VisitStatusCode;
import com.example.patient.dto.VisitDto;
import com.example.patient.entity.Hospital;
import com.example.patient.entity.Patient;
import com.example.patient.entity.Visit;
import com.example.patient.repository.HospitalRepository;
import com.example.patient.repository.PatientRepository;
import com.example.patient.repository.VisitRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class VisitControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private VisitRepository visitRepository;
    @Autowired private HospitalRepository hospitalRepository;
    @Autowired private PatientRepository patientRepository;

    private Hospital hospital;
    private Patient patient;

    @BeforeEach
    void setup() {
        hospital = Hospital.builder()
                .hospitalName("테스트병원")
                .institutionNumber("Y0001")
                .hospitalDirectorName("홍길동")
                .build();
        hospital = hospitalRepository.save(hospital);

        patient = Patient.builder()
                .hospital(hospital)
                .name("테스터환자")
                .registrationNumber("000500028")
                .genderCode(com.example.patient.code.GenderCode.F)
                .birthDate(java.time.LocalDate.of(2000, 1, 1))
                .mobilePhoneNumber("01099998888")
                .build();
        patient = patientRepository.save(patient);
    }

    @Test
    void createVisit_success() throws Exception {
        VisitDto.Request request = VisitDto.Request.builder()
                .hospitalId(hospital.getId())
                .patientId(patient.getId())
                .visitStatusCode("VISITING")
                .receptionDateTime(LocalDateTime.of(2025, 8, 26, 15, 0))
                .build();

        mockMvc.perform(post("/api/visit")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("visit-create",
                        requestFields(
                                fieldWithPath("hospitalId").description("병원 고유번호"),
                                fieldWithPath("patientId").description("환자 고유번호"),
                                fieldWithPath("visitStatusCode").description("방문 상태(VISITING, COMPLETE, CANCELLED)"),
                                fieldWithPath("receptionDateTime").description("방문(접수) 일시")
                        ),
                        responseFields(
                                fieldWithPath("id").description("방문 고유번호"),
                                fieldWithPath("hospitalId").description("병원 고유번호"),
                                fieldWithPath("patientId").description("환자 고유번호"),
                                fieldWithPath("visitStatusCode").description("방문 상태"),
                                fieldWithPath("receptionDateTime").description("접수일시")
                        )
                ));
    }

    @Test
    void updateVisit_success() throws Exception {
        Visit visit = Visit.builder()
                .hospital(hospital)
                .patient(patient)
                .visitStatusCode(VisitStatusCode.VISITING)
                .receptionDateTime(LocalDateTime.of(2025, 8, 26, 15, 0))
                .build();
        visit = visitRepository.save(visit);

        VisitDto.Update updateDto = VisitDto.Update.builder()
                .id(visit.getId())
                .visitStatusCode("COMPLETE")
                .receptionDateTime(LocalDateTime.of(2025, 9, 1, 14, 0))
                .build();

        mockMvc.perform(put("/api/visit/{id}", visit.getId())
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("visit-update",
                        pathParameters(
                                parameterWithName("id").description("방문 고유번호")
                        ),
                        requestFields(
                                fieldWithPath("id").description("방문 고유번호"),
                                fieldWithPath("visitStatusCode").description("방문 상태"),
                                fieldWithPath("receptionDateTime").description("접수일시")
                        ),
                        responseFields(
                                fieldWithPath("id").description("방문 고유번호"),
                                fieldWithPath("hospitalId").description("병원 고유번호"),
                                fieldWithPath("patientId").description("환자 고유번호"),
                                fieldWithPath("visitStatusCode").description("방문 상태"),
                                fieldWithPath("receptionDateTime").description("접수일시")
                        )
                ));
    }

    @Test
    void deleteVisit_success() throws Exception {
        Visit visit = Visit.builder()
                .hospital(hospital)
                .patient(patient)
                .visitStatusCode(VisitStatusCode.VISITING)
                .receptionDateTime(LocalDateTime.of(2025, 8, 26, 15, 0))
                .build();
        visit = visitRepository.save(visit);

        mockMvc.perform(delete("/api/visit/{id}", visit.getId()))
                .andExpect(status().isNoContent())
                .andDo(document("visit-delete",
                        pathParameters(
                                parameterWithName("id").description("방문 고유번호")
                        )
                ));
    }

    @Test
    void readVisit_success() throws Exception {
        Visit visit = Visit.builder()
                .hospital(hospital)
                .patient(patient)
                .visitStatusCode(VisitStatusCode.VISITING)
                .receptionDateTime(LocalDateTime.of(2025, 8, 26, 15, 0))
                .build();
        visit = visitRepository.save(visit);

        mockMvc.perform(get("/api/visit/{id}", visit.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("visit-read",
                        pathParameters(
                                parameterWithName("id").description("방문 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("id").description("방문 고유번호"),
                                fieldWithPath("hospitalId").description("병원 고유번호"),
                                fieldWithPath("patientId").description("환자 고유번호"),
                                fieldWithPath("visitStatusCode").description("방문 상태"),
                                fieldWithPath("receptionDateTime").description("접수일시")
                        )
                ));
    }

    @Test
    void listVisits_success() throws Exception {
        for (int i=0;i<3;i++) {
            visitRepository.save(Visit.builder()
                    .hospital(hospital)
                    .patient(patient)
                    .visitStatusCode(VisitStatusCode.VISITING)
                    .receptionDateTime(LocalDateTime.of(2025, 8, 28, 15, 0).plusDays(i))
                    .build());
        }

        mockMvc.perform(get("/api/visit")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("visit-list",
                        responseFields(
                                fieldWithPath("[].id").description("방문 고유번호"),
                                fieldWithPath("[].hospitalId").description("병원 고유번호"),
                                fieldWithPath("[].patientId").description("환자 고유번호"),
                                fieldWithPath("[].visitStatusCode").description("방문 상태"),
                                fieldWithPath("[].receptionDateTime").description("접수일시")
                        )
                ));
    }
}