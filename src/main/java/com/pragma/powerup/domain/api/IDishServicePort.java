package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.DishModel;

public interface IDishServicePort {

    void guardarPlato(DishModel dishModel);

    void actualizarPlato(Long id, Integer precio, String descripcion);

    void cambiarEstadoPlato(Long id, Boolean activo);
}
