package com.fernandopereira.adapter.in;

import com.fernandopereira.app.screenplay.AuthenticateUserUseCase;
import com.fernandopereira.domain.user.Actor;
import com.fernandopereira.config.jwt.JwtUtil;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthenticateUserUseCase auth;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticateUserUseCase auth, JwtUtil jwtUtil) {
        this.auth = auth;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq req) {
        return auth.authenticate(req.getEmail(), req.getPassword())
                .map(actor -> ResponseEntity.ok(new LoginResp(jwtUtil.generateToken(actor.getEmail()))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Data
    static class LoginReq {
        private String email;
        private String password;
    }

    @Data
    static class LoginResp {
        private final String token;
    }
}