package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.DishRequestDto;
import com.pragma.powerup.application.dto.request.DishStatusRequestDto;
import com.pragma.powerup.application.dto.request.UpdateDishRequestDto;

public interface IDishHandler {

    void guardarPlato(DishRequestDto dishRequestDto);

    void actualizarPlato(Long id, UpdateDishRequestDto updateDishRequestDto);

    void cambiarEstadoPlato(Long id, DishStatusRequestDto dishStatusRequestDto);
}
