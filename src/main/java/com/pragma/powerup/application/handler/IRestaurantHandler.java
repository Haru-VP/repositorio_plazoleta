package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;

import java.util.List;

public interface IRestaurantHandler {

    void guardarRestaurante(RestaurantRequestDto restaurantRequestDto);

    List<RestaurantResponseDto> listarRestaurantes(int page, int size);
}
