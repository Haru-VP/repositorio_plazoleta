package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.OrderDishResponseDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.domain.model.OrderDishModel;
import com.pragma.powerup.domain.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IOrderResponseMapper {

    @Mapping(target = "idRestaurante", source = "restaurante.id")
    @Mapping(target = "platos", source = "pedidosPlatos")
    OrderResponseDto toResponseDto(OrderModel orderModel);

    List<OrderResponseDto> toResponseDtoList(List<OrderModel> orderModelList);

    @Mapping(target = "id", source = "plato.id")
    @Mapping(target = "idPlato", source = "plato.id")
    @Mapping(target = "nombre", source = "plato.nombre")
    @Mapping(target = "precio", source = "plato.precio")
    @Mapping(target = "descripcion", source = "plato.descripcion")
    @Mapping(target = "urlImagen", source = "plato.urlImagen")
    @Mapping(target = "categoria", source = "plato.categoria.nombre")
    @Mapping(target = "cantidad", source = "cantidad")
    OrderDishResponseDto toDishResponseDto(OrderDishModel orderDishModel);

    List<OrderDishResponseDto> toDishResponseDtoList(List<OrderDishModel> orderDishModelList);
}
