package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Pedidos", description = "Endpoints para la gestión del ciclo de vida de los pedidos")
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @Operation(summary = "Realizar un nuevo pedido a un restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido realizado exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos, platos de diferente restaurante o platos inactivos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: se requiere rol CLIENTE", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurante o plato no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "El cliente ya tiene un pedido en proceso (PENDIENTE, EN_PREPARACION o LISTO)", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> realizarPedido(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        orderHandler.realizarPedido(orderRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
