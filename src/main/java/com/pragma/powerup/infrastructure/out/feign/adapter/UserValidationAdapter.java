package com.pragma.powerup.infrastructure.out.feign.adapter;

import com.pragma.powerup.domain.spi.IUserValidationServicePort;
import com.pragma.powerup.infrastructure.out.feign.client.IUserFeignClient;
import com.pragma.powerup.infrastructure.out.feign.dto.UserResponseDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserValidationAdapter implements IUserValidationServicePort {

    private static final String ROL_PROPIETARIO = "PROPIETARIO";
    private final IUserFeignClient userFeignClient;

    @Override
    public boolean esPropietario(Long idPropietario) {
        try {
            UserResponseDto userResponseDto = userFeignClient.obtenerUsuarioPorId(idPropietario);
            return userResponseDto != null
                    && userResponseDto.getRol() != null
                    && ROL_PROPIETARIO.equalsIgnoreCase(userResponseDto.getRol().getNombre());
        } catch (FeignException.NotFound e) {
            return false;
        } catch (FeignException e) {
            return false;
        }
    }
}
