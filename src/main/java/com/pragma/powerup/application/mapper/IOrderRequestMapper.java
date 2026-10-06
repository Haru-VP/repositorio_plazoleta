package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.request.OrderDishRequestDto;
import com.pragma.powerup.application.dto.request.OrderRequestDto;
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
public interface IOrderRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idCliente", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "idChef", ignore = true)
    @Mapping(target = "restaurante.id", source = "idRestaurante")
    @Mapping(target = "pedidosPlatos", source = "platos")
    OrderModel toOrderModel(OrderRequestDto orderRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    @Mapping(target = "plato.id", source = "idPlato")
    @Mapping(target = "cantidad", source = "cantidad")
    OrderDishModel toOrderDishModel(OrderDishRequestDto orderDishRequestDto);

    List<OrderDishModel> toOrderDishModelList(List<OrderDishRequestDto> orderDishRequestDtoList);
}
