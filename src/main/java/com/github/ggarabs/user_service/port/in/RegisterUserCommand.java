package com.github.ggarabs.user_service.port.in;

import java.util.List;

public record RegisterUserCommand(
                String name,
                String email,
                String password,
                List<String> roles) {
}
