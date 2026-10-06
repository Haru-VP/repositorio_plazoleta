package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.DishRequestDto;
import com.pragma.powerup.application.dto.request.DishStatusRequestDto;
import com.pragma.powerup.application.dto.request.UpdateDishRequestDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;
import com.pragma.powerup.application.handler.IDishHandler;
import com.pragma.powerup.application.mapper.IDishRequestMapper;
import com.pragma.powerup.application.mapper.IDishResponseMapper;
import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.model.DishModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;

    @Override
    public void guardarPlato(DishRequestDto dishRequestDto) {
        DishModel dishModel = dishRequestMapper.toModel(dishRequestDto);
        dishServicePort.guardarPlato(dishModel);
    }

    @Override
    public void actualizarPlato(Long id, UpdateDishRequestDto updateDishRequestDto) {
        dishServicePort.actualizarPlato(id, updateDishRequestDto.getPrecio(), updateDishRequestDto.getDescripcion());
    }

    @Override
    public void cambiarEstadoPlato(Long id, DishStatusRequestDto dishStatusRequestDto) {
        dishServicePort.cambiarEstadoPlato(id, dishStatusRequestDto.getActivo());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DishResponseDto> listarPlatosPorRestaurante(Long idRestaurante, Long idCategoria, int page, int size) {
        List<DishModel> dishModelList = dishServicePort.listarPlatosPorRestaurante(idRestaurante, idCategoria, page, size);
        return dishResponseMapper.toResponseDtoList(dishModelList);
    }
}
