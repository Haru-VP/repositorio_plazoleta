package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.AssignOrderRequestDto;
import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

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

    @Operation(summary = "Listar pedidos filtrando por estado y paginación (Solo Empleados)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: se requiere rol EMPLEADO", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurante no encontrado", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> listarPedidos(
            @RequestParam(required = false, defaultValue = "PENDIENTE") String estado,
            @RequestParam(required = false) Long idRestaurante,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderHandler.listarPedidosPorEstado(estado, idRestaurante, page, size));
    }

    @Operation(summary = "Listar pedidos de un restaurante específico filtrando por estado y paginación (Solo Empleados)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: se requiere rol EMPLEADO", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurante no encontrado", content = @Content)
    })
    @GetMapping("/restaurante/{idRestaurante}")
    public ResponseEntity<List<OrderResponseDto>> listarPedidosPorRestaurante(
            @PathVariable Long idRestaurante,
            @RequestParam(required = false, defaultValue = "PENDIENTE") String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderHandler.listarPedidosPorEstado(estado, idRestaurante, page, size));
    }

    @Operation(summary = "Asignarse a un pedido y cambiar su estado a EN_PREPARACION (Solo Empleados)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido asignado exitosamente y cambiado a EN_PREPARACION"),
            @ApiResponse(responseCode = "400", description = "El pedido no se encuentra en estado PENDIENTE o datos inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: se requiere rol EMPLEADO o no pertenece al restaurante", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto: el pedido no se encuentra en estado PENDIENTE", content = @Content)
    })
    @PutMapping(value = {"/{id}/asignar", "/{id}/preparacion", "/asignar/{id}"})
    @PatchMapping(value = {"/{id}/asignar", "/{id}/preparacion", "/asignar/{id}"})
    public ResponseEntity<OrderResponseDto> asignarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(orderHandler.asignarPedido(id));
    }

    @Operation(summary = "Asignarse a un pedido mediante body y cambiar su estado a EN_PREPARACION (Solo Empleados)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido asignado exitosamente y cambiado a EN_PREPARACION"),
            @ApiResponse(responseCode = "400", description = "El pedido no se encuentra en estado PENDIENTE o datos inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: se requiere rol EMPLEADO o no pertenece al restaurante", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto: el pedido no se encuentra en estado PENDIENTE", content = @Content)
    })
    @PutMapping("/asignar")
    @PatchMapping("/asignar")
    public ResponseEntity<OrderResponseDto> asignarPedidoConBody(@RequestBody(required = false) AssignOrderRequestDto assignOrderRequestDto) {
        Long id = assignOrderRequestDto != null ? assignOrderRequestDto.getIdPedidoEfectivo() : null;
        return ResponseEntity.ok(orderHandler.asignarPedido(id));
    }
}
