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
@Schema(description = "Datos del restaurante para clientes (nombre y logotipo)")
public class RestaurantResponseDto {

    @Schema(description = "Nombre comercial del restaurante", example = "Restaurante El Buen Sabor")
    private String nombre;

    @Schema(description = "URL del logotipo del restaurante", example = "https://misimagenes.com/logos/elbuensabor.png")
    private String urlLogo;
}
