package com.pragma.powerup.infrastructure.exceptionhandler;

import com.pragma.powerup.domain.exception.CategoriaNoEncontradaException;
import com.pragma.powerup.domain.exception.NitInvalidoException;
import com.pragma.powerup.domain.exception.NitYaExisteException;
import com.pragma.powerup.domain.exception.NombreInvalidoException;
import com.pragma.powerup.domain.exception.PlatoNoEncontradoException;
import com.pragma.powerup.domain.exception.PrecioInvalidoException;
import com.pragma.powerup.domain.exception.RestauranteNoEncontradoException;
import com.pragma.powerup.domain.exception.TelefonoInvalidoException;
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