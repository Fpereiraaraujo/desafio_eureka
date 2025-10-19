package com.fernandopereira.cooperfilme.ports.out;

import com.fernandopereira.cooperfilme.domain.user.Actor;

import java.util.Optional;

public interface ActorRepositoryPort {
    Optional<Actor> findByEmail(String email);
    Actor save(Actor actor);
}