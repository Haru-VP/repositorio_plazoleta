package com.pragma.powerup.infrastructure.out.feign.adapter;

import com.pragma.powerup.domain.spi.IMensajeriaServicePort;
import com.pragma.powerup.infrastructure.out.feign.client.IMensajeriaFeignClient;
import com.pragma.powerup.infrastructure.out.feign.dto.SmsFeignRequestDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MensajeriaAdapter implements IMensajeriaServicePort {

    private final IMensajeriaFeignClient mensajeriaFeignClient;

    @Override
    public boolean notificarPedidoListo(String numeroTelefono, String pin, Long idPedido) {
        try {
            if (numeroTelefono == null || numeroTelefono.trim().isEmpty()) {
                log.warn("No se puede notificar SMS: el cliente no tiene número de teléfono registrado.");
                return false;
            }
            String mensaje = "Tu pedido #" + idPedido + " está listo para ser reclamado. Tu PIN de seguridad es: " + pin;
            SmsFeignRequestDto request = new SmsFeignRequestDto(numeroTelefono, mensaje, pin, idPedido);
            mensajeriaFeignClient.notificarSms(request);
            log.info("Notificación SMS enviada exitosamente para pedido #{}", idPedido);
            return true;
        } catch (FeignException e) {
            log.error("Error al comunicarse con el microservicio de mensajería: {}", e.getMessage());
            return false;
        }
    }
}
