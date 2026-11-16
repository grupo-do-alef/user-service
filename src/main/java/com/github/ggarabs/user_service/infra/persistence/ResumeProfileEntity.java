package com.github.ggarabs.user_service.infra.persistence;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resume_profiles")
@Getter
@Setter
@NoArgsConstructor
public class ResumeProfileEntity {
    @Id
    private UUID userId;

    @Column(nullable = false, columnDefinition = "text")
    private String profileJson;

    @Column(nullable = false)
    private Instant updatedAt;
}
