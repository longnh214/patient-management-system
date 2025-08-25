package com.example.patient.repository;

import com.example.patient.entity.Patient;
import com.example.patient.repository.query.PatientQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, PatientQueryRepository {
    long countByHospitalId(Long hospitalId);
}