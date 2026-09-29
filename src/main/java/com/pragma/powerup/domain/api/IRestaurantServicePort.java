package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.RestaurantModel;

import java.util.List;

public interface IRestaurantServicePort {

    void guardarRestaurante(RestaurantModel restaurantModel);

    List<RestaurantModel> listarRestaurantes(int page, int size);
}
