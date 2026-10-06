package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.CategoryModel;

public interface ICategoryPersistencePort {

    boolean existePorId(Long id);

    CategoryModel obtenerPorId(Long id);
}
