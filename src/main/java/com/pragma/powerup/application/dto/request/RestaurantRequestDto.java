package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos requeridos para la creación de un nuevo restaurante")
public class RestaurantRequestDto {

    @Schema(description = "Nombre comercial del restaurante (no puede estar compuesto únicamente por números)", example = "Restaurante El Buen Sabor")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Número de Identificación Tributaria (NIT únicamente numérico)", example = "900123456")
    @NotBlank(message = "El NIT es obligatorio")
    private String nit;

    @Schema(description = "Dirección física del restaurante", example = "Calle 100 # 15-20")
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @Schema(description = "Número telefónico de contacto (máximo 13 caracteres, admite prefijo +)", example = "+573001234567")
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @Schema(description = "URL accesible que apunta a la imagen del logo del restaurante", example = "https://misimagenes.com/logo-restaurante.png")
    @NotBlank(message = "La URL del logo es obligatoria")
    private String urlLogo;

    @Schema(description = "ID del usuario que actúa como propietario (debe existir y tener rol PROPIETARIO)", example = "1")
    @NotNull(message = "El ID del propietario es obligatorio")
    private Long idPropietario;
}
