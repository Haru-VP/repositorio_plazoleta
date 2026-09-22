package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.RestaurantModel;

public interface IRestaurantServicePort {

    void guardarRestaurante(RestaurantModel restaurantModel);
}
