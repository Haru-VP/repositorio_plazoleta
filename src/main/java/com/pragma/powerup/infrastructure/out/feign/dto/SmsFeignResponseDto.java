package com.pragma.powerup.infrastructure.out.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsFeignResponseDto {
    private String numeroTelefono;
    private String mensaje;
    private String pin;
    private Long idPedido;
    private String estado;
}
