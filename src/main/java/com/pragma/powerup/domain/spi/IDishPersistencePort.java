package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.DishModel;

public interface IDishPersistencePort {

    void guardarPlato(DishModel dishModel);

    DishModel obtenerPlatoPorId(Long id);

    void actualizarPlato(DishModel dishModel);
}
