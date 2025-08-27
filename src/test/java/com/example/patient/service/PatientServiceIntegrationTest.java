package com.example.patient.service;

import com.example.patient.code.GenderCode;
import com.example.patient.constant.PageInfo;
import com.example.patient.dto.PatientDto;
import com.example.patient.entity.Hospital;
import com.example.patient.entity.Patient;
import com.example.patient.repository.HospitalRepository;
import com.example.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PatientServiceIntegrationTest {

    @Autowired
    private PatientService patientService;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private HospitalRepository hospitalRepository;

    private Hospital hospital;

    @BeforeEach
    void setup() {
        hospital = Hospital.builder()
                .hospitalName("테스트병원")
                .institutionNumber("Y0001")
                .hospitalDirectorName("홍길동")
                .build();
        hospital = hospitalRepository.save(hospital);
    }

    @Test
    void testCreateAndReadPatient_success() {
        String regNum = patientService.generateRegistrationNumber(hospital.getId());
        PatientDto.Request request = PatientDto.Request.builder()
                .hospitalId(hospital.getId())
                .name("테스터환자")
                .registrationNumber(regNum)
                .genderCode(GenderCode.F.name())
                .birthDate("1998-04-23")
                .mobilePhoneNumber("01099998888")
                .build();

        PatientDto.Response response = patientService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.getHospitalId()).isEqualTo(hospital.getId());
        assertThat(response.getName()).isEqualTo("테스터환자");
        assertThat(response.getRegistrationNumber()).isEqualTo(regNum);
        assertThat(response.getGenderCode()).isEqualTo("F");
        assertThat(response.getBirthDate()).isEqualTo("1998-04-23");
        assertThat(response.getMobilePhoneNumber()).isEqualTo("01099998888");

        Patient patient = patientRepository.findById(response.getId()).orElse(null);
        assertThat(patient).isNotNull();
        assertThat(patient.getHospital().getId()).isEqualTo(hospital.getId());
    }

    @Test
    void testUpdatePatient_success() {
        Patient patient = Patient.builder()
                .hospital(hospital)
                .name("업데이트전")
                .registrationNumber(patientService.generateRegistrationNumber(hospital.getId()))
                .genderCode(GenderCode.M)
                .birthDate(LocalDate.of(1976, 3, 5))
                .mobilePhoneNumber("01012341234")
                .build();
        patient = patientRepository.save(patient);

        PatientDto.Update updateDto = PatientDto.Update.builder()
                .id(patient.getId())
                .name("업데이트후")
                .genderCode(GenderCode.F.name())
                .birthDate("2001-09-09")
                .mobilePhoneNumber("01055558888")
                .build();

        PatientDto.Response updated = patientService.update(updateDto);

        assertThat(updated.getName()).isEqualTo("업데이트후");
        assertThat(updated.getGenderCode()).isEqualTo("F");
        assertThat(updated.getBirthDate()).isEqualTo("2001-09-09");
        assertThat(updated.getMobilePhoneNumber()).isEqualTo("01055558888");
    }

    @Test
    void testDeletePatient_success() {
        Patient patient = Patient.builder()
                .hospital(hospital)
                .name("삭제테스트")
                .registrationNumber(patientService.generateRegistrationNumber(hospital.getId()))
                .genderCode(GenderCode.M)
                .birthDate(LocalDate.of(1980, 1, 1))
                .mobilePhoneNumber("01011112222")
                .build();
        patient = patientRepository.save(patient);

        patientService.delete(patient.getId());

        assertThat(patientRepository.findById(patient.getId())).isEmpty();
    }

    @Test
    void testGetPatients_success() {
        // given
        for(int i=0;i<100;i++) {patientRepository.save(Patient.builder()
                    .hospital(hospital)
                    .name("김철수")
                    .registrationNumber(patientService.generateRegistrationNumber(hospital.getId()))
                    .genderCode(GenderCode.M)
                    .birthDate(LocalDate.of(1992, 2, 2))
                    .mobilePhoneNumber("01088887777")
                    .build());
        }

        PatientDto.SearchCondition searchCondition = PatientDto.SearchCondition.builder()
                .name("김철수")
                .build();

        PageInfo pageInfo = new PageInfo(3, 10);

        Page<PatientDto.Response> results = patientService.getPatients(searchCondition, pageInfo);

        assertThat(results).isNotNull();
        assertThat(results.getContent().get(0).getRegistrationNumber()).isEqualTo(String.format("%04d", hospital.getId()) + String.format("%05d", 21));
        assertThat(results.getContent().size()).isEqualTo(10);
        assertThat(results.getContent().stream().anyMatch(dto -> dto.getName().equals("홍길동"))).isFalse();
        assertThat(results.getContent().stream().anyMatch(dto -> dto.getName().equals("김철수"))).isTrue();
    }

    @Test
    void testGetPatients_failure_noMatch() {
        patientRepository.save(Patient.builder()
                .hospital(hospital)
                .name("홍길동")
                .registrationNumber(patientService.generateRegistrationNumber(hospital.getId()))
                .genderCode(GenderCode.F)
                .birthDate(LocalDate.of(1991, 1, 1))
                .mobilePhoneNumber("01012349876")
                .build());

        PatientDto.SearchCondition searchCondition = PatientDto.SearchCondition.builder()
                .name("없는이름")
                .build();

        PageInfo pageInfo = new PageInfo(1, 10);

        Page<PatientDto.Response> results = patientService.getPatients(searchCondition, pageInfo);

        assertThat(results).isNotNull();
        assertThat(results.isEmpty()).isTrue();
    }

    @Test
    void testGetPatients_success_filterByBirthDateAndRegistrationNumber() {
        patientRepository.save(Patient.builder()
                .hospital(hospital)
                .name("박지민")
                .registrationNumber("000500028")
                .genderCode(GenderCode.M)
                .birthDate(LocalDate.of(2000, 5, 15))
                .mobilePhoneNumber("01099998888")
                .build());

        PatientDto.SearchCondition searchCondition = PatientDto.SearchCondition.builder()
                .birthDate("2000-05-15")
                .registrationNumber("000500028")
                .build();

        PageInfo pageInfo = new PageInfo(1, 10);

        Page<PatientDto.Response> results = patientService.getPatients(searchCondition, pageInfo);

        assertThat(results).isNotNull();
        assertThat(results.getContent().size()).isGreaterThanOrEqualTo(1);
        assertThat(results.getContent().get(0).getName()).isEqualTo("박지민");
    }

    @Test
    void testGetPatients_failure_emptySearchCondition() {
        PatientDto.SearchCondition searchCondition = PatientDto.SearchCondition.builder()
                .build();

        PageInfo pageInfo = new PageInfo(1, 10);

        Page<PatientDto.Response> results = patientService.getPatients(searchCondition, pageInfo);

        assertThat(results).isNotNull();
    }
}