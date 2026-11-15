package com.github.ggarabs.user_service.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.ggarabs.user_service.application.exception.EmailAlreadyExistsException;
import com.github.ggarabs.user_service.domain.model.User;
import com.github.ggarabs.user_service.port.in.RegisterUserCommand;
import com.github.ggarabs.user_service.port.out.PasswordEncoderPort;
import com.github.ggarabs.user_service.port.out.UserRepositoryPort;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private RegisterUserUseCaseImpl registerUserUseCase;

    private RegisterUserCommand command;

    @BeforeEach
    void setUp() {
        command = new RegisterUserCommand("Gustavo", "gustavo@email.com", "123456");
    }

    @Test
    void shouldRegisterUserSuccessfullyWhenEmailIsUnique() {
        when(userRepositoryPort.findByEmail(command.email())).thenReturn(Optional.empty());
        when(passwordEncoderPort.encode(command.password())).thenReturn("encoded_password");

        User savedUser = new User(UUID.randomUUID(), command.name(), command.email(), "encoded_password");
        when(userRepositoryPort.save(any(User.class))).thenReturn(savedUser);

        var response = registerUserUseCase.register(command);

        assertNotNull(response);
        assertEquals(command.email(), response.email());
        assertEquals(command.name(), response.name());
        verify(userRepositoryPort).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        User existingUser = new User(UUID.randomUUID(), "Outro", command.email(), "hash");
        when(userRepositoryPort.findByEmail(command.email())).thenReturn(Optional.of(existingUser));

        assertThrows(EmailAlreadyExistsException.class, () -> {
            registerUserUseCase.register(command);
        });

        verify(userRepositoryPort, never()).save(any(User.class));
    }
}