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
@Schema(description = "Datos permitidos para la actualización de un plato existente (únicamente precio y descripción)")
public class UpdateDishRequestDto {

    @Schema(description = "Nuevo precio del plato (número entero positivo mayor a 0)", example = "38000")
    @NotNull(message = "El precio es obligatorio")
    @Min(value = 1, message = "El precio debe ser un número entero mayor a 0")
    private Integer precio;

    @Schema(description = "Nueva descripción detallada del plato", example = "Deliciosa bandeja paisa tradicional con ingredientes frescos y porción extra de aguacate")
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
}
