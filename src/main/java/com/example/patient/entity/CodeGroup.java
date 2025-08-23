package com.example.patient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CodeGroup {
    @Id
    @Column(length = 30)
    private String codeGroup;
    @Column(length = 30, nullable = false)
    private String codeGroupName;
    @Column(length = 100)
    private String description;
}