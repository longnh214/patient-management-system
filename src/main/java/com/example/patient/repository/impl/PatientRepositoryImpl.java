package com.example.patient.repository.impl;

import com.example.patient.dto.PatientDto;
import com.example.patient.entity.Patient;
import com.example.patient.entity.QHospital;
import com.example.patient.entity.QPatient;
import com.example.patient.repository.query.PatientRepositoryCustom;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PatientRepositoryImpl implements PatientRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PatientDto.Response> searchFromSearchCondition(PatientDto.SearchCondition condition, PageRequest pageRequest) {
        QPatient patient = QPatient.patient;
        QHospital hospital = QHospital.hospital;

        QueryResults<Patient> results = queryFactory.selectFrom(patient)
                .join(patient.hospital, hospital).fetchJoin()
                .where(
                        nameContains(condition.getName()),
                        registrationNumberEq(condition.getRegistrationNumber()),
                        birthDateEq(condition.getBirthDate())
                )
                .limit(pageRequest.getPageSize())
                .offset(pageRequest.getOffset())
                .fetchResults();

        List<PatientDto.Response> content = results.getResults().stream()
                .map(PatientDto.Response::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, results.getTotal());
    }

    private BooleanExpression nameContains(String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }
        return QPatient.patient.name.containsIgnoreCase(name);
    }

    private BooleanExpression registrationNumberEq(String regNum) {
        if (!StringUtils.hasText(regNum)) {
            return null;
        }
        return QPatient.patient.registrationNumber.eq(regNum);
    }

    private BooleanExpression birthDateEq(String birthDateStr) {
        if (!StringUtils.hasText(birthDateStr)) {
            return null;
        }
        LocalDate birthDate = LocalDate.parse(birthDateStr);
        return QPatient.patient.birthDate.eq(birthDate);
    }
}
