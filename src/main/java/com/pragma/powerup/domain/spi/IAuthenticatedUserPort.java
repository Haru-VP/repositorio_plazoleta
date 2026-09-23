package com.pragma.powerup.domain.spi;

public interface IAuthenticatedUserPort {

    Long obtenerIdUsuarioAutenticado();

    String obtenerRolUsuarioAutenticado();
}
