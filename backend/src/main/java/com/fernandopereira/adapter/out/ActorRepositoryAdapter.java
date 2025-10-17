package com.fernandopereira.adapter.out;

import com.fernandopereira.adapter.out.mapper.ActorMapper;
import com.fernandopereira.domain.user.Actor;
import com.fernandopereira.ports.out.ActorRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ActorRepositoryAdapter implements ActorRepositoryPort {

    private final SpringActorJpaRepository jpa;

    public ActorRepositoryAdapter(SpringActorJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Actor> findByEmail(String email) {
        return jpa.findByEmail(email).map(ActorMapper::toDomain);
    }

    @Override
    public Actor save(Actor actor) {
        return ActorMapper.toDomain(jpa.save(ActorMapper.toEntity(actor)));
    }
}