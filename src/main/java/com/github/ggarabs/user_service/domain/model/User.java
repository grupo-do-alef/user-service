package com.github.ggarabs.user_service.domain.model;

import java.util.Set;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String birthday;
    private Double latitude;
    private Double longitude;
    private Double preferredRatio;
    private Set<Role> roles;

    public User(UUID id, String name, String phone, String email, String password,
            String birthday, Double latitude, Double longitude, Double preferredRatio, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.latitude = latitude;
        this.longitude = longitude;
        this.preferredRatio = preferredRatio;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getPreferredRatio() {
        return preferredRatio;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}