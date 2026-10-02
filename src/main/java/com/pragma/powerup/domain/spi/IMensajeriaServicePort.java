package com.pragma.powerup.domain.spi;

public interface IMensajeriaServicePort {

    boolean notificarPedidoListo(String numeroTelefono, String pin, Long idPedido);
}
