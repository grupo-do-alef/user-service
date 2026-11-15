package com.github.ggarabs.user_service.infra.persistence;

import java.util.Set;
import java.util.UUID;

import com.github.ggarabs.user_service.domain.model.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users_tb")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String phone;
    private String email;
    private String password;
    private String birthday;
    private Double latitude;
    private Double longitude;
    private Double preferredRatio;

    private Set<Role> roles;

    @PrePersist
    private void setDefaultRole() {
        if (roles == null || roles.isEmpty()) {
            roles = Set.of(Role.WORKER);
        }
    }

}
