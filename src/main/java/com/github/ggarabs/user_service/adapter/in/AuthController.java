package com.github.ggarabs.user_service.adapter.in;

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
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        String token = authenticateUserUseCase.authenticate(
                new AuthenticateUserCommand(request.email(), request.password()));
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @GetMapping("/admin-dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminDashboard() {
        return ResponseEntity.ok("Bem-vindo à área do Administrador!");
    }

    public record LoginRequest(String email, String password) {
    }

    public record TokenResponse(String accessToken) {
    }
}