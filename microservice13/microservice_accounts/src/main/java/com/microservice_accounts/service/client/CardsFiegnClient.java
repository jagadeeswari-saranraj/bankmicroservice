package com.microservice_accounts.service.client;

import com.microservice_accounts.dto.CardsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cards", url="http://cards:9001", fallback = CardsFallback.class)
public interface CardsFiegnClient {

    @GetMapping(value="/api/fetch", consumes = "application/json")
    public ResponseEntity<CardsDTO> fetchCardDetails(@RequestHeader("jagabank-correlation-id") String correlationId, @RequestParam String mobileNumber);

}
