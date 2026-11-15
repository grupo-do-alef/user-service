package com.github.ggarabs.user_service.port.in;

public record AuthenticateUserCommand(String email, String password) {
}
