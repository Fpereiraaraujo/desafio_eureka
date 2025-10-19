package com.fernandopereira.cooperfilme.security;

import com.fernandopereira.cooperfilme.domain.user.Actor;
import com.fernandopereira.cooperfilme.domain.user.AccessRole;
import com.fernandopereira.cooperfilme.ports.out.ActorRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ActorRepositoryPort actorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Actor actor = actorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Converte Set<AccessRole> em lista de SimpleGrantedAuthority
        List<SimpleGrantedAuthority> authorities = actor.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();

        return new User(actor.getEmail(), actor.getPasswordHash(), authorities);
    }


}
