package com.pragma.powerup.infrastructure.out.feign.adapter;

import com.pragma.powerup.domain.spi.ITrazabilidadServicePort;
import com.pragma.powerup.infrastructure.out.feign.client.ITrazabilidadFeignClient;
import com.pragma.powerup.infrastructure.out.feign.dto.TrazabilidadFeignRequestDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class TrazabilidadAdapter implements ITrazabilidadServicePort {

    private final ITrazabilidadFeignClient trazabilidadFeignClient;

    @Override
    public void registrarCambioEstado(Long idPedido, Long idCliente, String correoCliente,
                                      String estadoAnterior, String nuevoEstado,
                                      Long idEmpleado, String correoEmpleado) {
        try {
            TrazabilidadFeignRequestDto requestDto = new TrazabilidadFeignRequestDto(
                    idPedido, idCliente, correoCliente, LocalDateTime.now(),
                    estadoAnterior, nuevoEstado, idEmpleado, correoEmpleado
            );
            trazabilidadFeignClient.registrarCambioEstado(requestDto);
            log.info("Cambio de estado {} -> {} registrado en trazabilidad para pedido #{}",
                    estadoAnterior, nuevoEstado, idPedido);
        } catch (FeignException e) {
            log.warn("No se pudo registrar la trazabilidad para el pedido #{} (servicio no disponible): {}",
                    idPedido, e.getMessage());
        } catch (Exception e) {
            log.warn("Excepción al intentar registrar trazabilidad para el pedido #{}: {}",
                    idPedido, e.getMessage());
        }
    }
}
