package com.github.ggarabs.user_service.adapter.in;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ggarabs.user_service.infra.rest.dto.UserProfileResponse;
import com.github.ggarabs.user_service.port.in.GetUserProfileUseCase;

@RestController
@RequestMapping("/users")
public class UserController {

    private final GetUserProfileUseCase getUserUseCase;

    public UserController(GetUserProfileUseCase getUserUseCase) {
        this.getUserUseCase = getUserUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(getUserUseCase.getById(id));
    }
}