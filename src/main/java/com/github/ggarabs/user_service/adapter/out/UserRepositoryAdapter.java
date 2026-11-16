package com.github.ggarabs.user_service.adapter.out;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.github.ggarabs.user_service.domain.model.User;
import com.github.ggarabs.user_service.infra.persistence.SpringDataUserRepository;
import com.github.ggarabs.user_service.infra.persistence.UserEntity;
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
    public Optional<User> findById(UUID id) {
        return springDataUserRepository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataUserRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = new UserEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles());

        UserEntity savedEntity = springDataUserRepository.save(entity);

        return new User(
                savedEntity.getId(),
                savedEntity.getName(),
                savedEntity.getEmail(),
                savedEntity.getPassword(),
                savedEntity.getRoles());
    }
}