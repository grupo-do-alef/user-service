package com.github.ggarabs.user_service.adapter.out;

import com.github.ggarabs.user_service.domain.model.User;
import com.github.ggarabs.user_service.infra.persistence.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null)
            return null;
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getBirthday(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getPreferredRatio(),
                entity.getRoles());
    }
}