package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de un pedido para visualización por el personal del restaurante")
public class OrderResponseDto {

    @Schema(description = "Identificador único del pedido", example = "1")
    private Long id;

    @Schema(description = "Identificador del cliente que realizó el pedido", example = "5")
    private Long idCliente;

    @Schema(description = "Fecha de realización del pedido", example = "2026-09-29")
    private LocalDate fecha;

    @Schema(description = "Estado actual del pedido", example = "PENDIENTE")
    private String estado;

    @Schema(description = "Identificador del chef asignado (null si no ha sido asignado)", example = "null")
    private Long idChef;

    @Schema(description = "PIN de seguridad asignado para la entrega del pedido", example = "123456")
    private String pin;

    @Schema(description = "Identificador del restaurante al que pertenece el pedido", example = "1")
    private Long idRestaurante;

    @Schema(description = "Lista de platos con sus detalles y cantidades incluidas en el pedido")
    private List<OrderDishResponseDto> platos;
}
