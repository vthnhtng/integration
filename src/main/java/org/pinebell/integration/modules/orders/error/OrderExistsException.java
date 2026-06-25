package org.pinebell.integration.modules.orders.error;

public class OrderExistsException extends RuntimeException {

    public OrderExistsException() {
        super("Order already exists");
    }

    public OrderExistsException(String message) {
        super(message);
    }

    public OrderExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderExistsException(Throwable cause) {
        super(cause);
    }
}
