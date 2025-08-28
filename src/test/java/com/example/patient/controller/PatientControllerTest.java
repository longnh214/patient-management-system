package com.example.patient.controller;

import com.example.patient.code.GenderCode;
import com.example.patient.dto.PatientDto;
import com.example.patient.entity.Hospital;
import com.example.patient.entity.Patient;
import com.example.patient.repository.HospitalRepository;
import com.example.patient.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@Transactional
public class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private Hospital hospital;

    @BeforeEach
    void setup() {
        hospital = Hospital.builder()
                .hospitalName("테스트병원")
                .institutionNumber("Y0001")
                .hospitalDirectorName("홍길동")
                .build();
        hospital = hospitalRepository.save(hospital);

        for(int i=0;i<100;i++) {
            patientRepository.save(Patient.builder()
                    .hospital(hospital)
                    .name("김철수")
                    .registrationNumber(String.format("%04d", hospital.getId()) + String.format("%05d", i + 1))
                    .genderCode(GenderCode.M)
                    .birthDate(java.time.LocalDate.of(1992, 2, 2))
                    .mobilePhoneNumber("01088887777")
                    .build());
        }
    }

    @Test
    void testCreatePatient_success() throws Exception {
        PatientDto.Request request = PatientDto.Request.builder()
                .hospitalId(hospital.getId())
                .name("신규환자")
                .genderCode(GenderCode.F.name())
                .birthDate("2000-01-12")
                .mobilePhoneNumber("01055559999")
                .build();

        mockMvc.perform(post("/api/patient")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("patient-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("hospitalId").description("병원 고유번호"),
                                fieldWithPath("name").description("환자 이름"),
                                fieldWithPath("genderCode").description("성별 코드"),
                                fieldWithPath("birthDate").description("생년월일 (yyyy-MM-dd)"),
                                fieldWithPath("mobilePhoneNumber").description("핸드폰 번호"),
                                fieldWithPath("registrationNumber").optional().description("등록 번호 (서버가 생성)")
                        )
                ));
    }

    @Test
    void testUpdatePatient_success() throws Exception {
        Patient updated = patientRepository.findAll().get(0);
        PatientDto.Update updateRequest = PatientDto.Update.builder()
                .id(updated.getId())
                .name("이름수정")
                .genderCode(GenderCode.M.name())
                .birthDate("1995-07-01")
                .mobilePhoneNumber("01012345678")
                .build();

        mockMvc.perform(put("/api/patient/{id}", updated.getId())
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("patient-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("수정할 환자 고유번호")
                        ),
                        requestFields(
                                fieldWithPath("id").description("환자 고유번호"),
                                fieldWithPath("name").description("수정된 이름"),
                                fieldWithPath("genderCode").description("수정된 성별"),
                                fieldWithPath("birthDate").description("수정된 생년월일 (yyyy-MM-dd)"),
                                fieldWithPath("mobilePhoneNumber").description("수정된 핸드폰 번호")
                        ),
                        responseFields(
                                fieldWithPath("id").description("환자 고유번호"),
                                fieldWithPath("hospitalId").description("병원 고유번호"),
                                fieldWithPath("name").description("환자 이름"),
                                fieldWithPath("registrationNumber").description("등록번호"),
                                fieldWithPath("genderCode").description("성별 코드"),
                                fieldWithPath("birthDate").description("생년월일 (yyyy-MM-dd)"),
                                fieldWithPath("mobilePhoneNumber").description("핸드폰 번호")
                        )
                ));
    }

    @Test
    void testDeletePatient_success() throws Exception {
        Patient target = patientRepository.findAll().get(0);

        mockMvc.perform(delete("/api/patient/{id}", target.getId()))
                .andExpect(status().isNoContent())
                .andDo(document("patient-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("삭제할 환자 고유번호")
                        )
                ));
    }

    @Test
    void testReadPatient_success() throws Exception {
        Patient target = patientRepository.findAll().get(0);

        mockMvc.perform(get("/api/patient/{id}", target.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("patient-read",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("환자 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("patient.id").description("환자 고유번호"),
                                fieldWithPath("patient.hospitalId").description("병원 고유번호"),
                                fieldWithPath("patient.name").description("환자 이름"),
                                fieldWithPath("patient.registrationNumber").description("등록번호"),
                                fieldWithPath("patient.genderCode").description("성별 코드"),
                                fieldWithPath("patient.birthDate").description("생년월일 (yyyy-MM-dd)"),
                                fieldWithPath("patient.mobilePhoneNumber").description("핸드폰 번호"),
                                fieldWithPath("visits").description("환자의 방문목록")
                        )
                ));
    }

    @Test
    void testGetPatients_success() throws Exception {
        mockMvc.perform(get("/api/patient")
                        .param("name", "김철수")
                        .param("registrationNumber", "")
                        .param("birthDate", "")
                        .param("pageNo", "1")
                        .param("pageSize", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("patients-list-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("name").description("환자 이름"),
                                parameterWithName("registrationNumber").description("등록 번호"),
                                parameterWithName("birthDate").description("생년월일 (yyyy-MM-dd)"),
                                parameterWithName("pageNo").description("페이지 번호 (1부터 시작)"),
                                parameterWithName("pageSize").description("한 페이지당 항목 수")
                        ),
                        responseFields(
                                fieldWithPath("content").description("결과 객체 리스트"),
                                fieldWithPath("content[].id").optional().description("환자 고유번호"),
                                fieldWithPath("content[].hospitalId").optional().description("병원 고유번호"),
                                fieldWithPath("content[].name").optional().description("환자 이름"),
                                fieldWithPath("content[].registrationNumber").optional().description("등록번호"),
                                fieldWithPath("content[].genderCode").optional().description("성별 코드"),
                                fieldWithPath("content[].birthDate").optional().description("생년월일 (yyyy-MM-dd)"),
                                fieldWithPath("content[].mobilePhoneNumber").optional().description("핸드폰 번호"),

                                fieldWithPath("pageable").description("페이징 정보"),
                                fieldWithPath("pageable.sort").description("정렬 정보"),
                                fieldWithPath("pageable.sort.empty").description("정렬 비어있음 여부"),
                                fieldWithPath("pageable.sort.unsorted").description("정렬되지 않았는지 여부"),
                                fieldWithPath("pageable.sort.sorted").description("정렬되었는지 여부"),
                                fieldWithPath("pageable.offset").description("offset 값"),
                                fieldWithPath("pageable.pageSize").description("페이지 크기"),
                                fieldWithPath("pageable.pageNumber").description("페이지 번호"),
                                fieldWithPath("pageable.paged").description("페이징 처리 여부"),
                                fieldWithPath("pageable.unpaged").description("페이징 처리되지 않았는지 여부"),

                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("totalElements").description("전체 검색 결과 수"),
                                fieldWithPath("last").description("마지막 페이지 여부"),
                                fieldWithPath("size").description("요청한 페이지 크기"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("sort").description("정렬 정보"),
                                fieldWithPath("sort.empty").description("정렬 비어있음 여부"),
                                fieldWithPath("sort.unsorted").description("정렬되지 않았는지 여부"),
                                fieldWithPath("sort.sorted").description("정렬되었는지 여부"),
                                fieldWithPath("numberOfElements").description("현재 페이지 내 결과 개수"),
                                fieldWithPath("first").description("첫 번째 페이지 여부"),
                                fieldWithPath("empty").description("결과가 비었는지 여부")
                        )
                ));
    }

    @Test
    void testGetPatients_failure_noMatch() throws Exception {
        mockMvc.perform(get("/api/patient")
                        .param("name", "없는이름")
                        .param("registrationNumber", "")
                        .param("birthDate", "")
                        .param("pageNo", "1")
                        .param("pageSize", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("patients-list-empty",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("name").description("검색 이름"),
                                parameterWithName("registrationNumber").description("등록 번호"),
                                parameterWithName("birthDate").description("생년월일 (yyyy-MM-dd)"),
                                parameterWithName("pageNo").description("페이지 번호"),
                                parameterWithName("pageSize").description("한 페이지당 항목 수")
                        ),
                        responseFields(
                                fieldWithPath("content").description("검색 결과 리스트 (비어있을 수 있음)"),

                                fieldWithPath("pageable").description("페이징 정보"),
                                fieldWithPath("pageable.sort").description("정렬 정보"),
                                fieldWithPath("pageable.sort.empty").description("정렬 비어있음 여부"),
                                fieldWithPath("pageable.sort.unsorted").description("정렬되지 않았는지 여부"),
                                fieldWithPath("pageable.sort.sorted").description("정렬되었는지 여부"),
                                fieldWithPath("pageable.offset").description("offset 값"),
                                fieldWithPath("pageable.pageSize").description("페이지 크기"),
                                fieldWithPath("pageable.pageNumber").description("페이지 번호"),
                                fieldWithPath("pageable.paged").description("페이징 처리 여부"),
                                fieldWithPath("pageable.unpaged").description("페이징 처리되지 않았는지 여부"),

                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("totalElements").description("전체 검색 결과 수"),
                                fieldWithPath("last").description("마지막 페이지 여부"),
                                fieldWithPath("size").description("요청한 페이지 크기"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("sort").description("정렬 정보"),
                                fieldWithPath("sort.empty").description("정렬 비어있음 여부"),
                                fieldWithPath("sort.unsorted").description("정렬되지 않았는지 여부"),
                                fieldWithPath("sort.sorted").description("정렬되었는지 여부"),
                                fieldWithPath("numberOfElements").description("현재 페이지 내 결과 개수"),
                                fieldWithPath("first").description("첫 번째 페이지 여부"),
                                fieldWithPath("empty").description("결과가 비었는지 여부")
                        )
                ));
    }
}