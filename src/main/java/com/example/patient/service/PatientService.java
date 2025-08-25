package com.example.patient.service;

import com.example.patient.code.GenderCode;
import com.example.patient.dto.PatientDto;
import com.example.patient.entity.Hospital;
import com.example.patient.entity.Patient;
import com.example.patient.repository.HospitalRepository;
import com.example.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final HospitalRepository hospitalRepository;

    @Transactional(readOnly = true)
    public List<PatientDto.Response> getPatients(PatientDto.SearchCondition searchCondition){
        List<PatientDto.Response> patients = patientRepository.searchFromSearchCondition(searchCondition);

        return patients;
    }

    @Transactional
    public PatientDto.Response create(PatientDto.Request dto) {
        Hospital hospital = hospitalRepository.findById(dto.getHospitalId())
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));

        Patient patient = Patient.builder()
                .hospital(hospital)
                .name(dto.getName())
                .registrationNumber(dto.getRegistrationNumber())
                .genderCode(GenderCode.valueOf(dto.getGenderCode()))
                .birthDate(dto.getBirthDate() != null ? LocalDate.parse(dto.getBirthDate()) : null)
                .mobilePhoneNumber(dto.getMobilePhoneNumber())
                .build();

        return PatientDto.Response.toDto(patientRepository.save(patient));
    }

    @Transactional(readOnly = true)
    public PatientDto.Response read(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        return PatientDto.Response.toDto(patient);
    }

    @Transactional
    public PatientDto.Response update(PatientDto.Update dto) {
        Patient patient = patientRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        patient.setName(dto.getName());
        patient.setGenderCode(GenderCode.fromCode(dto.getGenderCode()));
        patient.setBirthDate(dto.getBirthDate() != null ? LocalDate.parse(dto.getBirthDate()) : null);
        patient.setMobilePhoneNumber(dto.getMobilePhoneNumber());

        return PatientDto.Response.toDto(patient);
    }

    @Transactional
    public void delete(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        patientRepository.delete(patient);
    }

    public String generateRegistrationNumber(Long hospitalId) {
        String prefix = String.format("%04d", hospitalId); // 병원ID로 prefix 예시
        long count = patientRepository.countByHospitalId(hospitalId) + 1;
        return prefix + String.format("%05d", count); // 병원ID + 일련번호 예시: "0005xxxxx"
    }
}