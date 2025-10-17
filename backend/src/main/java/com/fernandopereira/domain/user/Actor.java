package com.fernandopereira.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Actor {
    private Long id;
    private String name;
    private String email;
    private String passwordHash;
    private Set<AccessRole> roles;
}