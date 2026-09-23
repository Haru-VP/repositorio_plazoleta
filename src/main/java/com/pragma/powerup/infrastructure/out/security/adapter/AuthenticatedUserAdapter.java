package com.pragma.powerup.infrastructure.out.security.adapter;

import com.pragma.powerup.domain.spi.IAuthenticatedUserPort;
import com.pragma.powerup.infrastructure.out.security.model.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserAdapter implements IAuthenticatedUserPort {

    @Override
    public Long obtenerIdUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser) {
            return ((AuthenticatedUser) authentication.getPrincipal()).getId();
        }
        return null;
    }

    @Override
    public String obtenerRolUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser) {
            return ((AuthenticatedUser) authentication.getPrincipal()).getRol();
        }
        return null;
    }
}
