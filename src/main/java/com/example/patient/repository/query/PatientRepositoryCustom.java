package com.example.patient.repository.query;

import com.example.patient.dto.PatientDto;

import java.util.List;

public interface PatientRepositoryCustom {
    List<PatientDto.Response> searchFromSearchCondition(PatientDto.SearchCondition searchCondition);
}
