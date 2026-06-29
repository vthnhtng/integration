package org.pinebell.integration.modules.orders.auth;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SignatureBasedFilter extends OncePerRequestFilter {

    private final SignatureGenerator signatureGenerator;

    public SignatureBasedFilter(SignatureGenerator signatureGenerator) {
        this.signatureGenerator = signatureGenerator;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String signature = request.getHeader("X-Signature");
        String timestampStr = request.getHeader("X-Timestamp");

        if (signature == null || timestampStr == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing authentication headers");
            return;
        }

        Instant timestamp = null;
        try {
            long epochMillis = Long.parseLong(timestampStr);
            timestamp = Instant.ofEpochMilli(epochMillis);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid timestamp format");
            return;
        }

        String payload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        String generatedSignature = signatureGenerator.generate(payload, timestamp);

        if (!signature.equals(generatedSignature)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid signature");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
