package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.DishEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    @Override
    public void guardarPlato(DishModel dishModel) {
        DishEntity dishEntity = dishEntityMapper.toEntity(dishModel);
        dishRepository.save(dishEntity);
    }

    @Override
    public DishModel obtenerPlatoPorId(Long id) {
        return dishRepository.findById(id)
                .map(dishEntityMapper::toDishModel)
                .orElse(null);
    }

    @Override
    public void actualizarPlato(DishModel dishModel) {
        DishEntity dishEntity = dishEntityMapper.toEntity(dishModel);
        dishRepository.save(dishEntity);
    }

    @Override
    public List<DishModel> listarPlatosPorRestaurante(Long idRestaurante, Long idCategoria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        Page<DishEntity> dishEntityPage;
        if (idCategoria != null) {
            dishEntityPage = dishRepository.findAllByRestauranteIdAndCategoriaIdAndActivoTrue(idRestaurante, idCategoria, pageable);
        } else {
            dishEntityPage = dishRepository.findAllByRestauranteIdAndActivoTrue(idRestaurante, pageable);
        }
        return dishEntityMapper.toDishModelList(dishEntityPage.getContent());
    }
}
