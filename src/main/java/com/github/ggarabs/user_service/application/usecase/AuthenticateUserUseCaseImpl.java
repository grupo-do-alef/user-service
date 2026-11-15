package com.github.ggarabs.user_service.application.usecase;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ggarabs.user_service.adapter.in.AuthController.LoginResponse;
import com.github.ggarabs.user_service.application.exception.InvalidCredentialsException;
import com.github.ggarabs.user_service.domain.model.User;
import com.github.ggarabs.user_service.port.in.AuthenticateUserCommand;
import com.github.ggarabs.user_service.port.in.AuthenticateUserUseCase;
import com.github.ggarabs.user_service.port.out.PasswordEncoderPort;
import com.github.ggarabs.user_service.port.out.TokenProviderPort;
import com.github.ggarabs.user_service.port.out.UserRepositoryPort;

public class AuthenticateUserUseCaseImpl implements AuthenticateUserUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticateUserUseCaseImpl.class);

    public AuthenticateUserUseCaseImpl(UserRepositoryPort userRepositoryPort,
            PasswordEncoderPort passwordEncoderPort,
            TokenProviderPort tokenProviderPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenProviderPort = tokenProviderPort;
    }

    @Override
    public LoginResponse authenticate(AuthenticateUserCommand command) {
        User user = userRepositoryPort.findByEmail(command.email())
                .orElseThrow(() -> {
                    logger.warn("Tentativa de login falhou para o e-mail: {}", command.email());
                    return new InvalidCredentialsException("Invalid credentials");
                });

        if (!passwordEncoderPort.matches(command.password(), user.getPassword())) {
            logger.warn("Tentativa de login falhou para o e-mail: {}", command.email());
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = tokenProviderPort.generateToken(user);

        List<String> roles = user.getRoles().stream()
                .map(role -> role.toString().toUpperCase())
                .collect(Collectors.toList());

        return new LoginResponse(token, user.getEmail(), user.getName(), roles);
    }
}
