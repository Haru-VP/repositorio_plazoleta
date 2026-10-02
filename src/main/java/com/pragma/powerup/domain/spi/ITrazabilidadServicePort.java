package com.pragma.powerup.domain.spi;

public interface ITrazabilidadServicePort {

    void registrarCambioEstado(Long idPedido, Long idCliente, String correoCliente,
                               String estadoAnterior, String nuevoEstado,
                               Long idEmpleado, String correoEmpleado);
}
