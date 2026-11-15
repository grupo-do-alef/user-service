package com.github.ggarabs.user_service.port.out;

import java.util.Optional;

import com.github.ggarabs.user_service.domain.model.User;

public interface UserRepositoryPort {
    User save(User user);

    Optional<User> findByEmail(String email);
}
