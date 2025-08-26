package com.example.patient.service;

import com.example.patient.code.VisitStatusCode;
import com.example.patient.dto.VisitDto;
import com.example.patient.entity.Hospital;
import com.example.patient.entity.Patient;
import com.example.patient.entity.Visit;
import com.example.patient.repository.HospitalRepository;
import com.example.patient.repository.PatientRepository;
import com.example.patient.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.example.patient.code.VisitStatusCode.VISITING;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class VisitServiceIntegrationTest {

    @Autowired
    private VisitService visitService;
    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private PatientRepository patientRepository;

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
                .registrationNumber("REG20250001")
                .genderCode(com.example.patient.code.GenderCode.F)
                .birthDate(java.time.LocalDate.of(2000, 1, 1))
                .mobilePhoneNumber("01099998888")
                .build();
        patient = patientRepository.save(patient);
    }

    @Test
    void testCreateAndReadVisit_success() {
        // given
        VisitDto.Request request = VisitDto.Request.builder()
                .hospitalId(hospital.getId())
                .patientId(patient.getId())
                .visitStatusCode("VISITING")
                .receptionDateTime(LocalDateTime.of(2025, 8, 26, 15, 0))
                .build();

        // when
        VisitDto.Response response = visitService.create(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getHospitalId()).isEqualTo(hospital.getId());
        assertThat(response.getPatientId()).isEqualTo(patient.getId());
        assertThat(response.getVisitStatusCode()).isEqualTo("VISITING");
        assertThat(response.getReceptionDateTime()).isEqualTo("2025-08-26T15:00:00");

        Visit visit = visitRepository.findById(response.getId()).orElse(null);
        assertThat(visit).isNotNull();
        assertThat(visit.getPatient().getId()).isEqualTo(patient.getId());
        assertThat(visit.getVisitStatusCode()).isEqualTo(VisitStatusCode.VISITING);
    }

    @Test
    void testUpdateVisit_success() {
        // given
        Visit visit = Visit.builder()
                .hospital(hospital)
                .patient(patient)
                .visitStatusCode(VISITING)
                .receptionDateTime(LocalDateTime.of(2025, 8, 26, 15, 0))
                .build();
        visit = visitRepository.save(visit);

        VisitDto.Update updateDto = VisitDto.Update.builder()
                .id(visit.getId())
                .visitStatusCode("COMPLETE")
                .receptionDateTime(LocalDateTime.of(2025, 9, 1, 14, 0))
                .build();

        // when
        VisitDto.Response updated = visitService.update(updateDto);

        // then
        assertThat(updated.getVisitStatusCode()).isEqualTo("COMPLETE");
        assertThat(updated.getReceptionDateTime()).isEqualTo("2025-09-01T14:00:00");
    }

    @Test
    void testDeleteVisit_success() {
        // given
        Visit visit = Visit.builder()
                .hospital(hospital)
                .patient(patient)
                .visitStatusCode(VisitStatusCode.VISITING)
                .receptionDateTime(LocalDateTime.of(2025, 8, 26, 15, 0))
                .build();
        visit = visitRepository.save(visit);

        // when
        visitService.delete(visit.getId());

        // then
        assertThat(visitRepository.findById(visit.getId())).isEmpty();
    }
}