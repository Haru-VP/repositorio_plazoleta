package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.DishRequestDto;
import com.pragma.powerup.application.dto.request.UpdateDishRequestDto;
import com.pragma.powerup.application.handler.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Platos", description = "Endpoints para la creación, configuración y gestión de platos")
@RestController
@RequestMapping("/api/v1/dish")
@RequiredArgsConstructor
public class DishRestController {

    private final IDishHandler dishHandler;

    @Operation(summary = "Crear un nuevo plato asociado a un restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plato creado exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o precio menor/igual a cero", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: solo el propietario del restaurante puede crear platos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurante o categoría no encontrados", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> guardarPlato(@Valid @RequestBody DishRequestDto dishRequestDto) {
        dishHandler.guardarPlato(dishRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Modificar el precio y la descripción de un plato existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato modificado exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o precio menor/igual a cero", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: solo el propietario del restaurante puede modificar platos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Plato no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarPlato(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDishRequestDto updateDishRequestDto) {
        dishHandler.actualizarPlato(id, updateDishRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
