package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.mapper.IRestaurantRequestMapper;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.RestaurantModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantHandlerTest {

    @Mock
    private IRestaurantServicePort restaurantServicePort;

    @Mock
    private IRestaurantRequestMapper restaurantRequestMapper;

    @InjectMocks
    private RestaurantHandler restaurantHandler;

    private RestaurantRequestDto restaurantRequestDto;
    private RestaurantModel restaurantModel;

    @BeforeEach
    void setUp() {
        restaurantRequestDto = new RestaurantRequestDto(
                "Restaurante El Buen Sabor",
                "123456789",
                "Calle 100 # 15-20",
                "+573001234567",
                "https://imagen.com/logo.png",
                1L
        );

        restaurantModel = new RestaurantModel(
                null,
                "Restaurante El Buen Sabor",
                "123456789",
                "Calle 100 # 15-20",
                "+573001234567",
                "https://imagen.com/logo.png",
                1L
        );
    }

    @Test
    void guardarRestaurante_debeMapearYLlamarAlPuertoDelDominio() {
        // Arrange
        when(restaurantRequestMapper.toModel(restaurantRequestDto)).thenReturn(restaurantModel);

        // Act
        restaurantHandler.guardarRestaurante(restaurantRequestDto);

        // Assert
        verify(restaurantRequestMapper, times(1)).toModel(restaurantRequestDto);
        verify(restaurantServicePort, times(1)).guardarRestaurante(restaurantModel);
    }
}
