package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.OrderModel;

public interface IOrderPersistencePort {

    void guardarPedido(OrderModel orderModel);

    boolean existePedidoActivoPorCliente(Long idCliente);
}
