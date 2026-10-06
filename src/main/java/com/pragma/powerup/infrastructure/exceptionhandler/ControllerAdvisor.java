package com.pragma.powerup.infrastructure.exceptionhandler;

import com.pragma.powerup.domain.exception.CantidadPlatoInvalidaException;
import com.pragma.powerup.domain.exception.CategoriaNoEncontradaException;
import com.pragma.powerup.domain.exception.ClienteTienePedidoActivoException;
import com.pragma.powerup.domain.exception.NitInvalidoException;
import com.pragma.powerup.domain.exception.NitYaExisteException;
import com.pragma.powerup.domain.exception.NombreInvalidoException;
import com.pragma.powerup.domain.exception.ParametrosPaginacionInvalidosException;
import com.pragma.powerup.domain.exception.PedidoNoEncontradoException;
import com.pragma.powerup.domain.exception.PedidoNoEstaEnEstadoPendienteException;
import com.pragma.powerup.domain.exception.PedidoNoEstaEnPreparacionException;
import com.pragma.powerup.domain.exception.PedidoNoEstaListoException;
import com.pragma.powerup.domain.exception.PedidoNoSePuedeCancelarException;
import com.pragma.powerup.domain.exception.PinInvalidoException;
import com.pragma.powerup.domain.exception.PedidoSinPlatosException;
import com.pragma.powerup.domain.exception.PlatoInactivoException;
import com.pragma.powerup.domain.exception.PlatoNoEncontradoException;
import com.pragma.powerup.domain.exception.PlatosDeDiferenteRestauranteException;
import com.pragma.powerup.domain.exception.PrecioInvalidoException;
import com.pragma.powerup.domain.exception.RestauranteNoEncontradoException;
import com.pragma.powerup.domain.exception.TelefonoInvalidoException;
import com.pragma.powerup.domain.exception.UsuarioNoAutorizadoException;
import com.pragma.powerup.domain.exception.UsuarioNoEsPropietarioException;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

    private static final String MESSAGE = "message";

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoDataFoundException(
            NoDataFoundException ignoredNoDataFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.NO_DATA_FOUND.getMessage()));
    }

    @ExceptionHandler(NombreInvalidoException.class)
    public ResponseEntity<Map<String, String>> handleNombreInvalidoException(
            NombreInvalidoException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.NOMBRE_INVALIDO.getMessage()));
    }

    @ExceptionHandler(NitInvalidoException.class)
    public ResponseEntity<Map<String, String>> handleNitInvalidoException(
            NitInvalidoException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.NIT_INVALIDO.getMessage()));
    }

    @ExceptionHandler(TelefonoInvalidoException.class)
    public ResponseEntity<Map<String, String>> handleTelefonoInvalidoException(
            TelefonoInvalidoException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.TELEFONO_INVALIDO.getMessage()));
    }

    @ExceptionHandler(UsuarioNoEsPropietarioException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioNoEsPropietarioException(
            UsuarioNoEsPropietarioException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.USUARIO_NO_ES_PROPIETARIO.getMessage()));
    }

    @ExceptionHandler(UsuarioNoAutorizadoException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioNoAutorizadoException(
            UsuarioNoAutorizadoException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.USUARIO_NO_AUTORIZADO.getMessage()));
    }

    @ExceptionHandler(NitYaExisteException.class)
    public ResponseEntity<Map<String, String>> handleNitYaExisteException(
            NitYaExisteException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.NIT_YA_EXISTE.getMessage()));
    }

    @ExceptionHandler(PrecioInvalidoException.class)
    public ResponseEntity<Map<String, String>> handlePrecioInvalidoException(
            PrecioInvalidoException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PRECIO_INVALIDO.getMessage()));
    }

    @ExceptionHandler(RestauranteNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleRestauranteNoEncontradoException(
            RestauranteNoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.RESTAURANTE_NO_ENCONTRADO.getMessage()));
    }

    @ExceptionHandler(CategoriaNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handleCategoriaNoEncontradaException(
            CategoriaNoEncontradaException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CATEGORIA_NO_ENCONTRADA.getMessage()));
    }

    @ExceptionHandler(PlatoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handlePlatoNoEncontradoException(
            PlatoNoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PLATO_NO_ENCONTRADO.getMessage()));
    }

    @ExceptionHandler(ParametrosPaginacionInvalidosException.class)
    public ResponseEntity<Map<String, String>> handleParametrosPaginacionInvalidosException(
            ParametrosPaginacionInvalidosException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PARAMETROS_PAGINACION_INVALIDOS.getMessage()));
    }

    @ExceptionHandler(ClienteTienePedidoActivoException.class)
    public ResponseEntity<Map<String, String>> handleClienteTienePedidoActivoException(
            ClienteTienePedidoActivoException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CLIENTE_TIENE_PEDIDO_ACTIVO.getMessage()));
    }

    @ExceptionHandler(PedidoSinPlatosException.class)
    public ResponseEntity<Map<String, String>> handlePedidoSinPlatosException(
            PedidoSinPlatosException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PEDIDO_SIN_PLATOS.getMessage()));
    }

    @ExceptionHandler(CantidadPlatoInvalidaException.class)
    public ResponseEntity<Map<String, String>> handleCantidadPlatoInvalidaException(
            CantidadPlatoInvalidaException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CANTIDAD_PLATO_INVALIDA.getMessage()));
    }

    @ExceptionHandler(PlatosDeDiferenteRestauranteException.class)
    public ResponseEntity<Map<String, String>> handlePlatosDeDiferenteRestauranteException(
            PlatosDeDiferenteRestauranteException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PLATOS_DIFERENTE_RESTAURANTE.getMessage()));
    }

    @ExceptionHandler(PlatoInactivoException.class)
    public ResponseEntity<Map<String, String>> handlePlatoInactivoException(
            PlatoInactivoException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PLATO_INACTIVO.getMessage()));
    }

    @ExceptionHandler(PedidoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handlePedidoNoEncontradoException(
            PedidoNoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PEDIDO_NO_ENCONTRADO.getMessage()));
    }

    @ExceptionHandler(PedidoNoEstaEnEstadoPendienteException.class)
    public ResponseEntity<Map<String, String>> handlePedidoNoEstaEnEstadoPendienteException(
            PedidoNoEstaEnEstadoPendienteException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PEDIDO_NO_PENDIENTE.getMessage()));
    }

    @ExceptionHandler(PedidoNoEstaEnPreparacionException.class)
    public ResponseEntity<Map<String, String>> handlePedidoNoEstaEnPreparacionException(
            PedidoNoEstaEnPreparacionException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PEDIDO_NO_EN_PREPARACION.getMessage()));
    }

    @ExceptionHandler(PedidoNoEstaListoException.class)
    public ResponseEntity<Map<String, String>> handlePedidoNoEstaListoException(
            PedidoNoEstaListoException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PEDIDO_NO_ESTA_LISTO.getMessage()));
    }

    @ExceptionHandler(PinInvalidoException.class)
    public ResponseEntity<Map<String, String>> handlePinInvalidoException(
            PinInvalidoException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PIN_INVALIDO.getMessage()));
    }

    @ExceptionHandler(PedidoNoSePuedeCancelarException.class)
    public ResponseEntity<Map<String, String>> handlePedidoNoSePuedeCancelarException(
            PedidoNoSePuedeCancelarException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PEDIDO_NO_SE_PUEDE_CANCELAR.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        String mensaje = exception.getBindingResult().getFieldError() != null
                ? exception.getBindingResult().getFieldError().getDefaultMessage()
                : "Datos de entrada inválidos";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, mensaje));
    }
}