package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {

    boolean existsByIdClienteAndEstadoIn(Long idCliente, List<String> estados);

    List<OrderEntity> findAllByIdCliente(Long idCliente);
}
