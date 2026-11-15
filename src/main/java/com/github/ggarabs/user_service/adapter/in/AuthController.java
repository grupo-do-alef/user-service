package com.github.ggarabs.user_service.adapter.in;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.github.ggarabs.user_service.port.in.AuthenticateUserCommand;
import com.github.ggarabs.user_service.port.in.AuthenticateUserUseCase;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse authenticatedUser = authenticateUserUseCase
                .authenticate(new AuthenticateUserCommand(request.email(), request.password()));

        return ResponseEntity.ok(authenticatedUser);
    }

    @GetMapping("/admin-dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminDashboard() {
        return ResponseEntity.ok("Bem-vindo à área do Administrador!");
    }

    public record LoginRequest(String email, String password) {
    }

    public record LoginResponse(String accessToken, String email, String name, List<String> roles) {
    }
}