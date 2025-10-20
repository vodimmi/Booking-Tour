package com.example.auth.controller;

import com.example.auth.security.KeyProvider;
import com.nimbusds.jose.jwk.JWKSet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "JWKS", description = "JSON Web Key Set endpoint")
public class JwksController {

    private final KeyProvider keyProvider;

    public JwksController(KeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    @GetMapping("/jwks.json")
    @Operation(summary = "Get JSON Web Key Set", description = "Retrieve public keys for JWT verification")
    @ApiResponse(responseCode = "200", description = "JWKS retrieved successfully")
    public ResponseEntity<Map<String, Object>> getJwks() {
        JWKSet jwkSet = new JWKSet(keyProvider.getRSAKey().toPublicJWK());
        return ResponseEntity.ok(jwkSet.toJSONObject());
    }
}