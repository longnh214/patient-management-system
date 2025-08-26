package com.example.patient.controller;

import com.example.patient.dto.PatientDto;
import com.example.patient.dto.VisitDto;
import com.example.patient.service.PatientService;
import com.example.patient.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientService patientService;
    private final VisitService visitService;

    @PostMapping
    public ResponseEntity<PatientDto.Response> create(@RequestBody PatientDto.Request request) {
        String newRegistrationNumber = patientService.generateRegistrationNumber(request.getHospitalId());
        PatientDto.Request requestDto = PatientDto.Request.builder()
                .hospitalId(request.getHospitalId())
                .name(request.getName())
                .registrationNumber(newRegistrationNumber)
                .genderCode(request.getGenderCode())
                .birthDate(request.getBirthDate())
                .mobilePhoneNumber(request.getMobilePhoneNumber())
                .build();

        PatientDto.Response responseDto = patientService.create(requestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.getId())
                .toUri();

        return ResponseEntity.created(location)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDto.Response> update(@PathVariable Long id, @RequestBody PatientDto.Update updateDto) {
        PatientDto.Update updateObj = PatientDto.Update.builder()
                .id(id)
                .name(updateDto.getName())
                .genderCode(updateDto.getGenderCode())
                .birthDate(updateDto.getBirthDate())
                .mobilePhoneNumber(updateDto.getMobilePhoneNumber())
                .build();

        PatientDto.Response responseDto = patientService.update(updateObj);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto.PatientDetailResponse> read(@PathVariable Long id) {
        PatientDto.Response patientDto = patientService.read(id);

        List<VisitDto.Response> visits = visitService.getVisits(id);

        PatientDto.PatientDetailResponse detail = PatientDto.PatientDetailResponse.builder()
                .patient(patientDto)
                .visits(visits)
                .build();

        return ResponseEntity.ok(detail);
    }

    @GetMapping
    public ResponseEntity<List<PatientDto.Response>> list(PatientDto.SearchCondition searchCondition) {
        List<PatientDto.Response> patients = patientService.getPatients(searchCondition);
        return ResponseEntity.ok(patients);
    }
}
