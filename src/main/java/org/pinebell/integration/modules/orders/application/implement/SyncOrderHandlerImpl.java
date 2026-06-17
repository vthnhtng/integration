package org.pinebell.integration.modules.orders.application.implement;

import org.pinebell.integration.modules.orders.application.SyncOrderHandler;
import org.pinebell.integration.modules.orders.domain.Order;
import org.pinebell.integration.modules.orders.dto.SyncOrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SyncOrderHandlerImpl implements SyncOrderHandler {

    @Override
    @Transactional
    public Order execute(SyncOrderRequest request) {
        return new Order();
    }
}
