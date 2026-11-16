package com.github.ggarabs.user_service.adapter.in;

import java.util.UUID;

import com.github.ggarabs.user_service.domain.model.Resume;
import com.github.ggarabs.user_service.infra.persistence.ResumeProfileEntity;
import com.github.ggarabs.user_service.infra.persistence.ResumeProfileRepository;
import com.github.ggarabs.user_service.port.out.UserRepositoryPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users/me/profile")
public class ResumeProfileController {

    private final ResumeProfileRepository profiles;
    private final UserRepositoryPort users;

    public ResumeProfileController(ResumeProfileRepository profiles, UserRepositoryPort users) {
        this.profiles = profiles;
        this.users = users;
    }

    @GetMapping
    public Resume getProfile(Authentication authentication) {
        return profiles.findById(userId(authentication))
                .map(ResumeProfileEntity::getResume)
                .orElseGet(Resume::empty);
    }

    @PutMapping
    public ResponseEntity<?> saveProfile(@RequestBody Resume resume, Authentication authentication) {
        if (!resume.hasRequiredFields()) {
            var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                    "Preencha os campos obrigatórios da escolaridade e da experiência e inclua ao menos uma habilidade.");
            return ResponseEntity.badRequest().body(problem);
        }

        var profile = profiles.save(new ResumeProfileEntity(userId(authentication), resume));
        return ResponseEntity.ok(profile.getResume());
    }

    private UUID userId(Authentication authentication) {
        return users.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário da sessão não encontrado"))
                .getId();
    }
}
