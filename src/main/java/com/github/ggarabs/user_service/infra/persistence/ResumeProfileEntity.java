package com.github.ggarabs.user_service.infra.persistence;

import java.time.Instant;
import java.util.UUID;

import com.github.ggarabs.user_service.domain.model.Resume;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resume_profiles")
@Getter
@NoArgsConstructor
public class ResumeProfileEntity {
    @Id
    private UUID userId;

    @Convert(converter = ResumeJsonConverter.class)
    @Column(name = "profile_json", nullable = false, columnDefinition = "text")
    private Resume resume;

    @Column(nullable = false)
    private Instant updatedAt;

    public ResumeProfileEntity(UUID userId, Resume resume) {
        this.userId = userId;
        this.resume = resume;
        this.updatedAt = Instant.now();
    }
}
