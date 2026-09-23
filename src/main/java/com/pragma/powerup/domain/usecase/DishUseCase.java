package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.exception.CategoriaNoEncontradaException;
import com.pragma.powerup.domain.exception.PlatoNoEncontradoException;
import com.pragma.powerup.domain.exception.PrecioInvalidoException;
import com.pragma.powerup.domain.exception.RestauranteNoEncontradoException;
import com.pragma.powerup.domain.model.CategoryModel;
import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.ICategoryPersistencePort;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;

public class DishUseCase implements IDishServicePort {

    private static final int PRECIO_MINIMO_VALIDO = 0;

    private final IDishPersistencePort dishPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort,
                       ICategoryPersistencePort categoryPersistencePort,
                       IRestaurantPersistencePort restaurantPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void guardarPlato(DishModel dishModel) {
        validarPrecio(dishModel.getPrecio());
        validarRestaurante(dishModel.getRestaurante());
        validarCategoria(dishModel.getCategoria());

        dishModel.setActivo(Boolean.TRUE);
        dishPersistencePort.guardarPlato(dishModel);
    }

    @Override
    public void actualizarPlato(Long id, Integer precio, String descripcion) {
        validarPrecio(precio);
        DishModel platoExistente = dishPersistencePort.obtenerPlatoPorId(id);
        if (platoExistente == null) {
            throw new PlatoNoEncontradoException();
        }

        platoExistente.setPrecio(precio);
        platoExistente.setDescripcion(descripcion);
        dishPersistencePort.actualizarPlato(platoExistente);
    }

    private void validarPrecio(Integer precio) {
        if (precio == null || precio <= PRECIO_MINIMO_VALIDO) {
            throw new PrecioInvalidoException();
        }
    }

    private void validarRestaurante(RestaurantModel restaurante) {
        if (restaurante == null || restaurante.getId() == null || !restaurantPersistencePort.existePorId(restaurante.getId())) {
            throw new RestauranteNoEncontradoException();
        }
    }

    private void validarCategoria(CategoryModel categoria) {
        if (categoria == null || categoria.getId() == null || !categoryPersistencePort.existePorId(categoria.getId())) {
            throw new CategoriaNoEncontradaException();
        }
    }
}
