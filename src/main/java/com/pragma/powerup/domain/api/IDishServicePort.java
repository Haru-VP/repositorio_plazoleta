package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.DishModel;

import java.util.List;

public interface IDishServicePort {

    void guardarPlato(DishModel dishModel);

    void actualizarPlato(Long id, Integer precio, String descripcion);

    void cambiarEstadoPlato(Long id, Boolean activo);

    List<DishModel> listarPlatosPorRestaurante(Long idRestaurante, Long idCategoria, int page, int size);
}
