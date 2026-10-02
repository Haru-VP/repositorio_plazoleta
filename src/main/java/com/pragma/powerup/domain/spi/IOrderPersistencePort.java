package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.OrderModel;

import java.util.List;

public interface IOrderPersistencePort {

    void guardarPedido(OrderModel orderModel);

    boolean existePedidoActivoPorCliente(Long idCliente);

    List<OrderModel> listarPedidosPorEstado(String estado, Long idRestaurante, int page, int size);

    Long obtenerIdRestaurantePorEmpleado(Long idEmpleado);

    OrderModel obtenerPedidoPorId(Long id);

    OrderModel actualizarPedido(OrderModel orderModel);
}
