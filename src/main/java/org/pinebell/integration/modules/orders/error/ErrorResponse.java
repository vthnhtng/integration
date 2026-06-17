package org.pinebell.integration.modules.orders.error;

public record ErrorResponse(
    String path,
    String message
) {}
