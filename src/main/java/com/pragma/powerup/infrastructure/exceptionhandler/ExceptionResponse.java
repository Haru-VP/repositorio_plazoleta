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
    CATEGORIA_NO_ENCONTRADA("La categoría especificada no existe en el sistema"),
    PLATO_NO_ENCONTRADO("El plato especificado no existe en el sistema"),
    USUARIO_NO_AUTORIZADO("El usuario autenticado no tiene permisos para realizar esta acción sobre este restaurante"),
    PARAMETROS_PAGINACION_INVALIDOS("Los parámetros de paginación deben ser válidos (page >= 0 y size > 0)"),
    CLIENTE_TIENE_PEDIDO_ACTIVO("El cliente ya tiene un pedido en proceso (PENDIENTE, EN_PREPARACION o LISTO)"),
    PEDIDO_SIN_PLATOS("El pedido debe contener al menos un plato"),
    CANTIDAD_PLATO_INVALIDA("La cantidad de cada plato debe ser un número entero mayor a cero"),
    PLATOS_DIFERENTE_RESTAURANTE("Todos los platos del pedido deben pertenecer al mismo restaurante"),
    PLATO_INACTIVO("Uno o más platos seleccionados no se encuentran activos en el restaurante"),
    PEDIDO_NO_ENCONTRADO("El pedido especificado no existe en el sistema"),
    PEDIDO_NO_PENDIENTE("Solo se pueden asignar pedidos que se encuentren en estado PENDIENTE"),
    PEDIDO_NO_EN_PREPARACION("Solo se pueden marcar como listos los pedidos que se encuentren en preparación"),
    PEDIDO_NO_ESTA_LISTO("Solo se pueden entregar pedidos que se encuentren en estado LISTO"),
    PIN_INVALIDO("El PIN de seguridad proporcionado es incorrecto o inválido");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}