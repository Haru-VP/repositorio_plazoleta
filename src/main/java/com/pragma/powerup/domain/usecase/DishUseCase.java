package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.exception.CategoriaNoEncontradaException;
import com.pragma.powerup.domain.exception.PlatoNoEncontradoException;
import com.pragma.powerup.domain.exception.PrecioInvalidoException;
import com.pragma.powerup.domain.exception.RestauranteNoEncontradoException;
import com.pragma.powerup.domain.exception.UsuarioNoAutorizadoException;
import com.pragma.powerup.domain.model.CategoryModel;
import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IAuthenticatedUserPort;
import com.pragma.powerup.domain.spi.ICategoryPersistencePort;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;

public class DishUseCase implements IDishServicePort {

    private static final int PRECIO_MINIMO_VALIDO = 0;

    private final IDishPersistencePort dishPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IAuthenticatedUserPort authenticatedUserPort;

    public DishUseCase(IDishPersistencePort dishPersistencePort,
                       ICategoryPersistencePort categoryPersistencePort,
                       IRestaurantPersistencePort restaurantPersistencePort,
                       IAuthenticatedUserPort authenticatedUserPort) {
        this.dishPersistencePort = dishPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.authenticatedUserPort = authenticatedUserPort;
    }

    @Override
    public void guardarPlato(DishModel dishModel) {
        validarPrecio(dishModel.getPrecio());
        RestaurantModel restaurante = obtenerRestauranteValidado(dishModel.getRestaurante());
        validarPropietarioDelRestaurante(restaurante);
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

        RestaurantModel restaurante = obtenerRestauranteValidado(platoExistente.getRestaurante());
        validarPropietarioDelRestaurante(restaurante);

        platoExistente.setPrecio(precio);
        platoExistente.setDescripcion(descripcion);
        dishPersistencePort.actualizarPlato(platoExistente);
    }

    private void validarPrecio(Integer precio) {
        if (precio == null || precio <= PRECIO_MINIMO_VALIDO) {
            throw new PrecioInvalidoException();
        }
    }

    private RestaurantModel obtenerRestauranteValidado(RestaurantModel restaurante) {
        if (restaurante == null || restaurante.getId() == null) {
            throw new RestauranteNoEncontradoException();
        }
        RestaurantModel restauranteEncontrado = restaurantPersistencePort.obtenerPorId(restaurante.getId());
        if (restauranteEncontrado == null) {
            throw new RestauranteNoEncontradoException();
        }
        return restauranteEncontrado;
    }

    private void validarPropietarioDelRestaurante(RestaurantModel restaurante) {
        Long idUsuarioAutenticado = authenticatedUserPort.obtenerIdUsuarioAutenticado();
        if (idUsuarioAutenticado == null || !idUsuarioAutenticado.equals(restaurante.getIdPropietario())) {
            throw new UsuarioNoAutorizadoException();
        }
    }

    private void validarCategoria(CategoryModel categoria) {
        if (categoria == null || categoria.getId() == null || !categoryPersistencePort.existePorId(categoria.getId())) {
            throw new CategoriaNoEncontradaException();
        }
    }
}
