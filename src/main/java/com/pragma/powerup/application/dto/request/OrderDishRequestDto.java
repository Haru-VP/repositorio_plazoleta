package com.pragma.powerup.application.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Detalle de un plato y cantidad para el pedido")
public class OrderDishRequestDto {

    @Schema(description = "Identificador único del plato a ordenar", example = "1")
    @NotNull(message = "El ID del plato es obligatorio")
    @JsonAlias({"idPlato", "platoId", "idDish", "dishId"})
    private Long idPlato;

    @Schema(description = "Cantidad deseada del plato (entero mayor a 0)", example = "2")
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser un número entero mayor a 0")
    @JsonAlias({"cantidad", "quantity"})
    private Integer cantidad;
}
