package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.OrderModel;

public interface IOrderServicePort {

    void realizarPedido(OrderModel orderModel);
}
