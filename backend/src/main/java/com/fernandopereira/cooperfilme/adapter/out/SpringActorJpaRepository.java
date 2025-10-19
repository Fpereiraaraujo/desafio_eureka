package com.fernandopereira.cooperfilme.adapter.out;

import com.fernandopereira.cooperfilme.adapter.out.entity.ActorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringActorJpaRepository extends JpaRepository<ActorEntity, Long> {
    Optional<ActorEntity> findByEmail(String email);
}
