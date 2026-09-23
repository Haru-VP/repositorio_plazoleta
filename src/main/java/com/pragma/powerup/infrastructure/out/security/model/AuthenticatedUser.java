package com.pragma.powerup.infrastructure.out.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedUser {
    private Long id;
    private String correo;
    private String rol;
}
