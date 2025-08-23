package com.example.patient.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String hospitalName;

    @Column(length = 20, nullable = false)
    private String institutionNumber;

    @Column(length = 10, nullable = false)
    private String hospitalDirectorName;
}
