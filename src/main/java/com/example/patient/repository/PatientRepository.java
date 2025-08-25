package com.example.patient.repository;

import com.example.patient.entity.Patient;
import com.example.patient.repository.query.PatientRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, PatientRepositoryCustom {
    long countByHospitalId(Long hospitalId);
}