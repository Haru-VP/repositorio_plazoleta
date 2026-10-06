package com.pragma.powerup.infrastructure.out.security.adapter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtAdapterTest {

    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private JwtAdapter jwtAdapter;

    @BeforeEach
    void setUp() {
        jwtAdapter = new JwtAdapter(SECRET);
    }

    private String generarTokenPrueba(Long id, String correo, String rol, long validezMs) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("rol", rol);

        Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + validezMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(correo)
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(key)
                .compact();
    }

    @Test
    void esTokenValido_conTokenCorrecto_retornaTrue() {
        String token = generarTokenPrueba(1L, "propietario@plazoleta.com", "PROPIETARIO", 3600000);
        assertTrue(jwtAdapter.esTokenValido(token));
    }

    @Test
    void esTokenValido_conTokenInvalido_retornaFalse() {
        assertFalse(jwtAdapter.esTokenValido("token.invalido.estructuralmente"));
    }

    @Test
    void esTokenValido_conTokenExpirado_retornaFalse() {
        String token = generarTokenPrueba(1L, "propietario@plazoleta.com", "PROPIETARIO", -3600000);
        assertFalse(jwtAdapter.esTokenValido(token));
    }

    @Test
    void extraerClaims_conTokenValido_retornaClaimsCorrectos() {
        String token = generarTokenPrueba(5L, "admin@plazoleta.com", "ADMINISTRADOR", 3600000);
        Claims claims = jwtAdapter.extraerClaims(token);

        assertNotNull(claims);
        assertEquals("admin@plazoleta.com", claims.getSubject());
        assertEquals("ADMINISTRADOR", claims.get("rol", String.class));
        assertEquals(5, claims.get("id", Number.class).intValue());
    }
}
