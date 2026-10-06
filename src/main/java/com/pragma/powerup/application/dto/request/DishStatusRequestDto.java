package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos para habilitar o deshabilitar un plato en el menú")
public class DishStatusRequestDto {

    @Schema(description = "Estado de disponibilidad del plato (true para habilitar, false para deshabilitar)", example = "false")
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
}
