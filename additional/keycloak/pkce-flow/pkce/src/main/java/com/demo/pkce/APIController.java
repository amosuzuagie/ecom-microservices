package com.demo.pkce;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIController {

    @GetMapping("/api/home")
    public String home(@AuthenticationPrincipal Jwt jwt) {
        return "Welcome Home, " + jwt.getClaim("name");
    }
}
