package com.fernandopereira.cooperfilme.app.screenplay;

import com.fernandopereira.cooperfilme.domain.user.AccessRole;
import com.fernandopereira.cooperfilme.domain.user.Actor;
import com.fernandopereira.cooperfilme.ports.out.ActorRepositoryPort;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SeedUsersUseCase implements CommandLineRunner {

    private final ActorRepositoryPort repo;
    private final PasswordEncoder encoder;

    public SeedUsersUseCase(ActorRepositoryPort repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        // Idempotent se já existir
        if (repo.findByEmail("helena.motta@cooper.io").isPresent()) return;

        Actor analyst = Actor.builder()
                .name("Helena Motta")
                .email("helena.motta@cooper.io")
                .passwordHash(encoder.encode("Analyst#2025"))
                .roles(Set.of(AccessRole.CONTENT_ANALYST))
                .build();

        Actor reviser = Actor.builder()
                .name("Igor Sena")
                .email("igor.sena@cooper.io")
                .passwordHash(encoder.encode("Reviser#2025"))
                .roles(Set.of(AccessRole.QUALITY_REVISER))
                .build();

        Actor a1 = Actor.builder()
                .name("Lara Torres")
                .email("lara.torres@cooper.io")
                .passwordHash(encoder.encode("Approver1#2025"))
                .roles(Set.of(AccessRole.BOARD_APPROVER))
                .build();

        Actor a2 = Actor.builder()
                .name("Thiago Brandao")
                .email("thiago.brandao@cooper.io")
                .passwordHash(encoder.encode("Approver2#2025"))
                .roles(Set.of(AccessRole.BOARD_APPROVER))
                .build();

        Actor a3 = Actor.builder()
                .name("Emanuel Lima")
                .email("emanuel.lima@cooper.io")
                .passwordHash(encoder.encode("Approver3#2025"))
                .roles(Set.of(AccessRole.BOARD_APPROVER))
                .build();

        repo.save(analyst);
        repo.save(reviser);
        repo.save(a1);
        repo.save(a2);
        repo.save(a3);

        System.out.println("Seeded default users (emails & passwords printed in README).");
    }
}