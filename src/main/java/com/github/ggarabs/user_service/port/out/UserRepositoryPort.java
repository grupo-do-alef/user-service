package com.github.ggarabs.user_service.port.out;

import java.util.Optional;

import com.github.ggarabs.user_service.domain.model.User;

public interface UserRepositoryPort {
    Optional<User> findByEmail(String email);
}
