package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.OrderRequestDto;

import com.pragma.powerup.application.dto.response.OrderResponseDto;

import java.util.List;

public interface IOrderHandler {

    void realizarPedido(OrderRequestDto orderRequestDto);

    List<OrderResponseDto> listarPedidosPorEstado(String estado, Long idRestaurante, int page, int size);

    OrderResponseDto asignarPedido(Long idPedido);
}
