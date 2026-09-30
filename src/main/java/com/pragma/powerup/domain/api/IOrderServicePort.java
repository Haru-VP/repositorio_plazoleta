package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.OrderModel;

import java.util.List;

public interface IOrderServicePort {

    void realizarPedido(OrderModel orderModel);

    List<OrderModel> listarPedidosPorEstado(String estado, Long idRestaurante, int page, int size);
}
