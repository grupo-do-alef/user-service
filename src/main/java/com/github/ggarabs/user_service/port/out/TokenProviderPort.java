package com.github.ggarabs.user_service.port.out;

import com.github.ggarabs.user_service.domain.model.User;

public interface TokenProviderPort {
    String generateToken(User user);

    boolean validateToken(String token);

    String getEmailFromToken(String token);

    String getRolesFromToken(String token);
}
