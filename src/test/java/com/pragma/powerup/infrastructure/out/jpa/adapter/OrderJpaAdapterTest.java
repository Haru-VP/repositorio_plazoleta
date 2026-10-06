package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEmployeeEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IOrderRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantEmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

    @Mock
    private IRestaurantEmployeeRepository restaurantEmployeeRepository;

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

    @Test
    void listarPedidosPorEstado_conRestaurante_exitoso() {
        // Arrange
        Long idRestaurante = 1L;
        String estado = "PENDIENTE";
        OrderEntity orderEntity = new OrderEntity();
        OrderModel orderModel = new OrderModel();
        Page<OrderEntity> page = new PageImpl<>(Collections.singletonList(orderEntity));

        when(orderRepository.findAllByRestauranteIdAndEstado(eq(idRestaurante), eq(estado), any(Pageable.class)))
                .thenReturn(page);
        when(orderEntityMapper.toModelList(Collections.singletonList(orderEntity)))
                .thenReturn(Collections.singletonList(orderModel));

        // Act
        List<OrderModel> resultado = orderJpaAdapter.listarPedidosPorEstado(estado, idRestaurante, 0, 10);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(orderRepository, times(1)).findAllByRestauranteIdAndEstado(eq(idRestaurante), eq(estado), any(Pageable.class));
    }

    @Test
    void listarPedidosPorEstado_sinRestaurante_exitoso() {
        // Arrange
        String estado = "PENDIENTE";
        OrderEntity orderEntity = new OrderEntity();
        OrderModel orderModel = new OrderModel();
        Page<OrderEntity> page = new PageImpl<>(Collections.singletonList(orderEntity));

        when(orderRepository.findAllByEstado(eq(estado), any(Pageable.class)))
                .thenReturn(page);
        when(orderEntityMapper.toModelList(Collections.singletonList(orderEntity)))
                .thenReturn(Collections.singletonList(orderModel));

        // Act
        List<OrderModel> resultado = orderJpaAdapter.listarPedidosPorEstado(estado, null, 0, 10);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(orderRepository, times(1)).findAllByEstado(eq(estado), any(Pageable.class));
    }

    @Test
    void obtenerIdRestaurantePorEmpleado_cuandoExiste_retornaIdRestaurante() {
        // Arrange
        Long idEmpleado = 10L;
        RestaurantEmployeeEntity entity = new RestaurantEmployeeEntity(1L, 2L, idEmpleado);
        when(restaurantEmployeeRepository.findByIdEmpleado(idEmpleado)).thenReturn(Optional.of(entity));

        // Act
        Long resultado = orderJpaAdapter.obtenerIdRestaurantePorEmpleado(idEmpleado);

        // Assert
        assertEquals(2L, resultado);
        verify(restaurantEmployeeRepository, times(1)).findByIdEmpleado(idEmpleado);
    }

    @Test
    void obtenerIdRestaurantePorEmpleado_cuandoNoExiste_retornaNull() {
        // Arrange
        Long idEmpleado = 10L;
        when(restaurantEmployeeRepository.findByIdEmpleado(idEmpleado)).thenReturn(Optional.empty());

        // Act
        Long resultado = orderJpaAdapter.obtenerIdRestaurantePorEmpleado(idEmpleado);

        // Assert
        assertNull(resultado);
        verify(restaurantEmployeeRepository, times(1)).findByIdEmpleado(idEmpleado);
    }

    @Test
    void obtenerPedidoPorId_cuandoExiste_retornaOrderModel() {
        // Arrange
        Long id = 1L;
        OrderEntity entity = new OrderEntity();
        OrderModel model = new OrderModel();
        when(orderRepository.findById(id)).thenReturn(Optional.of(entity));
        when(orderEntityMapper.toModel(entity)).thenReturn(model);

        // Act
        OrderModel resultado = orderJpaAdapter.obtenerPedidoPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(model, resultado);
        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPedidoPorId_cuandoNoExiste_retornaNull() {
        // Arrange
        Long id = 999L;
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        OrderModel resultado = orderJpaAdapter.obtenerPedidoPorId(id);

        // Assert
        assertNull(resultado);
        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPedidoPorId_idNulo_retornaNull() {
        // Act
        OrderModel resultado = orderJpaAdapter.obtenerPedidoPorId(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    void actualizarPedido_existente_actualizaEstadoYChefYRetornaModel() {
        // Arrange
        OrderModel input = new OrderModel();
        input.setId(1L);
        input.setEstado("EN_PREPARACION");
        input.setIdChef(50L);

        OrderEntity existing = new OrderEntity();
        existing.setId(1L);
        existing.setEstado("PENDIENTE");

        OrderEntity saved = new OrderEntity();
        saved.setId(1L);
        saved.setEstado("EN_PREPARACION");
        saved.setIdChef(50L);

        OrderModel output = new OrderModel();
        output.setId(1L);
        output.setEstado("EN_PREPARACION");
        output.setIdChef(50L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(orderRepository.save(existing)).thenReturn(saved);
        when(orderEntityMapper.toModel(saved)).thenReturn(output);

        // Act
        OrderModel resultado = orderJpaAdapter.actualizarPedido(input);

        // Assert
        assertNotNull(resultado);
        assertEquals("EN_PREPARACION", resultado.getEstado());
        assertEquals(50L, resultado.getIdChef());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(existing);
    }

    @Test
    void actualizarPedido_noExistente_guardaNuevoYRetornaModel() {
        // Arrange
        OrderModel input = new OrderModel();
        input.setId(1L);
        input.setEstado("EN_PREPARACION");

        OrderEntity toSave = new OrderEntity();
        OrderEntity saved = new OrderEntity();
        OrderModel output = new OrderModel();

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        when(orderEntityMapper.toEntity(input)).thenReturn(toSave);
        when(orderRepository.save(toSave)).thenReturn(saved);
        when(orderEntityMapper.toModel(saved)).thenReturn(output);

        // Act
        OrderModel resultado = orderJpaAdapter.actualizarPedido(input);

        // Assert
        assertNotNull(resultado);
        verify(orderRepository, times(1)).save(toSave);
    }

    @Test
    void actualizarPedido_nulo_retornaNull() {
        // Act
        OrderModel resultado = orderJpaAdapter.actualizarPedido(null);

        // Assert
        assertNull(resultado);
    }
}
