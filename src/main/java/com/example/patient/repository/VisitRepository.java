package com.example.patient.repository;

import com.example.patient.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    // TODO : querydsl로 조인 후 조회 변경 예정
    List<Visit> findAllByPatientId(Long patientId);
}