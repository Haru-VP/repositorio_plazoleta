package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.RestaurantModel;

public interface IRestaurantPersistencePort {

    void guardarRestaurante(RestaurantModel restaurantModel);

    boolean existePorNit(String nit);

    boolean existePorId(Long id);

    RestaurantModel obtenerPorId(Long id);
}
