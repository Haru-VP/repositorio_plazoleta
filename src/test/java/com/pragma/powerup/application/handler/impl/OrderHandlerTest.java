package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.OrderDishRequestDto;
import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.mapper.IOrderRequestMapper;
import com.pragma.powerup.application.mapper.IOrderResponseMapper;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.OrderModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderHandlerTest {

    @Mock
    private IOrderServicePort orderServicePort;

    @Mock
    private IOrderRequestMapper orderRequestMapper;

    @Mock
    private IOrderResponseMapper orderResponseMapper;

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

    @Test
    void listarPedidosPorEstado_exitoso() {
        // Arrange
        String estado = "PENDIENTE";
        Long idRestaurante = 1L;
        OrderModel orderModel = new OrderModel();
        OrderResponseDto responseDto = new OrderResponseDto();

        when(orderServicePort.listarPedidosPorEstado(estado, idRestaurante, 0, 10))
                .thenReturn(Collections.singletonList(orderModel));
        when(orderResponseMapper.toResponseDtoList(Collections.singletonList(orderModel)))
                .thenReturn(Collections.singletonList(responseDto));

        // Act
        List<OrderResponseDto> resultado = orderHandler.listarPedidosPorEstado(estado, idRestaurante, 0, 10);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(orderServicePort, times(1)).listarPedidosPorEstado(estado, idRestaurante, 0, 10);
        verify(orderResponseMapper, times(1)).toResponseDtoList(Collections.singletonList(orderModel));
    }

    @Test
    void asignarPedido_exitoso() {
        // Arrange
        Long idPedido = 1L;
        OrderModel orderModel = new OrderModel();
        OrderResponseDto responseDto = new OrderResponseDto();

        when(orderServicePort.asignarPedido(idPedido)).thenReturn(orderModel);
        when(orderResponseMapper.toResponseDto(orderModel)).thenReturn(responseDto);

        // Act
        OrderResponseDto resultado = orderHandler.asignarPedido(idPedido);

        // Assert
        assertNotNull(resultado);
        verify(orderServicePort, times(1)).asignarPedido(idPedido);
        verify(orderResponseMapper, times(1)).toResponseDto(orderModel);
    }

    @Test
    void notificarPedidoListo_exitoso() {
        // Arrange
        Long idPedido = 1L;
        OrderModel orderModel = new OrderModel();
        OrderResponseDto responseDto = new OrderResponseDto();

        when(orderServicePort.notificarPedidoListo(idPedido)).thenReturn(orderModel);
        when(orderResponseMapper.toResponseDto(orderModel)).thenReturn(responseDto);

        // Act
        OrderResponseDto resultado = orderHandler.notificarPedidoListo(idPedido);

        // Assert
        assertNotNull(resultado);
        verify(orderServicePort, times(1)).notificarPedidoListo(idPedido);
        verify(orderResponseMapper, times(1)).toResponseDto(orderModel);
    }
}
