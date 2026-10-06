package com.pragma.powerup.infrastructure.out.feign.adapter;

import com.pragma.powerup.infrastructure.out.feign.client.ITrazabilidadFeignClient;
import com.pragma.powerup.infrastructure.out.feign.dto.TrazabilidadFeignRequestDto;
import com.pragma.powerup.infrastructure.out.feign.dto.TrazabilidadFeignResponseDto;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrazabilidadAdapterTest {

    @Mock
    private ITrazabilidadFeignClient trazabilidadFeignClient;

    @InjectMocks
    private TrazabilidadAdapter trazabilidadAdapter;

    @Test
    @DisplayName("Debe registrar cambio de estado exitosamente en el microservicio de trazabilidad")
    void registrarCambioEstado_exitoso() {
        TrazabilidadFeignResponseDto responseDto = new TrazabilidadFeignResponseDto(
                "log123", 1L, 10L, "cliente@correo.com", LocalDateTime.now(),
                "PENDIENTE", "EN_PREPARACION", 20L, "empleado@correo.com"
        );
        when(trazabilidadFeignClient.registrarCambioEstado(any(TrazabilidadFeignRequestDto.class))).thenReturn(responseDto);

        assertDoesNotThrow(() ->
                trazabilidadAdapter.registrarCambioEstado(1L, 10L, "cliente@correo.com",
                        "PENDIENTE", "EN_PREPARACION", 20L, "empleado@correo.com")
        );

        verify(trazabilidadFeignClient, times(1)).registrarCambioEstado(any(TrazabilidadFeignRequestDto.class));
    }

    @Test
    @DisplayName("Debe capturar FeignException sin propagar error si el microservicio de trazabilidad falla")
    void registrarCambioEstado_feignException_capturaSinLanzar() {
        Request request = Request.create(Request.HttpMethod.POST, "/api/v1/trazabilidad", Collections.emptyMap(), null, StandardCharsets.UTF_8, new RequestTemplate());
        when(trazabilidadFeignClient.registrarCambioEstado(any(TrazabilidadFeignRequestDto.class)))
                .thenThrow(new FeignException.InternalServerError("Error MongoDB", request, null, null));

        assertDoesNotThrow(() ->
                trazabilidadAdapter.registrarCambioEstado(1L, 10L, "cliente@correo.com",
                        "PENDIENTE", "EN_PREPARACION", 20L, "empleado@correo.com")
        );

        verify(trazabilidadFeignClient, times(1)).registrarCambioEstado(any(TrazabilidadFeignRequestDto.class));
    }

    @Test
    @DisplayName("Debe capturar excepción genérica sin propagar error")
    void registrarCambioEstado_genericException_capturaSinLanzar() {
        when(trazabilidadFeignClient.registrarCambioEstado(any(TrazabilidadFeignRequestDto.class)))
                .thenThrow(new RuntimeException("Conexión interrumpida"));

        assertDoesNotThrow(() ->
                trazabilidadAdapter.registrarCambioEstado(1L, 10L, "cliente@correo.com",
                        "PENDIENTE", "EN_PREPARACION", 20L, "empleado@correo.com")
        );

        verify(trazabilidadFeignClient, times(1)).registrarCambioEstado(any(TrazabilidadFeignRequestDto.class));
    }
}
