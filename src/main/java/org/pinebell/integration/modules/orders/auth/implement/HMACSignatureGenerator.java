package org.pinebell.integration.modules.orders.auth.implement;

import java.time.Instant;
import java.util.HexFormat;

import org.pinebell.integration.modules.orders.auth.SignatureGenerator;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;

@Component
public class HMACSignatureGenerator implements SignatureGenerator {

    private final SecretKeySpec secretKeySpec;

    public HMACSignatureGenerator(@Value("${spring.security.secret}") String key) {
        try {
            this.secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize secret key", e);
        }
    }

    @Override
    public String generate(String payload, Instant timestamp) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);

            String data = timestamp.toEpochMilli() + "." + payload;
            byte[] hash = mac.doFinal(data.getBytes("UTF-8"));

            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate signature", e);
        }
    }
}
