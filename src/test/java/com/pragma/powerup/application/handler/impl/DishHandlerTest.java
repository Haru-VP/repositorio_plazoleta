package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.DishRequestDto;
import com.pragma.powerup.application.dto.request.UpdateDishRequestDto;
import com.pragma.powerup.application.mapper.IDishRequestMapper;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishHandlerTest {

    @Mock
    private IDishServicePort dishServicePort;

    @Mock
    private IDishRequestMapper dishRequestMapper;

    @InjectMocks
    private DishHandler dishHandler;

    private DishRequestDto dishRequestDto;
    private DishModel dishModel;

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
                null,
                "Bandeja Paisa",
                categoryModel,
                "Deliciosa bandeja tradicional",
                35000,
                restaurantModel,
                "https://imagenes.com/bandeja-paisa.png",
                null
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
        com.pragma.powerup.application.dto.request.DishStatusRequestDto statusDto =
                new com.pragma.powerup.application.dto.request.DishStatusRequestDto(false);

        // Act
        dishHandler.cambiarEstadoPlato(idPlato, statusDto);

        // Assert
        verify(dishServicePort, times(1)).cambiarEstadoPlato(idPlato, false);
    }
}
