package org.pinebell.integration.modules.orders.service;

import org.pinebell.integration.modules.orders.dto.SyncOrderRequest;
import org.pinebell.integration.modules.orders.dto.SyncOrderResponse;

public interface SyncOrderHandler {
    SyncOrderResponse execute(SyncOrderRequest request);
}
