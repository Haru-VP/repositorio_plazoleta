package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.AssignOrderRequestDto;
import com.pragma.powerup.application.dto.request.OrderDishRequestDto;
import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderRestControllerTest {

    @Mock
    private IOrderHandler orderHandler;

    @InjectMocks
    private OrderRestController orderRestController;

    @Test
    void realizarPedido_retornaCreated() {
        // Arrange
        OrderDishRequestDto dishDto = new OrderDishRequestDto(1L, 2);
        OrderRequestDto requestDto = new OrderRequestDto(10L, Collections.singletonList(dishDto));

        // Act
        ResponseEntity<Void> response = orderRestController.realizarPedido(requestDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(orderHandler, times(1)).realizarPedido(requestDto);
    }

    @Test
    void listarPedidos_retornaOkConLista() {
        // Arrange
        OrderResponseDto dto = new OrderResponseDto();
        when(orderHandler.listarPedidosPorEstado("PENDIENTE", 1L, 0, 10))
                .thenReturn(Collections.singletonList(dto));

        // Act
        ResponseEntity<List<OrderResponseDto>> response = orderRestController.listarPedidos("PENDIENTE", 1L, 0, 10);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(orderHandler, times(1)).listarPedidosPorEstado("PENDIENTE", 1L, 0, 10);
    }

    @Test
    void listarPedidosPorRestaurante_retornaOkConLista() {
        // Arrange
        OrderResponseDto dto = new OrderResponseDto();
        when(orderHandler.listarPedidosPorEstado("PENDIENTE", 1L, 0, 10))
                .thenReturn(Collections.singletonList(dto));

        // Act
        ResponseEntity<List<OrderResponseDto>> response = orderRestController.listarPedidosPorRestaurante(1L, "PENDIENTE", 0, 10);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(orderHandler, times(1)).listarPedidosPorEstado("PENDIENTE", 1L, 0, 10);
    }

    @Test
    void asignarPedido_porPath_retornaOkConOrderResponseDto() {
        // Arrange
        Long idPedido = 1L;
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(idPedido);
        dto.setEstado("EN_PREPARACION");
        dto.setIdChef(50L);

        when(orderHandler.asignarPedido(idPedido)).thenReturn(dto);

        // Act
        ResponseEntity<OrderResponseDto> response = orderRestController.asignarPedido(idPedido);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("EN_PREPARACION", response.getBody().getEstado());
        assertEquals(50L, response.getBody().getIdChef());
        verify(orderHandler, times(1)).asignarPedido(idPedido);
    }

    @Test
    void asignarPedidoConBody_retornaOkConOrderResponseDto() {
        // Arrange
        Long idPedido = 1L;
        AssignOrderRequestDto bodyDto = new AssignOrderRequestDto(idPedido, null);
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(idPedido);
        dto.setEstado("EN_PREPARACION");
        dto.setIdChef(50L);

        when(orderHandler.asignarPedido(idPedido)).thenReturn(dto);

        // Act
        ResponseEntity<OrderResponseDto> response = orderRestController.asignarPedidoConBody(bodyDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("EN_PREPARACION", response.getBody().getEstado());
        assertEquals(50L, response.getBody().getIdChef());
        verify(orderHandler, times(1)).asignarPedido(idPedido);
    }

    @Test
    void asignarPedidoConBody_bodyNulo_llamaHandlerConNull() {
        // Arrange
        when(orderHandler.asignarPedido(null)).thenReturn(new OrderResponseDto());

        // Act
        ResponseEntity<OrderResponseDto> response = orderRestController.asignarPedidoConBody(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(orderHandler, times(1)).asignarPedido(null);
    }

    @Test
    void notificarPedidoListo_porPath_retornaOkConOrderResponseDto() {
        // Arrange
        Long idPedido = 1L;
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(idPedido);
        dto.setEstado("LISTO");
        dto.setPin("123456");

        when(orderHandler.notificarPedidoListo(idPedido)).thenReturn(dto);

        // Act
        ResponseEntity<OrderResponseDto> response = orderRestController.notificarPedidoListo(idPedido);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("LISTO", response.getBody().getEstado());
        assertEquals("123456", response.getBody().getPin());
        verify(orderHandler, times(1)).notificarPedidoListo(idPedido);
    }

    @Test
    void notificarPedidoListoConBody_retornaOkConOrderResponseDto() {
        // Arrange
        Long idPedido = 1L;
        com.pragma.powerup.application.dto.request.ReadyOrderRequestDto bodyDto =
                new com.pragma.powerup.application.dto.request.ReadyOrderRequestDto(idPedido, null);
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(idPedido);
        dto.setEstado("LISTO");
        dto.setPin("654321");

        when(orderHandler.notificarPedidoListo(idPedido)).thenReturn(dto);

        // Act
        ResponseEntity<OrderResponseDto> response = orderRestController.notificarPedidoListoConBody(bodyDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("LISTO", response.getBody().getEstado());
        assertEquals("654321", response.getBody().getPin());
        verify(orderHandler, times(1)).notificarPedidoListo(idPedido);
    }

    @Test
    void notificarPedidoListoConBody_bodyNulo_llamaHandlerConNull() {
        // Arrange
        when(orderHandler.notificarPedidoListo(null)).thenReturn(new OrderResponseDto());

        // Act
        ResponseEntity<OrderResponseDto> response = orderRestController.notificarPedidoListoConBody(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(orderHandler, times(1)).notificarPedidoListo(null);
    }

    @Test
    void entregarPedido_porPathYQueryParam_retornaOk() {
        // Arrange
        Long idPedido = 1L;
        String pin = "123456";
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(idPedido);
        dto.setEstado("ENTREGADO");

        when(orderHandler.entregarPedido(idPedido, pin)).thenReturn(dto);

        // Act
        ResponseEntity<OrderResponseDto> response = orderRestController.entregarPedido(idPedido, pin, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ENTREGADO", response.getBody().getEstado());
        verify(orderHandler, times(1)).entregarPedido(idPedido, pin);
    }

    @Test
    void entregarPedido_porPathYBody_retornaOk() {
        // Arrange
        Long idPedido = 1L;
        com.pragma.powerup.application.dto.request.DeliverOrderRequestDto bodyDto =
                new com.pragma.powerup.application.dto.request.DeliverOrderRequestDto(null, null, "123456");
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(idPedido);
        dto.setEstado("ENTREGADO");

        when(orderHandler.entregarPedido(idPedido, "123456")).thenReturn(dto);

        // Act
        ResponseEntity<OrderResponseDto> response = orderRestController.entregarPedido(idPedido, null, bodyDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ENTREGADO", response.getBody().getEstado());
        verify(orderHandler, times(1)).entregarPedido(idPedido, "123456");
    }

    @Test
    void entregarPedidoConBody_retornaOk() {
        // Arrange
        Long idPedido = 1L;
        com.pragma.powerup.application.dto.request.DeliverOrderRequestDto bodyDto =
                new com.pragma.powerup.application.dto.request.DeliverOrderRequestDto(idPedido, null, "123456");
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(idPedido);
        dto.setEstado("ENTREGADO");

        when(orderHandler.entregarPedido(idPedido, "123456")).thenReturn(dto);

        // Act
        ResponseEntity<OrderResponseDto> response = orderRestController.entregarPedidoConBody(bodyDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ENTREGADO", response.getBody().getEstado());
        verify(orderHandler, times(1)).entregarPedido(idPedido, "123456");
    }

    @Test
    void entregarPedidoConBody_bodyNulo_llamaHandlerConNull() {
        // Arrange
        when(orderHandler.entregarPedido(null, null)).thenReturn(new OrderResponseDto());

        // Act
        ResponseEntity<OrderResponseDto> response = orderRestController.entregarPedidoConBody(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(orderHandler, times(1)).entregarPedido(null, null);
    }
}
