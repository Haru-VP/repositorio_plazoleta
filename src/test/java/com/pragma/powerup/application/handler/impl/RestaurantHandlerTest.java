package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.mapper.IRestaurantRequestMapper;
import com.pragma.powerup.application.mapper.IRestaurantResponseMapper;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
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
class RestaurantHandlerTest {

    @Mock
    private IRestaurantServicePort restaurantServicePort;

    @Mock
    private IRestaurantRequestMapper restaurantRequestMapper;

    @Mock
    private IRestaurantResponseMapper restaurantResponseMapper;

    @InjectMocks
    private RestaurantHandler restaurantHandler;

    private RestaurantRequestDto restaurantRequestDto;
    private RestaurantModel restaurantModel;
    private RestaurantResponseDto restaurantResponseDto;

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

        restaurantResponseDto = new RestaurantResponseDto(
                "Restaurante El Buen Sabor",
                "https://imagen.com/logo.png"
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

    @Test
    void listarRestaurantes_debeMapearYLlamarAlPuertoDelDominio() {
        // Arrange
        List<RestaurantModel> modelList = Collections.singletonList(restaurantModel);
        List<RestaurantResponseDto> dtoList = Collections.singletonList(restaurantResponseDto);
        when(restaurantServicePort.listarRestaurantes(0, 10)).thenReturn(modelList);
        when(restaurantResponseMapper.toResponseDtoList(modelList)).thenReturn(dtoList);

        // Act
        List<RestaurantResponseDto> resultado = restaurantHandler.listarRestaurantes(0, 10);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Restaurante El Buen Sabor", resultado.get(0).getNombre());
        assertEquals("https://imagen.com/logo.png", resultado.get(0).getUrlLogo());
        verify(restaurantServicePort, times(1)).listarRestaurantes(0, 10);
        verify(restaurantResponseMapper, times(1)).toResponseDtoList(modelList);
    }
}
