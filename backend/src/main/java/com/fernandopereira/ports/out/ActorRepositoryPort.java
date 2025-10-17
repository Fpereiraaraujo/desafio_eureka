package com.fernandopereira.ports.out;

import com.fernandopereira.domain.user.Actor;

import java.util.Optional;

public interface ActorRepositoryPort {
    Optional<Actor> findByEmail(String email);
    Actor save(Actor actor);
}