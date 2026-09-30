package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {
    private Long id;
    private Long idCliente;
    private LocalDate fecha;
    private String estado;
    private Long idChef;
    private RestaurantModel restaurante;
    private List<OrderDishModel> pedidosPlatos;
}
