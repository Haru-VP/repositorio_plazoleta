package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.OrderModel;

import java.util.List;

public interface IOrderServicePort {

    void realizarPedido(OrderModel orderModel);

    List<OrderModel> listarPedidosPorEstado(String estado, Long idRestaurante, int page, int size);

    OrderModel asignarPedido(Long idPedido);

    OrderModel notificarPedidoListo(Long idPedido);

    OrderModel entregarPedido(Long idPedido, String pin);

    OrderModel cancelarPedido(Long idPedido);
}
