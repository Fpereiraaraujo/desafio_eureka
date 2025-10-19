package com.fernandopereira.cooperfilme.adapter.out.mapper;

import com.fernandopereira.cooperfilme.adapter.out.entity.ActorEntity;
import com.fernandopereira.domain.user.Actor;

public class ActorMapper {
    public static Actor toDomain(ActorEntity e) {
        if (e == null) return null;
        return Actor.builder()
                .id(e.getId())
                .name(e.getName())
                .email(e.getEmail())
                .passwordHash(e.getPasswordHash())
                .roles(e.getRoles())
                .build();
    }

    public static ActorEntity toEntity(Actor d) {
        if (d == null) return null;
        ActorEntity e = new ActorEntity();
        e.setId(d.getId());
        e.setName(d.getName());
        e.setEmail(d.getEmail());
        e.setPasswordHash(d.getPasswordHash());
        e.setRoles(d.getRoles());
        return e;
    }
}