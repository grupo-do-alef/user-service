package com.github.ggarabs.user_service.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ggarabs.user_service.application.usecase.AuthenticateUserUseCaseImpl;
import com.github.ggarabs.user_service.port.in.AuthenticateUserUseCase;
import com.github.ggarabs.user_service.port.out.PasswordEncoderPort;
import com.github.ggarabs.user_service.port.out.TokenProviderPort;
import com.github.ggarabs.user_service.port.out.UserRepositoryPort;

@Configuration
public class BeanConfig {

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(
            UserRepositoryPort userRepositoryPort,
            PasswordEncoderPort passwordEncoderPort,
            TokenProviderPort tokenProviderPort) {
        return new AuthenticateUserUseCaseImpl(userRepositoryPort, passwordEncoderPort, tokenProviderPort);
    }
}