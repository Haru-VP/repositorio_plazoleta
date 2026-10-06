package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishModel {
    private Long id;
    private String nombre;
    private CategoryModel categoria;
    private String descripcion;
    private Integer precio;
    private RestaurantModel restaurante;
    private String urlImagen;
    private Boolean activo;
}
