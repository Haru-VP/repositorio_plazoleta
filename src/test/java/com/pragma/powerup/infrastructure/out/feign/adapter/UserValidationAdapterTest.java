package com.pragma.powerup.infrastructure.out.feign.adapter;

import com.pragma.powerup.infrastructure.out.feign.client.IUserFeignClient;
import com.pragma.powerup.infrastructure.out.feign.dto.RoleResponseDto;
import com.pragma.powerup.infrastructure.out.feign.dto.UserResponseDto;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserValidationAdapterTest {

    @Mock
    private IUserFeignClient userFeignClient;

    @InjectMocks
    private UserValidationAdapter userValidationAdapter;

    private UserResponseDto usuarioPropietario;
    private UserResponseDto usuarioCliente;

    @BeforeEach
    void setUp() {
        RoleResponseDto rolPropietario = new RoleResponseDto(2L, "PROPIETARIO", "Rol de propietario");
        RoleResponseDto rolCliente = new RoleResponseDto(4L, "CLIENTE", "Rol de cliente");

        usuarioPropietario = new UserResponseDto();
        usuarioPropietario.setId(1L);
        usuarioPropietario.setRol(rolPropietario);

        usuarioCliente = new UserResponseDto();
        usuarioCliente.setId(2L);
        usuarioCliente.setRol(rolCliente);
    }

    @Test
    void esPropietario_usuarioEsPropietario_retornaTrue() {
        // Arrange
        when(userFeignClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);

        // Act
        boolean resultado = userValidationAdapter.esPropietario(1L);

        // Assert
        assertTrue(resultado);
        verify(userFeignClient).obtenerUsuarioPorId(1L);
    }

    @Test
    void esPropietario_usuarioTieneOtroRol_retornaFalse() {
        // Arrange
        when(userFeignClient.obtenerUsuarioPorId(2L)).thenReturn(usuarioCliente);

        // Act
        boolean resultado = userValidationAdapter.esPropietario(2L);

        // Assert
        assertFalse(resultado);
        verify(userFeignClient).obtenerUsuarioPorId(2L);
    }

    @Test
    void esPropietario_usuarioNoExiste_retornaFalse() {
        // Arrange
        Request request = Request.create(Request.HttpMethod.GET, "/api/v1/user/99", Collections.emptyMap(), null, StandardCharsets.UTF_8, new RequestTemplate());
        when(userFeignClient.obtenerUsuarioPorId(99L)).thenThrow(new FeignException.NotFound("Not found", request, null, null));

        // Act
        boolean resultado = userValidationAdapter.esPropietario(99L);

        // Assert
        assertFalse(resultado);
        verify(userFeignClient).obtenerUsuarioPorId(99L);
    }

    @Test
    void esPropietario_usuarioRetornaNull_retornaFalse() {
        // Arrange
        when(userFeignClient.obtenerUsuarioPorId(3L)).thenReturn(null);

        // Act
        boolean resultado = userValidationAdapter.esPropietario(3L);

        // Assert
        assertFalse(resultado);
        verify(userFeignClient).obtenerUsuarioPorId(3L);
    }
}
