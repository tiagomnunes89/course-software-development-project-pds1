package com.tiagomnunes.aulapds1.resources;

import com.tiagomnunes.aulapds1.dto.CredentialsDTO;
import com.tiagomnunes.aulapds1.dto.TokenDTO;
import com.tiagomnunes.aulapds1.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

    @Autowired
    private AuthService service;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody CredentialsDTO credentialsDTO) {
        TokenDTO tokenDTO = service.authenticate(credentialsDTO);
        return ResponseEntity.ok().body(tokenDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDTO> refresh() {
        TokenDTO tokenDTO = service.refreshToken();
        return ResponseEntity.ok().body(tokenDTO);
    }
}