package com.github.ggarabs.user_service.application.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.github.ggarabs.user_service.application.exception.EmailAlreadyExistsException;
import com.github.ggarabs.user_service.domain.model.User;
import com.github.ggarabs.user_service.infra.rest.dto.UserResponse;
import com.github.ggarabs.user_service.port.in.RegisterUserCommand;
import com.github.ggarabs.user_service.port.in.RegisterUserUseCase;
import com.github.ggarabs.user_service.port.out.PasswordEncoderPort;
import com.github.ggarabs.user_service.port.out.UserRepositoryPort;

@Service
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private static final Logger logger = LoggerFactory.getLogger(RegisterUserUseCaseImpl.class);

    public RegisterUserUseCaseImpl(UserRepositoryPort userRepositoryPort, PasswordEncoderPort passwordEncoderPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public UserResponse register(RegisterUserCommand command) {
        userRepositoryPort.findByEmail(command.email()).ifPresent(user -> {
            logger.warn("E-mail já cadastrado tentou registrar-se: {}", command.email());
            throw new EmailAlreadyExistsException("E-mail já cadastrado.");
        });

        String encodedPassword = passwordEncoderPort.encode(command.password());

        User newUser = new User(null, command.name(), command.email(), encodedPassword);
        User savedUser = userRepositoryPort.save(newUser);

        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }
}
