package com.github.ggarabs.user_service.adapter.in;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.github.ggarabs.user_service.port.out.UserRepositoryPort;

import com.github.ggarabs.user_service.port.in.AuthenticateUserCommand;
import com.github.ggarabs.user_service.port.in.AuthenticateUserUseCase;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final UserRepositoryPort users;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase, UserRepositoryPort users) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.users = users;
    }

    @GetMapping("/me")
    public ResponseEntity<SessionUser> me(Authentication authentication) {
        var user = users.findByEmail(authentication.getName()).orElseThrow();
        String role = user.getRoles().stream().findFirst().map(Enum::name).orElse("WORKER");
        return ResponseEntity.ok(new SessionUser(user.getId().toString(), user.getEmail(), role, "ok"));
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
    public record SessionUser(String id, String email, String role, String message) { }
}
