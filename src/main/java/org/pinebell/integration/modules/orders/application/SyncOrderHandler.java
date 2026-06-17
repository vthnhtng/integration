package org.pinebell.integration.modules.orders.application;

import org.pinebell.integration.modules.orders.domain.Order;
import org.pinebell.integration.modules.orders.dto.SyncOrderRequest;

public interface SyncOrderHandler {
    public Order execute(SyncOrderRequest request);
}
