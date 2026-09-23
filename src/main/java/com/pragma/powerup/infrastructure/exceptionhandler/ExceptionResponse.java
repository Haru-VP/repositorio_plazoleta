package com.pragma.powerup.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    NO_DATA_FOUND("No data found for the requested petition"),
    NOMBRE_INVALIDO("El nombre del restaurante no puede estar compuesto únicamente por números"),
    NIT_INVALIDO("El NIT del restaurante debe ser únicamente numérico"),
    TELEFONO_INVALIDO("El teléfono del restaurante no es válido o supera los 13 caracteres"),
    USUARIO_NO_ES_PROPIETARIO("El usuario asignado no existe o no cuenta con el rol de propietario"),
    NIT_YA_EXISTE("Ya existe un restaurante registrado con ese NIT"),
    PRECIO_INVALIDO("El precio del plato debe ser un número entero positivo mayor a cero"),
    RESTAURANTE_NO_ENCONTRADO("El restaurante especificado no existe en el sistema"),
    CATEGORIA_NO_ENCONTRADA("La categoría especificada no existe en el sistema");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}