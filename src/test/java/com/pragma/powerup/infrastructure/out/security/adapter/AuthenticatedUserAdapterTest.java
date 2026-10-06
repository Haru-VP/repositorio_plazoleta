package com.pragma.powerup.infrastructure.out.security.adapter;

import com.pragma.powerup.infrastructure.out.security.model.AuthenticatedUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuthenticatedUserAdapterTest {

    private AuthenticatedUserAdapter authenticatedUserAdapter;

    @BeforeEach
    void setUp() {
        authenticatedUserAdapter = new AuthenticatedUserAdapter();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void obtenerIdUsuarioAutenticado_conUsuarioEnContexto_retornaIdCorrecto() {
        // Arrange
        AuthenticatedUser user = new AuthenticatedUser(5L, "propietario@plazoleta.com", "PROPIETARIO");
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        Long id = authenticatedUserAdapter.obtenerIdUsuarioAutenticado();

        // Assert
        assertEquals(5L, id);
    }

    @Test
    void obtenerIdUsuarioAutenticado_sinUsuarioEnContexto_retornaNull() {
        // Act
        Long id = authenticatedUserAdapter.obtenerIdUsuarioAutenticado();

        // Assert
        assertNull(id);
    }

    @Test
    void obtenerRolUsuarioAutenticado_conUsuarioEnContexto_retornaRolCorrecto() {
        // Arrange
        AuthenticatedUser user = new AuthenticatedUser(5L, "propietario@plazoleta.com", "PROPIETARIO");
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        String rol = authenticatedUserAdapter.obtenerRolUsuarioAutenticado();

        // Assert
        assertEquals("PROPIETARIO", rol);
    }

    @Test
    void obtenerRolUsuarioAutenticado_sinUsuarioEnContexto_retornaNull() {
        // Act
        String rol = authenticatedUserAdapter.obtenerRolUsuarioAutenticado();

        // Assert
        assertNull(rol);
    }
}
