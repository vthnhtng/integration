package org.pinebell.integration.modules.orders.api;

import org.pinebell.integration.modules.orders.dto.SyncOrderRequest;
import org.pinebell.integration.modules.orders.dto.SyncOrderResponse;
import org.pinebell.integration.modules.orders.service.SyncOrderHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {
    private final SyncOrderHandler syncOrderHandler;

    public OrderController(SyncOrderHandler syncOrderHandler) {
        this.syncOrderHandler = syncOrderHandler;
    }

    @PostMapping("/sync")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SyncOrderResponse syncOrder(@Valid @RequestBody SyncOrderRequest request) {

        return syncOrderHandler.execute(request);

    }
}
