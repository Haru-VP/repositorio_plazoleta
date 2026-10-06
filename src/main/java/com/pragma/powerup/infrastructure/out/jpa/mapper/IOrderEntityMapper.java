package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.OrderDishModel;
import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderDishEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {IRestaurantEntityMapper.class, IDishEntityMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IOrderEntityMapper {

    OrderEntity toEntity(OrderModel orderModel);

    OrderModel toModel(OrderEntity orderEntity);

    List<OrderModel> toModelList(List<OrderEntity> orderEntityList);

    OrderDishEntity toEntity(OrderDishModel orderDishModel);

    OrderDishModel toModel(OrderDishEntity orderDishEntity);

    @AfterMapping
    default void vincularDetalles(@MappingTarget OrderEntity orderEntity) {
        if (orderEntity.getPedidosPlatos() != null) {
            orderEntity.getPedidosPlatos().forEach(detalle -> detalle.setPedido(orderEntity));
        }
    }
}
