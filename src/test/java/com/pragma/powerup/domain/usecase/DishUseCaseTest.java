package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.CategoriaNoEncontradaException;
import com.pragma.powerup.domain.exception.ParametrosPaginacionInvalidosException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IAuthenticatedUserPort authenticatedUserPort;

    @InjectMocks
    private DishUseCase dishUseCase;

    private DishModel platoValido;
    private RestaurantModel restauranteValido;
    private CategoryModel categoriaValida;

    @BeforeEach
    void setUp() {
        restauranteValido = new RestaurantModel();
        restauranteValido.setId(1L);
        restauranteValido.setIdPropietario(1L);

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
        when(restaurantPersistencePort.obtenerPorId(1L)).thenReturn(restauranteValido);
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(1L);
        when(categoryPersistencePort.existePorId(1L)).thenReturn(true);

        // Act
        dishUseCase.guardarPlato(platoValido);

        // Assert
        assertTrue(platoValido.getActivo());
        verify(restaurantPersistencePort).obtenerPorId(1L);
        verify(authenticatedUserPort).obtenerIdUsuarioAutenticado();
        verify(categoryPersistencePort).existePorId(1L);
        verify(dishPersistencePort).guardarPlato(platoValido);
    }

    @Test
    void guardarPlato_usuarioNoEsPropietario_lanzaUsuarioNoAutorizadoException() {
        // Arrange
        when(restaurantPersistencePort.obtenerPorId(1L)).thenReturn(restauranteValido);
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(999L);

        // Act & Assert
        assertThrows(UsuarioNoAutorizadoException.class, () -> dishUseCase.guardarPlato(platoValido));
        verify(dishPersistencePort, never()).guardarPlato(any());
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
        when(restaurantPersistencePort.obtenerPorId(1L)).thenReturn(null);

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
        when(restaurantPersistencePort.obtenerPorId(1L)).thenReturn(restauranteValido);
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(1L);
        when(categoryPersistencePort.existePorId(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(CategoriaNoEncontradaException.class, () -> dishUseCase.guardarPlato(platoValido));
        verify(dishPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void guardarPlato_categoriaNula_lanzaExcepcion() {
        // Arrange
        when(restaurantPersistencePort.obtenerPorId(1L)).thenReturn(restauranteValido);
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(1L);
        platoValido.setCategoria(null);

        // Act & Assert
        assertThrows(CategoriaNoEncontradaException.class, () -> dishUseCase.guardarPlato(platoValido));
        verify(dishPersistencePort, never()).guardarPlato(any());
    }

    @Test
    void actualizarPlato_datosValidos_actualizaPrecioYDescripcionExitosamente() {
        // Arrange
        Long idPlato = 1L;
        Integer nuevoPrecio = 42000;
        String nuevaDescripcion = "Bandeja paisa con porción extra de aguacate";
        when(dishPersistencePort.obtenerPlatoPorId(idPlato)).thenReturn(platoValido);
        when(restaurantPersistencePort.obtenerPorId(1L)).thenReturn(restauranteValido);
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(1L);

        // Act
        dishUseCase.actualizarPlato(idPlato, nuevoPrecio, nuevaDescripcion);

        // Assert
        assertEquals(nuevoPrecio, platoValido.getPrecio());
        assertEquals(nuevaDescripcion, platoValido.getDescripcion());
        assertEquals("Bandeja Paisa", platoValido.getNombre());
        verify(dishPersistencePort).obtenerPlatoPorId(idPlato);
        verify(restaurantPersistencePort).obtenerPorId(1L);
        verify(authenticatedUserPort).obtenerIdUsuarioAutenticado();
        verify(dishPersistencePort).actualizarPlato(platoValido);
    }

    @Test
    void actualizarPlato_usuarioNoEsPropietario_lanzaUsuarioNoAutorizadoException() {
        // Arrange
        Long idPlato = 1L;
        when(dishPersistencePort.obtenerPlatoPorId(idPlato)).thenReturn(platoValido);
        when(restaurantPersistencePort.obtenerPorId(1L)).thenReturn(restauranteValido);
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(999L);

        // Act & Assert
        assertThrows(UsuarioNoAutorizadoException.class, () ->
                dishUseCase.actualizarPlato(idPlato, 42000, "Descripción de prueba")
        );
        verify(dishPersistencePort, never()).actualizarPlato(any());
    }

    @Test
    void actualizarPlato_platoNoExiste_lanzaPlatoNoEncontradoException() {
        // Arrange
        Long idPlatoInexistente = 999L;
        when(dishPersistencePort.obtenerPlatoPorId(idPlatoInexistente)).thenReturn(null);

        // Act & Assert
        assertThrows(PlatoNoEncontradoException.class, () ->
                dishUseCase.actualizarPlato(idPlatoInexistente, 42000, "Descripción de prueba")
        );
        verify(dishPersistencePort, never()).actualizarPlato(any());
    }

    @Test
    void actualizarPlato_precioInvalido_lanzaPrecioInvalidoException() {
        // Arrange
        Long idPlato = 1L;

        // Act & Assert
        assertThrows(PrecioInvalidoException.class, () ->
                dishUseCase.actualizarPlato(idPlato, 0, "Descripción de prueba")
        );
        assertThrows(PrecioInvalidoException.class, () ->
                dishUseCase.actualizarPlato(idPlato, -500, "Descripción de prueba")
        );
        assertThrows(PrecioInvalidoException.class, () ->
                dishUseCase.actualizarPlato(idPlato, null, "Descripción de prueba")
        );
        verify(dishPersistencePort, never()).obtenerPlatoPorId(any());
        verify(dishPersistencePort, never()).actualizarPlato(any());
    }

    @Test
    void cambiarEstadoPlato_propietarioValido_cambiaEstadoAFalseYActualiza() {
        // Arrange
        Long idPlato = 1L;
        platoValido.setActivo(true);
        when(dishPersistencePort.obtenerPlatoPorId(idPlato)).thenReturn(platoValido);
        when(restaurantPersistencePort.obtenerPorId(1L)).thenReturn(restauranteValido);
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(1L);

        // Act
        dishUseCase.cambiarEstadoPlato(idPlato, false);

        // Assert
        assertEquals(false, platoValido.getActivo());
        verify(dishPersistencePort).obtenerPlatoPorId(idPlato);
        verify(restaurantPersistencePort).obtenerPorId(1L);
        verify(authenticatedUserPort).obtenerIdUsuarioAutenticado();
        verify(dishPersistencePort).actualizarPlato(platoValido);
    }

    @Test
    void cambiarEstadoPlato_usuarioNoEsPropietario_lanzaUsuarioNoAutorizadoException() {
        // Arrange
        Long idPlato = 1L;
        when(dishPersistencePort.obtenerPlatoPorId(idPlato)).thenReturn(platoValido);
        when(restaurantPersistencePort.obtenerPorId(1L)).thenReturn(restauranteValido);
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(999L);

        // Act & Assert
        assertThrows(UsuarioNoAutorizadoException.class, () ->
                dishUseCase.cambiarEstadoPlato(idPlato, false)
        );
        verify(dishPersistencePort, never()).actualizarPlato(any());
    }

    @Test
    void cambiarEstadoPlato_platoNoExiste_lanzaPlatoNoEncontradoException() {
        // Arrange
        Long idPlatoInexistente = 999L;
        when(dishPersistencePort.obtenerPlatoPorId(idPlatoInexistente)).thenReturn(null);

        // Act & Assert
        assertThrows(PlatoNoEncontradoException.class, () ->
                dishUseCase.cambiarEstadoPlato(idPlatoInexistente, false)
        );
        verify(dishPersistencePort, never()).actualizarPlato(any());
    }

    @Test
    void listarPlatosPorRestaurante_conCategoria_exitoso() {
        // Arrange
        Long idRestaurante = 1L;
        Long idCategoria = 1L;
        when(restaurantPersistencePort.existePorId(idRestaurante)).thenReturn(true);
        when(categoryPersistencePort.existePorId(idCategoria)).thenReturn(true);
        when(dishPersistencePort.listarPlatosPorRestaurante(idRestaurante, idCategoria, 0, 10))
                .thenReturn(Collections.singletonList(platoValido));

        // Act
        List<DishModel> resultado = dishUseCase.listarPlatosPorRestaurante(idRestaurante, idCategoria, 0, 10);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Bandeja Paisa", resultado.get(0).getNombre());
        verify(dishPersistencePort, times(1)).listarPlatosPorRestaurante(idRestaurante, idCategoria, 0, 10);
    }

    @Test
    void listarPlatosPorRestaurante_sinCategoria_exitoso() {
        // Arrange
        Long idRestaurante = 1L;
        when(restaurantPersistencePort.existePorId(idRestaurante)).thenReturn(true);
        when(dishPersistencePort.listarPlatosPorRestaurante(idRestaurante, null, 0, 10))
                .thenReturn(Collections.singletonList(platoValido));

        // Act
        List<DishModel> resultado = dishUseCase.listarPlatosPorRestaurante(idRestaurante, null, 0, 10);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(categoryPersistencePort, never()).existePorId(any());
        verify(dishPersistencePort, times(1)).listarPlatosPorRestaurante(idRestaurante, null, 0, 10);
    }

    @Test
    void listarPlatosPorRestaurante_paginaNegativa_lanzaParametrosPaginacionInvalidosException() {
        // Act & Assert
        assertThrows(ParametrosPaginacionInvalidosException.class, () ->
                dishUseCase.listarPlatosPorRestaurante(1L, null, -1, 10)
        );
        verify(dishPersistencePort, never()).listarPlatosPorRestaurante(any(), any(), anyInt(), anyInt());
    }

    @Test
    void listarPlatosPorRestaurante_tamanioInvalido_lanzaParametrosPaginacionInvalidosException() {
        // Act & Assert
        assertThrows(ParametrosPaginacionInvalidosException.class, () ->
                dishUseCase.listarPlatosPorRestaurante(1L, null, 0, 0)
        );
        verify(dishPersistencePort, never()).listarPlatosPorRestaurante(any(), any(), anyInt(), anyInt());
    }

    @Test
    void listarPlatosPorRestaurante_restauranteNoExiste_lanzaRestauranteNoEncontradoException() {
        // Arrange
        when(restaurantPersistencePort.existePorId(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(RestauranteNoEncontradoException.class, () ->
                dishUseCase.listarPlatosPorRestaurante(999L, null, 0, 10)
        );
        verify(dishPersistencePort, never()).listarPlatosPorRestaurante(any(), any(), anyInt(), anyInt());
    }

    @Test
    void listarPlatosPorRestaurante_categoriaNoExiste_lanzaCategoriaNoEncontradaException() {
        // Arrange
        when(restaurantPersistencePort.existePorId(1L)).thenReturn(true);
        when(categoryPersistencePort.existePorId(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(CategoriaNoEncontradaException.class, () ->
                dishUseCase.listarPlatosPorRestaurante(1L, 999L, 0, 10)
        );
        verify(dishPersistencePort, never()).listarPlatosPorRestaurante(any(), any(), anyInt(), anyInt());
    }
}
