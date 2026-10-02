package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEmployeeEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IOrderRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final IRestaurantEmployeeRepository restaurantEmployeeRepository;

    private static final List<String> ESTADOS_ACTIVOS = Arrays.asList(
            OrderStatus.PENDIENTE.name(),
            OrderStatus.EN_PREPARACION.name(),
            OrderStatus.LISTO.name()
    );

    @Override
    public void guardarPedido(OrderModel orderModel) {
        OrderEntity orderEntity = orderEntityMapper.toEntity(orderModel);
        OrderEntity guardado = orderRepository.save(orderEntity);
        if (guardado != null && guardado.getId() != null) {
            orderModel.setId(guardado.getId());
        }
    }

    @Override
    public boolean existePedidoActivoPorCliente(Long idCliente) {
        return orderRepository.existsByIdClienteAndEstadoIn(idCliente, ESTADOS_ACTIVOS);
    }

    @Override
    public List<OrderModel> listarPedidosPorEstado(String estado, Long idRestaurante, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<OrderEntity> pageResult;
        if (idRestaurante != null) {
            pageResult = orderRepository.findAllByRestauranteIdAndEstado(idRestaurante, estado, pageable);
        } else {
            pageResult = orderRepository.findAllByEstado(estado, pageable);
        }
        return orderEntityMapper.toModelList(pageResult.getContent());
    }

    @Override
    public Long obtenerIdRestaurantePorEmpleado(Long idEmpleado) {
        return restaurantEmployeeRepository.findByIdEmpleado(idEmpleado)
                .map(RestaurantEmployeeEntity::getIdRestaurante)
                .orElse(null);
    }

    @Override
    public OrderModel obtenerPedidoPorId(Long id) {
        if (id == null) {
            return null;
        }
        return orderRepository.findById(id)
                .map(orderEntityMapper::toModel)
                .orElse(null);
    }

    @Override
    public OrderModel actualizarPedido(OrderModel orderModel) {
        if (orderModel == null || orderModel.getId() == null) {
            return null;
        }
        OrderEntity orderEntity = orderRepository.findById(orderModel.getId()).orElse(null);
        if (orderEntity != null) {
            orderEntity.setEstado(orderModel.getEstado());
            orderEntity.setIdChef(orderModel.getIdChef());
            OrderEntity saved = orderRepository.save(orderEntity);
            return orderEntityMapper.toModel(saved);
        }
        OrderEntity toSave = orderEntityMapper.toEntity(orderModel);
        return orderEntityMapper.toModel(orderRepository.save(toSave));
    }
}
