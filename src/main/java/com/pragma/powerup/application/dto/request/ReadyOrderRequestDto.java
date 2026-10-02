package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de solicitud para notificar pedido listo")
public class ReadyOrderRequestDto {

    @Schema(description = "Identificador del pedido a marcar como listo", example = "1")
    private Long idPedido;

    @Schema(description = "Identificador alternativo del pedido", example = "1")
    private Long id;

    public Long getIdPedidoEfectivo() {
        return idPedido != null ? idPedido : id;
    }
}
