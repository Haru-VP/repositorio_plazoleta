package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderJpaAdapterTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IOrderEntityMapper orderEntityMapper;

    @InjectMocks
    private OrderJpaAdapter orderJpaAdapter;

    @Test
    void guardarPedido_exitoso() {
        // Arrange
        OrderModel orderModel = new OrderModel();
        OrderEntity orderEntity = new OrderEntity();
        when(orderEntityMapper.toEntity(orderModel)).thenReturn(orderEntity);

        // Act
        orderJpaAdapter.guardarPedido(orderModel);

        // Assert
        verify(orderEntityMapper, times(1)).toEntity(orderModel);
        verify(orderRepository, times(1)).save(orderEntity);
    }

    @Test
    void existePedidoActivoPorCliente_retornaTrueCuandoExiste() {
        // Arrange
        Long idCliente = 5L;
        when(orderRepository.existsByIdClienteAndEstadoIn(eq(idCliente), any())).thenReturn(true);

        // Act
        boolean resultado = orderJpaAdapter.existePedidoActivoPorCliente(idCliente);

        // Assert
        assertTrue(resultado);
        verify(orderRepository, times(1)).existsByIdClienteAndEstadoIn(eq(idCliente), any());
    }

    @Test
    void existePedidoActivoPorCliente_retornaFalseCuandoNoExiste() {
        // Arrange
        Long idCliente = 5L;
        when(orderRepository.existsByIdClienteAndEstadoIn(eq(idCliente), any())).thenReturn(false);

        // Act
        boolean resultado = orderJpaAdapter.existePedidoActivoPorCliente(idCliente);

        // Assert
        assertFalse(resultado);
        verify(orderRepository, times(1)).existsByIdClienteAndEstadoIn(eq(idCliente), any());
    }
}
