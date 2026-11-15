package com.github.ggarabs.user_service.adapter.out;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.github.ggarabs.user_service.domain.model.User;
import com.github.ggarabs.user_service.infra.persistence.SpringDataUserRepository;
import com.github.ggarabs.user_service.port.out.UserRepositoryPort;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository springDataUserRepository;
    private final UserMapper userMapper;

    public UserRepositoryAdapter(SpringDataUserRepository springDataUserRepository, UserMapper userMapper) {
        this.springDataUserRepository = springDataUserRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataUserRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }
}