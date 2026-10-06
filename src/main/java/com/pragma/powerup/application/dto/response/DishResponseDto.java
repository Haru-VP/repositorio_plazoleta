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
@Schema(description = "Datos de un plato para la visualización del menú por el cliente")
public class DishResponseDto {

    @Schema(description = "Identificador único del plato", example = "1")
    private Long id;

    @Schema(description = "Nombre comercial del plato", example = "Bandeja Paisa")
    private String nombre;

    @Schema(description = "Precio del plato en pesos colombianos", example = "35000")
    private Integer precio;

    @Schema(description = "Descripción detallada de ingredientes", example = "Frijoles, arroz, carne molida, chicharrón, huevo y aguacate")
    private String descripcion;

    @Schema(description = "URL accesible de la imagen del plato", example = "https://misimagenes.com/platos/bandeja-paisa.png")
    private String urlImagen;

    @Schema(description = "Nombre de la categoría del plato", example = "PLATO FUERTE")
    private String categoria;
}
