package com.github.ggarabs.user_service.application.usecase;

import java.util.UUID;
import org.springframework.stereotype.Service;

import com.github.ggarabs.user_service.infra.rest.dto.UserProfileResponse;
import com.github.ggarabs.user_service.port.in.GetUserProfileUseCase;
import com.github.ggarabs.user_service.port.out.UserRepositoryPort;

@Service
public class GetUserProfileUseCaseImpl implements GetUserProfileUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public GetUserProfileUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public UserProfileResponse getById(UUID id) {
        return userRepositoryPort.findById(id)
                .map(user -> new UserProfileResponse(
                        user.getId(),
                        user.getName(),
                        user.getPhone(),
                        user.getEmail(),
                        user.getBirthday(),
                        user.getLatitude(),
                        user.getLongitude(),
                        user.getPreferredRatio()))
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
    }
}