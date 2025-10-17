package com.fernandopereira.adapter.out;

import com.fernandopereira.adapter.out.entity.ActorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringActorJpaRepository extends JpaRepository<ActorEntity, Long> {
    Optional<ActorEntity> findByEmail(String email);
}
