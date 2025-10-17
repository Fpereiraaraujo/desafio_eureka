package com.fernandopereira.app.screenplay;

import com.fernandopereira.domain.user.Actor;
import com.fernandopereira.ports.out.ActorRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticateUserUseCase {
    private final ActorRepositoryPort repo;
    private final PasswordEncoder encoder;

    public AuthenticateUserUseCase(ActorRepositoryPort repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public Optional<Actor> authenticate(String email, String rawPassword) {
        return repo.findByEmail(email)
                .filter(actor -> encoder.matches(rawPassword, actor.getPasswordHash()));
    }
}