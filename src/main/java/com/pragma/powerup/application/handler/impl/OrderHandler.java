package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.application.mapper.IOrderRequestMapper;
import com.pragma.powerup.application.mapper.IOrderResponseMapper;
import com.pragma.powerup.domain.api.IOrderServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;

    @Override
    public void realizarPedido(OrderRequestDto orderRequestDto) {
        orderServicePort.realizarPedido(orderRequestMapper.toOrderModel(orderRequestDto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> listarPedidosPorEstado(String estado, Long idRestaurante, int page, int size) {
        return orderResponseMapper.toResponseDtoList(
                orderServicePort.listarPedidosPorEstado(estado, idRestaurante, page, size)
        );
    }

    @Override
    public OrderResponseDto asignarPedido(Long idPedido) {
        return orderResponseMapper.toResponseDto(orderServicePort.asignarPedido(idPedido));
    }
}
