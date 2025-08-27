package com.example.patient.service;

import com.example.patient.code.GenderCode;
import com.example.patient.dto.PatientDto;
import com.example.patient.entity.Hospital;
import com.example.patient.entity.Patient;
import com.example.patient.repository.HospitalRepository;
import com.example.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

class PatientServiceTest {

    private PatientRepository patientRepository = mock(PatientRepository.class);
    private HospitalRepository hospitalRepository = mock(HospitalRepository.class);
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        patientService = new PatientService(patientRepository, hospitalRepository);
    }

    @Test
    void testCreate() {
        // given
        Long hospitalId = 1L;
        Hospital hospital = Hospital.builder()

                .id(hospitalId)
                .build();

        PatientDto.Request request = PatientDto.Request.builder()
                .hospitalId(hospitalId)
                .name("홍길동")
                .registrationNumber("000100001")
                .genderCode(GenderCode.M.name())
                .birthDate("1990-01-01")
                .mobilePhoneNumber("01012345678")
                .build();

        Patient savedPatient = Patient.builder()
                .id(1L)
                .hospital(hospital)
                .name("홍길동")
                .registrationNumber("000100001")
                .genderCode(GenderCode.M)
                .birthDate(LocalDate.parse("1990-01-01"))
                .mobilePhoneNumber("01012345678")
                .build();

        given(hospitalRepository.findById(hospitalId)).willReturn(Optional.of(hospital));
        given(patientRepository.save(ArgumentMatchers.any(Patient.class))).willReturn(savedPatient);

        // when
        PatientDto.Response response = patientService.create(request);

        // then
        assertThat(response.getId()).isEqualTo(savedPatient.getId());
        assertThat(response.getName()).isEqualTo(savedPatient.getName());
        assertThat(response.getGenderCode()).isEqualTo(savedPatient.getGenderCode().name());
    }

    @Test
    void testRead() {
        // given
        Long patientId = 1L;
        Hospital hospital = Hospital.builder().id(1L).build();
        Patient patient = Patient.builder()
                .id(patientId)
                .hospital(hospital)
                .name("테스터")
                .registrationNumber("001200010")
                .genderCode(GenderCode.F)
                .birthDate(LocalDate.of(1985, 5, 10))
                .mobilePhoneNumber("01098765432")
                .build();

        given(patientRepository.findById(patientId)).willReturn(Optional.of(patient));

        // when
        PatientDto.Response response = patientService.read(patientId);

        // then
        assertThat(response.getId()).isEqualTo(patientId);
        assertThat(response.getName()).isEqualTo("테스터");
        assertThat(response.getBirthDate()).isEqualTo("1985-05-10");
    }

    @Test
    void testUpdate() {
        // given
        Long patientId = 1L;
        Patient patient = Patient.builder()
                .id(patientId)
                .hospital(Hospital.builder().id(1L).build())
                .name("이전이름")
                .genderCode(GenderCode.M)
                .birthDate(LocalDate.of(1999, 1, 1))
                .mobilePhoneNumber("01022223333")
                .build();

        PatientDto.Update updateDto = PatientDto.Update.builder()
                .id(patientId)
                .name("수정이름")
                .genderCode("F")
                .birthDate("2000-12-12")
                .mobilePhoneNumber("01044445555")
                .build();

        given(patientRepository.findById(patientId)).willReturn(Optional.of(patient));

        // when
        PatientDto.Response response = patientService.update(updateDto);

        // then
        assertThat(response.getName()).isEqualTo("수정이름");
        assertThat(response.getGenderCode()).isEqualTo(GenderCode.F.name());
        assertThat(response.getBirthDate()).isEqualTo("2000-12-12");
        assertThat(response.getMobilePhoneNumber()).isEqualTo("01044445555");
    }

    @Test
    void testDelete() {
        // given
        Long patientId = 7L;
        Patient patient = Patient.builder()
                .id(patientId)
                .hospital(Hospital.builder().id(1L).build())
                .name("삭제대상")
                .build();

        given(patientRepository.findById(patientId)).willReturn(Optional.of(patient));
        willDoNothing().given(patientRepository).delete(patient);

        // when/then
        patientService.delete(patientId);
        verify(patientRepository, times(1)).delete(patient);
    }
}