package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.NitInvalidoException;
import com.pragma.powerup.domain.exception.NitYaExisteException;
import com.pragma.powerup.domain.exception.NombreInvalidoException;
import com.pragma.powerup.domain.exception.ParametrosPaginacionInvalidosException;
import com.pragma.powerup.domain.exception.TelefonoInvalidoException;
import com.pragma.powerup.domain.exception.UsuarioNoEsPropietarioException;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserValidationServicePort;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IUserValidationServicePort userValidationServicePort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private RestaurantModel restauranteValido;

    @BeforeEach
    void setUp() {
        restauranteValido = new RestaurantModel();
        restauranteValido.setNombre("Restaurante El Gran Sabor 2");
        restauranteValido.setNit("900123456");
        restauranteValido.setDireccion("Calle 100 # 15-20");
        restauranteValido.setTelefono("+573001234567");
        restauranteValido.setUrlLogo("https://imagenes.com/logo.png");
        restauranteValido.setIdPropietario(2L);
    }

    @Test
    void guardarRestaurante_exitoso() {
        // Arrange
        when(userValidationServicePort.esPropietario(2L)).thenReturn(true);
        when(restaurantPersistencePort.existePorNit("900123456")).thenReturn(false);

        // Act
        restaurantUseCase.guardarRestaurante(restauranteValido);

        // Assert
        verify(restaurantPersistencePort, times(1)).guardarRestaurante(restauranteValido);
    }

    @Test
    void guardarRestaurante_nombreSoloNumeros_lanzaExcepcion() {
        // Arrange: Nombre compuesto únicamente por números
        restauranteValido.setNombre("12345678");

        // Act & Assert
        assertThrows(NombreInvalidoException.class, () -> restaurantUseCase.guardarRestaurante(restauranteValido));
        verify(restaurantPersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_nombreVacio_lanzaExcepcion() {
        // Arrange: Nombre vacío
        restauranteValido.setNombre("   ");

        // Act & Assert
        assertThrows(NombreInvalidoException.class, () -> restaurantUseCase.guardarRestaurante(restauranteValido));
        verify(restaurantPersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_nitInvalidoConLetras_lanzaExcepcion() {
        // Arrange: NIT con letras
        restauranteValido.setNit("900ABC123");

        // Act & Assert
        assertThrows(NitInvalidoException.class, () -> restaurantUseCase.guardarRestaurante(restauranteValido));
        verify(restaurantPersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_telefonoInvalidoMayorA13_lanzaExcepcion() {
        // Arrange: Teléfono con más de 13 caracteres
        restauranteValido.setTelefono("+57300123456789");

        // Act & Assert
        assertThrows(TelefonoInvalidoException.class, () -> restaurantUseCase.guardarRestaurante(restauranteValido));
        verify(restaurantPersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_telefonoInvalidoConLetras_lanzaExcepcion() {
        // Arrange: Teléfono con letras
        restauranteValido.setTelefono("+57300ABC45");

        // Act & Assert
        assertThrows(TelefonoInvalidoException.class, () -> restaurantUseCase.guardarRestaurante(restauranteValido));
        verify(restaurantPersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_usuarioNoEsPropietario_lanzaExcepcion() {
        // Arrange: El usuario no tiene rol de propietario
        when(userValidationServicePort.esPropietario(2L)).thenReturn(false);

        // Act & Assert
        assertThrows(UsuarioNoEsPropietarioException.class, () -> restaurantUseCase.guardarRestaurante(restauranteValido));
        verify(restaurantPersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void guardarRestaurante_nitYaExiste_lanzaExcepcion() {
        // Arrange: El NIT ya existe en base de datos
        when(userValidationServicePort.esPropietario(2L)).thenReturn(true);
        when(restaurantPersistencePort.existePorNit("900123456")).thenReturn(true);

        // Act & Assert
        assertThrows(NitYaExisteException.class, () -> restaurantUseCase.guardarRestaurante(restauranteValido));
        verify(restaurantPersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    void listarRestaurantes_exitoso() {
        // Arrange
        List<RestaurantModel> listaEsperada = Collections.singletonList(restauranteValido);
        when(restaurantPersistencePort.listarRestaurantes(0, 10)).thenReturn(listaEsperada);

        // Act
        List<RestaurantModel> resultado = restaurantUseCase.listarRestaurantes(0, 10);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Restaurante El Gran Sabor 2", resultado.get(0).getNombre());
        verify(restaurantPersistencePort, times(1)).listarRestaurantes(0, 10);
    }

    @Test
    void listarRestaurantes_paginaNegativa_lanzaExcepcion() {
        // Act & Assert
        assertThrows(ParametrosPaginacionInvalidosException.class,
                () -> restaurantUseCase.listarRestaurantes(-1, 10));
        verify(restaurantPersistencePort, never()).listarRestaurantes(anyInt(), anyInt());
    }

    @Test
    void listarRestaurantes_tamanioMenorOIgualACero_lanzaExcepcion() {
        // Act & Assert
        assertThrows(ParametrosPaginacionInvalidosException.class,
                () -> restaurantUseCase.listarRestaurantes(0, 0));
        verify(restaurantPersistencePort, never()).listarRestaurantes(anyInt(), anyInt());
    }

    @Test
    void listarRestaurantes_tamanioNegativo_lanzaExcepcion() {
        // Act & Assert
        assertThrows(ParametrosPaginacionInvalidosException.class,
                () -> restaurantUseCase.listarRestaurantes(0, -5));
        verify(restaurantPersistencePort, never()).listarRestaurantes(anyInt(), anyInt());
    }
}
