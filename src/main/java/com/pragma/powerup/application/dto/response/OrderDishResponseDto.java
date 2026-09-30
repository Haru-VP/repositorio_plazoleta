package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Detalle de un plato y cantidad ordenada")
public class OrderDishResponseDto {

    @Schema(description = "Identificador del plato", example = "10")
    private Long id;

    @Schema(description = "Identificador del plato (alias)", example = "10")
    private Long idPlato;

    @Schema(description = "Nombre comercial del plato", example = "Bandeja Paisa")
    private String nombre;

    @Schema(description = "Precio del plato", example = "35000")
    private Integer precio;

    @Schema(description = "Descripción de ingredientes", example = "Frijoles, arroz, carne molida...")
    private String descripcion;

    @Schema(description = "URL de la imagen del plato", example = "https://misimagenes.com/platos/bandeja-paisa.png")
    private String urlImagen;

    @Schema(description = "Nombre de la categoría del plato", example = "PLATO FUERTE")
    private String categoria;

    @Schema(description = "Cantidad ordenada del plato", example = "2")
    private Integer cantidad;
}
