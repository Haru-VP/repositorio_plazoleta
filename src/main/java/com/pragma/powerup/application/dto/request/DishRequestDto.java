package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos requeridos para la creación de un nuevo plato en el menú")
public class DishRequestDto {

    @Schema(description = "Nombre comercial del plato", example = "Bandeja Paisa")
    @NotBlank(message = "El nombre del plato es obligatorio")
    private String nombre;

    @Schema(description = "Identificador de la categoría a la que pertenece el plato", example = "2")
    @NotNull(message = "El ID de la categoría es obligatorio")
    private Long idCategoria;

    @Schema(description = "Descripción detallada de ingredientes y preparación del plato", example = "Deliciosa bandeja con frijoles, arroz, carne molida, chicharrón, huevo y aguacate")
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @Schema(description = "Precio del plato (número entero positivo mayor a 0)", example = "35000")
    @NotNull(message = "El precio es obligatorio")
    @Min(value = 1, message = "El precio debe ser un número entero mayor a 0")
    private Integer precio;

    @Schema(description = "Identificador del restaurante al cual se asocia el plato", example = "1")
    @NotNull(message = "El ID del restaurante es obligatorio")
    private Long idRestaurante;

    @Schema(description = "URL accesible que apunta a la fotografía del plato", example = "https://misimagenes.com/platos/bandeja-paisa.png")
    @NotBlank(message = "La URL de la imagen es obligatoria")
    private String urlImagen;
}
