package org.pinebell.integration.modules.orders.auth;

import java.time.Instant;

public interface SignatureGenerator {

    public String generate(String payload, Instant timestamp);

}
