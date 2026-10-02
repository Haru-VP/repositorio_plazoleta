package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de solicitud para entregar pedido con verificación de PIN")
public class DeliverOrderRequestDto {

    @Schema(description = "Identificador del pedido a entregar", example = "1")
    private Long idPedido;

    @Schema(description = "Identificador alternativo del pedido", example = "1")
    private Long id;

    @NotBlank(message = "El PIN de seguridad es obligatorio para entregar el pedido")
    @Pattern(regexp = "^[0-9]{6}$", message = "El PIN de seguridad debe contener exactamente 6 dígitos numéricos")
    @Schema(description = "PIN numérico de seguridad de 6 dígitos suministrado por el cliente", example = "123456")
    private String pin;

    public Long getIdPedidoEfectivo() {
        return idPedido != null ? idPedido : id;
    }
}
