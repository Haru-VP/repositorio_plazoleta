package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.NitInvalidoException;
import com.pragma.powerup.domain.exception.NitYaExisteException;
import com.pragma.powerup.domain.exception.NombreInvalidoException;
import com.pragma.powerup.domain.exception.TelefonoInvalidoException;
import com.pragma.powerup.domain.exception.UsuarioNoEsPropietarioException;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserValidationServicePort;

import java.util.regex.Pattern;

public class RestaurantUseCase implements IRestaurantServicePort {

    private static final int LONGITUD_MAXIMA_TELEFONO = 13;
    private static final Pattern PATRON_SOLO_NUMEROS = Pattern.compile("^[0-9]+$");
    private static final Pattern PATRON_TELEFONO = Pattern.compile("^\\+?[0-9]+$");

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserValidationServicePort userValidationServicePort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort,
                             IUserValidationServicePort userValidationServicePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userValidationServicePort = userValidationServicePort;
    }

    @Override
    public void guardarRestaurante(RestaurantModel restaurantModel) {
        validarNombre(restaurantModel.getNombre());
        validarNit(restaurantModel.getNit());
        validarTelefono(restaurantModel.getTelefono());
        validarPropietario(restaurantModel.getIdPropietario());
        validarUnicidadNit(restaurantModel.getNit());

        restaurantPersistencePort.guardarRestaurante(restaurantModel);
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty() || PATRON_SOLO_NUMEROS.matcher(nombre.trim()).matches()) {
            throw new NombreInvalidoException();
        }
    }

    private void validarNit(String nit) {
        if (nit == null || !PATRON_SOLO_NUMEROS.matcher(nit.trim()).matches()) {
            throw new NitInvalidoException();
        }
    }

    private void validarTelefono(String telefono) {
        if (telefono == null || telefono.length() > LONGITUD_MAXIMA_TELEFONO || !PATRON_TELEFONO.matcher(telefono).matches()) {
            throw new TelefonoInvalidoException();
        }
    }

    private void validarPropietario(Long idPropietario) {
        if (idPropietario == null || !userValidationServicePort.esPropietario(idPropietario)) {
            throw new UsuarioNoEsPropietarioException();
        }
    }

    private void validarUnicidadNit(String nit) {
        if (restaurantPersistencePort.existePorNit(nit)) {
            throw new NitYaExisteException();
        }
    }
}
