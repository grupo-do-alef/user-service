package com.github.ggarabs.user_service.port.in;

public record RegisterUserCommand(
        String name,
        String email,
        String password) {
}
