package com.pragma.powerup.domain.spi;

public interface IUserValidationServicePort {

    boolean esPropietario(Long idPropietario);

    String obtenerCelularUsuario(Long idUsuario);
}
