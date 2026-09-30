package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {

    boolean existsByIdClienteAndEstadoIn(Long idCliente, List<String> estados);

    List<OrderEntity> findAllByIdCliente(Long idCliente);

    @EntityGraph(attributePaths = {"pedidosPlatos", "pedidosPlatos.plato", "pedidosPlatos.plato.categoria", "restaurante"})
    Page<OrderEntity> findAllByRestauranteIdAndEstado(Long restauranteId, String estado, Pageable pageable);

    @EntityGraph(attributePaths = {"pedidosPlatos", "pedidosPlatos.plato", "pedidosPlatos.plato.categoria", "restaurante"})
    Page<OrderEntity> findAllByEstado(String estado, Pageable pageable);
}
