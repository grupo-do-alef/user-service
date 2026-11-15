package com.github.ggarabs.user_service.adapter.in;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ggarabs.user_service.infra.rest.dto.UserResponse;
import com.github.ggarabs.user_service.port.in.RegisterUserCommand;
import com.github.ggarabs.user_service.port.in.RegisterUserUseCase;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    private final RegisterUserUseCase registerUserUseCase;

    public RegisterController(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponse> register(@RequestBody RegisterUserCommand command) {
        UserResponse response = registerUserUseCase.register(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
