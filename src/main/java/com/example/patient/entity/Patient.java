package com.example.patient.entity;

import com.example.patient.code.GenderCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospitalId", nullable = false)
    private Hospital hospital;

    @Column(length = 45, nullable = false)
    @Setter
    private String name;

    @Column(length = 13, nullable = false, unique = true)
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private GenderCode genderCode;

    @Setter
    private LocalDate birthDate;

    @Column(length = 20, nullable = false)
    @Setter
    private String mobilePhoneNumber;

    @OneToMany(mappedBy = "patient")
    @Builder.Default
    private List<Visit> visits = new ArrayList<>();
}
