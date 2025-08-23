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
    @Column(length = 10)
    private String codeGroup;
    @Column(length = 10, nullable = false)
    private String codeGroupName;
    @Column(length = 10)
    private String description;
}