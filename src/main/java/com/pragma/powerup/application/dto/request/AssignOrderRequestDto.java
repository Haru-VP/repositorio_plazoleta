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
@Schema(description = "DTO de solicitud para asignarse a un pedido")
public class AssignOrderRequestDto {

    @Schema(description = "Identificador del pedido a asignar", example = "1")
    private Long idPedido;

    @Schema(description = "Identificador alternativo del pedido a asignar", example = "1")
    private Long id;

    public Long getIdPedidoEfectivo() {
        return idPedido != null ? idPedido : id;
    }
}
