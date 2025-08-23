package com.example.patient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Code {
    @EmbeddedId
    private CodePK id;

    @Column(length = 100, nullable = false)
    private String codeName;

    @Embeddable
    static class CodePK implements Serializable {
        @Column(length = 30)
        private String codeGroup;
        @Column(length = 30)
        private String code;
    }
}
