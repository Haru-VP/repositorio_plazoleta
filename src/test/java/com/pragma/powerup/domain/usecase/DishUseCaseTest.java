package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.CategoriaNoEncontradaException;
import com.pragma.powerup.domain.exception.PrecioInvalidoException;
import com.pragma.powerup.domain.exception.RestauranteNoEncontradoException;
import com.pragma.powerup.domain.model.CategoryModel;
import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.ICategoryPersistencePort;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    private DishUseCase dishUseCase;

    private DishModel platoValido;
    private RestaurantModel restauranteValido;
    private CategoryModel categoriaValida;

    @BeforeEach
    void setUp() {
        restauranteValido = new RestaurantModel();
        restauranteValido.setId(1L);

        categoriaValida = new CategoryModel();
        categoriaValida.setId(1L);

        platoValido = new DishModel(
                null,
                "Bandeja Paisa",
                categoriaValida,
                "Deliciosa bandeja con frijoles, arroz, carne molida, chicharrón, huevo y aguacate",
                35000,
                restauranteValido,
                "https://imagenes.com/bandeja-paisa.png",
                null
        );
    }

    @Test
    void guardarPlato_datosValidos_guardaPlatoConActivoEnTrue() {
        // Arrange
        when(restaurantPersistencePort.existePorId(1L)).thenReturn(true);
        when(categoryPersistencePort.existePorId(1L)).thenReturn(true);

        // Act
        dishUseCase.guardarPlato(platoValido);

        // Assert
        assertTrue(platoValido.getActivo());
        verify(restaurantPersistencePort).existePorId(1L);
        verify(categoryPersistencePort).existePorId(1L);
        verify(dishPersistencePort).guardarPlato(platoValido);
    }

    @Test
    void guardarPlato_precioCero_lanzaExcepcion() {
        // Arrange
        platoValido.setPrecio(0);

        // Act & Assert
        assertThrows(PrecioInvalidoException.class, () -> dishUseCase.guardarPlato(platoValido));
        verify(dishPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void guardarPlato_precioNegativo_lanzaExcepcion() {
        // Arrange
        platoValido.setPrecio(-15000);

        // Act & Assert
        assertThrows(PrecioInvalidoException.class, () -> dishUseCase.guardarPlato(platoValido));
        verify(dishPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void guardarPlato_precioNulo_lanzaExcepcion() {
        // Arrange
        platoValido.setPrecio(null);

        // Act & Assert
        assertThrows(PrecioInvalidoException.class, () -> dishUseCase.guardarPlato(platoValido));
        verify(dishPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void guardarPlato_restauranteNoExiste_lanzaExcepcion() {
        // Arrange
        when(restaurantPersistencePort.existePorId(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(RestauranteNoEncontradoException.class, () -> dishUseCase.guardarPlato(platoValido));
        verify(dishPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void guardarPlato_restauranteNulo_lanzaExcepcion() {
        // Arrange
        platoValido.setRestaurante(null);

        // Act & Assert
        assertThrows(RestauranteNoEncontradoException.class, () -> dishUseCase.guardarPlato(platoValido));
        verify(dishPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void guardarPlato_categoriaNoExiste_lanzaExcepcion() {
        // Arrange
        when(restaurantPersistencePort.existePorId(1L)).thenReturn(true);
        when(categoryPersistencePort.existePorId(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(CategoriaNoEncontradaException.class, () -> dishUseCase.guardarPlato(platoValido));
        verify(dishPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void guardarPlato_categoriaNula_lanzaExcepcion() {
        // Arrange
        when(restaurantPersistencePort.existePorId(1L)).thenReturn(true);
        platoValido.setCategoria(null);

        // Act & Assert
        assertThrows(CategoriaNoEncontradaException.class, () -> dishUseCase.guardarPlato(platoValido));
        verify(dishPersistencePort, never()).guardarPlato(any());
    }
}
