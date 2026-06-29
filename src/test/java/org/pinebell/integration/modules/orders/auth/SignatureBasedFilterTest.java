package org.pinebell.integration.modules.orders.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.io.BufferedReader;
import java.io.StringReader;

import static org.mockito.Mockito.*;

class SignatureBasedFilterTest {

    private SignatureBasedFilter filter;
    private SignatureGenerator signatureGenerator;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        signatureGenerator = mock(SignatureGenerator.class);
        filter = new SignatureBasedFilter(signatureGenerator);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testMissingHeaders_Returns401() throws Exception {
        when(request.getHeader("X-Signature")).thenReturn(null);
        when(request.getHeader("X-Timestamp")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testInvalidTimestamp_Returns401() throws Exception {
        when(request.getHeader("X-Signature")).thenReturn("some-sig");
        when(request.getHeader("X-Timestamp")).thenReturn("invalid-time");

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testValidSignature_CallsFilterChain() throws Exception {
        Instant now = Instant.now();
        when(request.getHeader("X-Signature")).thenReturn("valid-sig");
        when(request.getHeader("X-Timestamp")).thenReturn(String.valueOf(now.toEpochMilli()));

        // Mock request body reading
        BufferedReader reader = new BufferedReader(new StringReader("{\"orderId\":123}"));
        when(request.getReader()).thenReturn(reader);

        when(signatureGenerator.generate(eq("{\"orderId\":123}"), argThat(instant -> instant.toEpochMilli() == now.toEpochMilli()))).thenReturn("valid-sig");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testInvalidSignature_Returns401() throws Exception {
        Instant now = Instant.now();
        when(request.getHeader("X-Signature")).thenReturn("invalid-sig");
        when(request.getHeader("X-Timestamp")).thenReturn(String.valueOf(now.toEpochMilli()));

        // Mock request body reading
        BufferedReader reader = new BufferedReader(new StringReader("{\"orderId\":123}"));
        when(request.getReader()).thenReturn(reader);

        when(signatureGenerator.generate(eq("{\"orderId\":123}"), argThat(instant -> instant.toEpochMilli() == now.toEpochMilli()))).thenReturn("different-sig");

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }
}
