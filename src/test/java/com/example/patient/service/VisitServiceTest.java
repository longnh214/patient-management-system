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
import org.mockito.ArgumentMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

class VisitServiceTest {

    private VisitRepository visitRepository = mock(VisitRepository.class);
    private HospitalRepository hospitalRepository = mock(HospitalRepository.class);
    private PatientRepository patientRepository = mock(PatientRepository.class);
    private VisitService visitService;

    @BeforeEach
    void setUp() {
        visitService = new VisitService(visitRepository, hospitalRepository, patientRepository);
    }

    @Test
    void testCreate() {
        // given
        Long hospitalId = 1L;
        Long patientId = 2L;
        Hospital hospital = Hospital.builder().id(hospitalId).build();
        Patient patient = Patient.builder().id(patientId).build();

        VisitDto.Request request = VisitDto.Request.builder()
                .hospitalId(hospitalId)
                .patientId(patientId)
                .visitStatusCode("VISITING")
                .receptionDateTime(LocalDateTime.of(2025, 8, 26, 11, 0))
                .build();

        Visit savedVisit = Visit.builder()
                .hospital(hospital)
                .patient(patient)
                .visitStatusCode(VisitStatusCode.VISITING)
                .receptionDateTime(LocalDateTime.of(2025, 8, 26, 11, 0))
                .build();

        given(hospitalRepository.findById(hospitalId)).willReturn(Optional.of(hospital));
        given(patientRepository.findById(patientId)).willReturn(Optional.of(patient));
        given(visitRepository.save(ArgumentMatchers.any(Visit.class))).willReturn(savedVisit);

        // when
        VisitDto.Response response = visitService.create(request);

        // then
        assertThat(response.getId()).isEqualTo(savedVisit.getId());
        assertThat(response.getHospitalId()).isEqualTo(hospitalId);
        assertThat(response.getPatientId()).isEqualTo(patientId);
        assertThat(response.getVisitStatusCode()).isEqualTo("VISITING");
    }

    @Test
    void testRead() {
        // given
        Long visitId = 1L;
        Hospital hospital = Hospital.builder().id(1L).build();
        Patient patient = Patient.builder().id(2L).build();
        Visit visit = Visit.builder()
                .id(visitId)
                .hospital(hospital)
                .patient(patient)
                .visitStatusCode(VisitStatusCode.VISITING)
                .receptionDateTime(LocalDateTime.of(2025, 8, 26, 11, 0))
                .build();

        given(visitRepository.findById(visitId)).willReturn(Optional.of(visit));

        // when
        VisitDto.Response response = visitService.read(visitId);

        // then
        assertThat(response.getId()).isEqualTo(visitId);
        assertThat(response.getVisitStatusCode()).isEqualTo("VISITING");
        assertThat(response.getReceptionDateTime()).isEqualTo(LocalDateTime.of(2025, 8, 26, 11, 0).toString());
    }

    @Test
    void testUpdate() {
        // given
        Long visitId = 1L;
        Hospital hospital = Hospital.builder().id(1L).build();
        Patient patient = Patient.builder().id(2L).build();
        Visit visit = Visit.builder()
                .id(visitId)
                .hospital(hospital)
                .patient(patient)
                .visitStatusCode(VisitStatusCode.VISITING)
                .receptionDateTime(LocalDateTime.of(2025, 8, 26, 11, 0))
                .build();

        VisitDto.Update updateDto = VisitDto.Update.builder()
                .id(visitId)
                .visitStatusCode("COMPLETE")
                .receptionDateTime(LocalDateTime.of(2025, 9, 1, 14, 0))
                .build();

        given(visitRepository.findById(visitId)).willReturn(Optional.of(visit));

        // when
        VisitDto.Response response = visitService.update(updateDto);

        // then
        assertThat(response.getVisitStatusCode()).isEqualTo("COMPLETE");
        assertThat(response.getReceptionDateTime()).isEqualTo("2025-09-01T14:00:00");
    }

    @Test
    void testDelete() {
        // given
        Long visitId = 7L;
        Hospital hospital = Hospital.builder().id(1L).build();
        Patient patient = Patient.builder().id(2L).build();
        Visit visit = Visit.builder()
                .id(visitId)
                .hospital(hospital)
                .patient(patient)
                .build();

        given(visitRepository.findById(visitId)).willReturn(Optional.of(visit));
        willDoNothing().given(visitRepository).delete(visit);

        // when/then
        visitService.delete(visitId);
        verify(visitRepository, times(1)).delete(visit);
    }
}
