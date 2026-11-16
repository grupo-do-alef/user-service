package com.github.ggarabs.user_service.adapter.in;

import java.time.Instant;
import java.util.UUID;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;
import com.github.ggarabs.user_service.infra.persistence.ResumeProfileEntity;
import com.github.ggarabs.user_service.infra.persistence.ResumeProfileRepository;
import com.github.ggarabs.user_service.port.out.UserRepositoryPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users/me/profile")
public class ResumeProfileController {
    private final ResumeProfileRepository repository;
    private final ObjectMapper objectMapper;
    private final UserRepositoryPort users;

    public ResumeProfileController(ResumeProfileRepository repository, ObjectMapper objectMapper, UserRepositoryPort users) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.users = users;
    }

    @GetMapping
    public JsonNode getProfile(Authentication authentication) {
        return repository.findById(userId(authentication))
                .map(profile -> read(profile.getProfileJson()))
                .orElseGet(() -> objectMapper.createObjectNode());
    }

    @PutMapping
    public JsonNode saveProfile(@RequestBody ProfileRequest request, Authentication authentication) {
        if (request == null || request.education() == null || request.education().isEmpty()
                || request.skills() == null || request.skills().isEmpty()
                || request.experiences() == null || request.experiences().isEmpty()
                || request.skills().stream().anyMatch(skill -> skill == null || skill.isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Informe escolaridade, ao menos uma habilidade e uma experiência");
        }
        boolean invalidEducation = request.education().stream().anyMatch(item -> item == null
                || blank(item, "course") || blank(item, "level") || blank(item, "institution") || blank(item, "start")
                || (!item.path("ongoing").asBoolean() && blank(item, "end")));
        boolean invalidExperience = request.experiences().stream().anyMatch(item -> item == null
                || blank(item, "role") || blank(item, "company") || blank(item, "start")
                || blank(item, "description") || (!item.path("current").asBoolean() && blank(item, "end")));
        if (invalidEducation || invalidExperience) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Preencha os campos obrigatórios do perfil");
        }
        ObjectNode json = objectMapper.createObjectNode();
        json.set("education", objectMapper.valueToTree(request.education()));
        json.set("skills", objectMapper.valueToTree(request.skills()));
        json.set("experiences", objectMapper.valueToTree(request.experiences()));

        ResumeProfileEntity profile = repository.findById(userId(authentication)).orElseGet(() -> {
            ResumeProfileEntity entity = new ResumeProfileEntity();
            entity.setUserId(userId(authentication));
            return entity;
        });
        profile.setProfileJson(json.toString());
        profile.setUpdatedAt(Instant.now());
        repository.save(profile);
        return json;
    }

    private JsonNode read(String json) {
        try { return objectMapper.readTree(json); }
        catch (Exception exception) { throw new IllegalStateException("Perfil salvo inválido", exception); }
    }

    private boolean blank(JsonNode node, String field) {
        return node.path(field).asText().isBlank();
    }

    private UUID userId(Authentication authentication) {
        try { return users.findByEmail(authentication.getName()).orElseThrow().getId(); }
        catch (RuntimeException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token sem identificador de usuário");
        }
    }

    public record ProfileRequest(java.util.List<JsonNode> education,
            java.util.List<String> skills,
            java.util.List<JsonNode> experiences) { }
}
