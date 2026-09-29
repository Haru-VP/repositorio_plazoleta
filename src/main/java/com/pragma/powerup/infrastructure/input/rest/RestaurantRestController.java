package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Restaurantes", description = "Endpoints para la creación, configuración y gestión de restaurantes")
@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;

    @Operation(summary = "Crear un nuevo restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante creado exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o reglas de negocio no cumplidas", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: se requiere rol ADMINISTRADOR", content = @Content),
            @ApiResponse(responseCode = "409", description = "El NIT del restaurante ya se encuentra registrado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> guardarRestaurante(@Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
        restaurantHandler.guardarRestaurante(restaurantRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Listar restaurantes ordenados alfabéticamente de forma paginada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de restaurantes obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: se requiere rol CLIENTE", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<RestaurantResponseDto>> listarRestaurantes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(restaurantHandler.listarRestaurantes(page, size));
    }
}
