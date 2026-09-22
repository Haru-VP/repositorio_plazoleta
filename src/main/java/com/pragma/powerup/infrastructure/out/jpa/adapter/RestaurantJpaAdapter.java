package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public void guardarRestaurante(RestaurantModel restaurantModel) {
        RestaurantEntity restaurantEntity = restaurantEntityMapper.toEntity(restaurantModel);
        restaurantRepository.save(restaurantEntity);
    }

    @Override
    public boolean existePorNit(String nit) {
        return Boolean.TRUE.equals(restaurantRepository.existsByNit(nit));
    }
}
