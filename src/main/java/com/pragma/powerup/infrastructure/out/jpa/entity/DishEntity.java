package com.pragma.powerup.infrastructure.out.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "platos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoryEntity categoria;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Integer precio;

    @ManyToOne
    @JoinColumn(name = "id_restaurante", nullable = false)
    private RestaurantEntity restaurante;

    @Column(name = "url_imagen", nullable = false)
    private String urlImagen;

    @Column(nullable = false)
    private Boolean activo;
}
