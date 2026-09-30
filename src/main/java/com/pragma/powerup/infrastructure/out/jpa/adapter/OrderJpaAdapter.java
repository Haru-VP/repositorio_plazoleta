package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

    private static final List<String> ESTADOS_ACTIVOS = Arrays.asList(
            OrderStatus.PENDIENTE.name(),
            OrderStatus.EN_PREPARACION.name(),
            OrderStatus.LISTO.name()
    );

    @Override
    public void guardarPedido(OrderModel orderModel) {
        OrderEntity orderEntity = orderEntityMapper.toEntity(orderModel);
        orderRepository.save(orderEntity);
    }

    @Override
    public boolean existePedidoActivoPorCliente(Long idCliente) {
        return orderRepository.existsByIdClienteAndEstadoIn(idCliente, ESTADOS_ACTIVOS);
    }
}
