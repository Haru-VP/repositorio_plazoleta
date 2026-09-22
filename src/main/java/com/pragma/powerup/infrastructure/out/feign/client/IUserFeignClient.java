package com.pragma.powerup.infrastructure.out.feign.client;

import com.pragma.powerup.infrastructure.out.feign.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-usuarios", url = "${microservices.usuarios.url}")
public interface IUserFeignClient {

    @GetMapping("/api/v1/user/{id}")
    UserResponseDto obtenerUsuarioPorId(@PathVariable("id") Long id);
}
