package com.example.patient.entity;

import com.example.patient.code.VisitStatusCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospitalId", nullable = false)
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patientId", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    @Setter
    private LocalDateTime receptionDateTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    @Setter
    private VisitStatusCode visitStatusCode;
}

