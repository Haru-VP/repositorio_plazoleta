package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.DishRequestDto;
import com.pragma.powerup.application.dto.request.DishStatusRequestDto;
import com.pragma.powerup.application.dto.request.UpdateDishRequestDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;
import com.pragma.powerup.application.mapper.IDishRequestMapper;
import com.pragma.powerup.application.mapper.IDishResponseMapper;
import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.model.CategoryModel;
import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.RestaurantModel;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishHandlerTest {

    @Mock
    private IDishServicePort dishServicePort;

    @Mock
    private IDishRequestMapper dishRequestMapper;

    @Mock
    private IDishResponseMapper dishResponseMapper;

    @InjectMocks
    private DishHandler dishHandler;

    private DishRequestDto dishRequestDto;
    private DishModel dishModel;
    private DishResponseDto dishResponseDto;

    @BeforeEach
    void setUp() {
        dishRequestDto = new DishRequestDto(
                "Bandeja Paisa",
                1L,
                "Deliciosa bandeja tradicional",
                35000,
                1L,
                "https://imagenes.com/bandeja-paisa.png"
        );

        CategoryModel categoryModel = new CategoryModel(1L, "PLATO FUERTE", "Platos principales");
        RestaurantModel restaurantModel = new RestaurantModel();
        restaurantModel.setId(1L);

        dishModel = new DishModel(
                1L,
                "Bandeja Paisa",
                categoryModel,
                "Deliciosa bandeja tradicional",
                35000,
                restaurantModel,
                "https://imagenes.com/bandeja-paisa.png",
                true
        );

        dishResponseDto = new DishResponseDto(
                1L,
                "Bandeja Paisa",
                35000,
                "Deliciosa bandeja tradicional",
                "https://imagenes.com/bandeja-paisa.png",
                "PLATO FUERTE"
        );
    }

    @Test
    void guardarPlato_debeMapearYLlamarAlPuertoDelDominio() {
        // Arrange
        when(dishRequestMapper.toModel(dishRequestDto)).thenReturn(dishModel);

        // Act
        dishHandler.guardarPlato(dishRequestDto);

        // Assert
        verify(dishRequestMapper, times(1)).toModel(dishRequestDto);
        verify(dishServicePort, times(1)).guardarPlato(dishModel);
    }

    @Test
    void actualizarPlato_debeLlamarAlPuertoDelDominioConParametrosCorrectos() {
        // Arrange
        Long idPlato = 1L;
        UpdateDishRequestDto updateDto = new UpdateDishRequestDto(40000, "Nueva descripción");

        // Act
        dishHandler.actualizarPlato(idPlato, updateDto);

        // Assert
        verify(dishServicePort, times(1)).actualizarPlato(idPlato, 40000, "Nueva descripción");
    }

    @Test
    void cambiarEstadoPlato_debeLlamarAlPuertoDelDominioConParametrosCorrectos() {
        // Arrange
        Long idPlato = 1L;
        DishStatusRequestDto statusDto = new DishStatusRequestDto(false);

        // Act
        dishHandler.cambiarEstadoPlato(idPlato, statusDto);

        // Assert
        verify(dishServicePort, times(1)).cambiarEstadoPlato(idPlato, false);
    }

    @Test
    void listarPlatosPorRestaurante_debeMapearYLlamarAlPuertoDelDominio() {
        // Arrange
        Long idRestaurante = 1L;
        Long idCategoria = 1L;
        List<DishModel> modelList = Collections.singletonList(dishModel);
        List<DishResponseDto> dtoList = Collections.singletonList(dishResponseDto);

        when(dishServicePort.listarPlatosPorRestaurante(idRestaurante, idCategoria, 0, 10))
                .thenReturn(modelList);
        when(dishResponseMapper.toResponseDtoList(modelList))
                .thenReturn(dtoList);

        // Act
        List<DishResponseDto> resultado = dishHandler.listarPlatosPorRestaurante(idRestaurante, idCategoria, 0, 10);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Bandeja Paisa", resultado.get(0).getNombre());
        assertEquals("PLATO FUERTE", resultado.get(0).getCategoria());
        verify(dishServicePort, times(1)).listarPlatosPorRestaurante(idRestaurante, idCategoria, 0, 10);
        verify(dishResponseMapper, times(1)).toResponseDtoList(modelList);
    }
}
