package com.pragma.powerup.infrastructure.out.feign.client;

import com.pragma.powerup.infrastructure.out.feign.dto.TrazabilidadFeignRequestDto;
import com.pragma.powerup.infrastructure.out.feign.dto.TrazabilidadFeignResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservicio-trazabilidad", url = "${microservices.trazabilidad.url}")
public interface ITrazabilidadFeignClient {

    @PostMapping("/api/v1/trazabilidad")
    TrazabilidadFeignResponseDto registrarCambioEstado(@RequestBody TrazabilidadFeignRequestDto requestDto);
}
