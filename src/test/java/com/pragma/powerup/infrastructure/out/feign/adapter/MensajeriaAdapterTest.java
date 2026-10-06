package com.pragma.powerup.infrastructure.out.feign.adapter;

import com.pragma.powerup.infrastructure.out.feign.client.IMensajeriaFeignClient;
import com.pragma.powerup.infrastructure.out.feign.dto.SmsFeignRequestDto;
import com.pragma.powerup.infrastructure.out.feign.dto.SmsFeignResponseDto;
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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MensajeriaAdapterTest {

    @Mock
    private IMensajeriaFeignClient mensajeriaFeignClient;

    @InjectMocks
    private MensajeriaAdapter mensajeriaAdapter;

    @Test
    @DisplayName("Debe retornar true al enviar notificación SMS con éxito")
    void notificarPedidoListo_exitoso_retornaTrue() {
        SmsFeignResponseDto responseDto = new SmsFeignResponseDto("+573001234567", "Mensaje", "123456", 1L, "ENVIADO");
        when(mensajeriaFeignClient.notificarSms(any(SmsFeignRequestDto.class))).thenReturn(responseDto);

        boolean resultado = mensajeriaAdapter.notificarPedidoListo("+573001234567", "123456", 1L);

        assertTrue(resultado);
        verify(mensajeriaFeignClient, times(1)).notificarSms(any(SmsFeignRequestDto.class));
    }

    @Test
    @DisplayName("Debe retornar false si el número de teléfono es nulo o vacío")
    void notificarPedidoListo_telefonoNuloOVacio_retornaFalse() {
        boolean resNull = mensajeriaAdapter.notificarPedidoListo(null, "123456", 1L);
        boolean resVacio = mensajeriaAdapter.notificarPedidoListo("   ", "123456", 1L);

        assertFalse(resNull);
        assertFalse(resVacio);
        verify(mensajeriaFeignClient, never()).notificarSms(any());
    }

    @Test
    @DisplayName("Debe capturar FeignException y retornar false si el microservicio de mensajería falla")
    void notificarPedidoListo_errorFeign_retornaFalse() {
        Request request = Request.create(Request.HttpMethod.POST, "/api/v1/sms/notify", Collections.emptyMap(), null, StandardCharsets.UTF_8, new RequestTemplate());
        when(mensajeriaFeignClient.notificarSms(any(SmsFeignRequestDto.class)))
                .thenThrow(new FeignException.InternalServerError("Error Twilio", request, null, null));

        boolean resultado = mensajeriaAdapter.notificarPedidoListo("+573001234567", "123456", 1L);

        assertFalse(resultado);
        verify(mensajeriaFeignClient, times(1)).notificarSms(any(SmsFeignRequestDto.class));
    }
}
