package com.pragma.powerup.infrastructure.out.feign.client;

import com.pragma.powerup.infrastructure.out.feign.dto.SmsFeignRequestDto;
import com.pragma.powerup.infrastructure.out.feign.dto.SmsFeignResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservicio-mensajeria", url = "${microservices.mensajeria.url}")
public interface IMensajeriaFeignClient {

    @PostMapping("/api/v1/sms/notify")
    SmsFeignResponseDto notificarSms(@RequestBody SmsFeignRequestDto requestDto);
}
