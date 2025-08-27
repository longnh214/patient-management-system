package com.example.patient.repository.query;

import com.example.patient.dto.PatientDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface PatientRepositoryCustom {
    Page<PatientDto.Response> searchFromSearchCondition(PatientDto.SearchCondition searchCondition, PageRequest pageRequest);
}
