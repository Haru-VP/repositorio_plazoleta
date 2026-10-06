package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.DishModel;

import java.util.List;

public interface IDishPersistencePort {

    void guardarPlato(DishModel dishModel);

    DishModel obtenerPlatoPorId(Long id);

    void actualizarPlato(DishModel dishModel);

    List<DishModel> listarPlatosPorRestaurante(Long idRestaurante, Long idCategoria, int page, int size);
}
