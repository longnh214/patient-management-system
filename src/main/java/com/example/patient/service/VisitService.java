package com.example.patient.service;

import com.example.patient.code.VisitStatusCode;
import com.example.patient.dto.VisitDto;
import com.example.patient.entity.Hospital;
import com.example.patient.entity.Patient;
import com.example.patient.entity.Visit;
import com.example.patient.repository.HospitalRepository;
import com.example.patient.repository.PatientRepository;
import com.example.patient.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final HospitalRepository hospitalRepository;
    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public List<VisitDto.Response> getVisits(){
        List<Visit> visits = visitRepository.findAll();
        List<VisitDto.Response> visitDtos = visits.stream()
                .map(VisitDto.Response::toDto)
                .collect(Collectors.toList());

        return visitDtos;
    }

    @Transactional(readOnly = true)
    public List<VisitDto.Response> getVisits(Long patientId){
        List<Visit> visits = visitRepository.findAllByPatientId(patientId);
        List<VisitDto.Response> visitDtos = visits.stream()
                .map(VisitDto.Response::toDto)
                .collect(Collectors.toList());

        return visitDtos;
    }

    @Transactional
    public VisitDto.Response create(VisitDto.Request dto) {
        Hospital hospital = hospitalRepository.findById(dto.getHospitalId())
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        Visit visit = Visit.builder()
                .hospital(hospital)
                .patient(patient)
                .receptionDateTime(dto.getReceptionDateTime())
                .visitStatusCode(VisitStatusCode.valueOf(dto.getVisitStatusCode()))
                .build();

        return VisitDto.Response.toDto(visitRepository.save(visit));
    }

    @Transactional(readOnly = true)
    public VisitDto.Response read(Long patientVisitId) {
        Visit visit = visitRepository.findById(patientVisitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found"));
        return VisitDto.Response.toDto(visit);
    }

    @Transactional
    public VisitDto.Response update(VisitDto.Update dto) {
        Visit visit = visitRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Visit not found"));
        visit.setVisitStatusCode(VisitStatusCode.valueOf(dto.getVisitStatusCode()));
        visit.setReceptionDateTime(dto.getReceptionDateTime());
        return VisitDto.Response.toDto(visit);
    }

    @Transactional
    public void delete(Long patientVisitId) {
        Visit visit = visitRepository.findById(patientVisitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found"));
        visitRepository.delete(visit);
    }
}