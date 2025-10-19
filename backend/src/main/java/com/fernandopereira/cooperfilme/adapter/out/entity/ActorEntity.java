package com.fernandopereira.cooperfilme.adapter.out.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "actors")
public class ActorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "actor_roles", joinColumns = @JoinColumn(name = "actor_id"))
    private Set<com.fernandopereira.domain.user.AccessRole> roles;
}
