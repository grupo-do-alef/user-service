package com.github.ggarabs.user_service.port.out;

public interface PasswordEncoderPort {
    boolean matches(String rawPassword, String encodedPassword);

    String encode(String rawPassword);
}
