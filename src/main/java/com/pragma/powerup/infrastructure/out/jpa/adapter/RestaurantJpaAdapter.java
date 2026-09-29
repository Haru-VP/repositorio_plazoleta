package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

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

    @Override
    public boolean existePorId(Long id) {
        return restaurantRepository.existsById(id);
    }

    @Override
    public RestaurantModel obtenerPorId(Long id) {
        return restaurantRepository.findById(id)
                .map(restaurantEntityMapper::toRestaurantModel)
                .orElse(null);
    }

    @Override
    public List<RestaurantModel> listarRestaurantes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        Page<RestaurantEntity> restaurantEntityPage = restaurantRepository.findAll(pageable);
        return restaurantEntityMapper.toRestaurantModelList(restaurantEntityPage.getContent());
    }
}
