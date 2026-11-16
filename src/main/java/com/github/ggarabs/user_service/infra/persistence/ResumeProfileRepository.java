package com.github.ggarabs.user_service.infra.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeProfileRepository extends JpaRepository<ResumeProfileEntity, UUID> {
}
