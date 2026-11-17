package com.github.ggarabs.user_service.adapter.in;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.ggarabs.user_service.port.out.UserRepositoryPort;

import com.github.ggarabs.user_service.port.in.AuthenticateUserCommand;
import com.github.ggarabs.user_service.port.in.AuthenticateUserUseCase;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase, UserRepositoryPort users) {
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse authenticatedUser = authenticateUserUseCase
                .authenticate(new AuthenticateUserCommand(request.email(), request.password()));

        return ResponseEntity.ok(authenticatedUser);
    }

    public record LoginRequest(String email, String password) {
    }

    public record LoginResponse(String accessToken, String email, String name, List<String> roles) {
    }

    public record SessionUser(String id, String email, String role, String message) {
    }
}
