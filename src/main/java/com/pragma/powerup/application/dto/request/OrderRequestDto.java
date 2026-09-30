package com.pragma.powerup.application.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos para realizar un pedido a un restaurante")
public class OrderRequestDto {

    @Schema(description = "Identificador del restaurante al que se le realiza el pedido", example = "1")
    @NotNull(message = "El ID del restaurante es obligatorio")
    @JsonAlias({"idRestaurante", "restauranteId", "restaurantId"})
    private Long idRestaurante;

    @Schema(description = "Lista de platos con sus respectivas cantidades a ordenar")
    @NotEmpty(message = "La lista de platos no puede estar vacía")
    @Valid
    @JsonAlias({"platos", "elementosPedido", "items", "dishes"})
    private List<OrderDishRequestDto> platos;
}
