package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.OrderDishRequestDto;
import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.mapper.IOrderRequestMapper;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.OrderModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderHandlerTest {

    @Mock
    private IOrderServicePort orderServicePort;

    @Mock
    private IOrderRequestMapper orderRequestMapper;

    @InjectMocks
    private OrderHandler orderHandler;

    @Test
    void realizarPedido_exitoso() {
        // Arrange
        OrderDishRequestDto dishDto = new OrderDishRequestDto(1L, 2);
        OrderRequestDto requestDto = new OrderRequestDto(10L, Collections.singletonList(dishDto));
        OrderModel orderModel = new OrderModel();

        when(orderRequestMapper.toOrderModel(requestDto)).thenReturn(orderModel);

        // Act
        orderHandler.realizarPedido(requestDto);

        // Assert
        verify(orderRequestMapper, times(1)).toOrderModel(requestDto);
        verify(orderServicePort, times(1)).realizarPedido(orderModel);
    }
}
