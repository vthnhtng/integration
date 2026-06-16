package org.pinebell.integration.modules.orders.infrastructure;

import org.pinebell.integration.modules.orders.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
