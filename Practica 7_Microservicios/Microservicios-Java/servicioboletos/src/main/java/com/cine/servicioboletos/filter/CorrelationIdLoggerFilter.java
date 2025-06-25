package com.cine.servicioboletos.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CorrelationIdLoggerFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CorrelationIdLoggerFilter.class);
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest) {
            String correlationId = httpRequest.getHeader(CORRELATION_ID_HEADER);
            if (correlationId != null) {
                logger.info("Recibido X-Correlation-ID: {}", correlationId);
            }
        }

        chain.doFilter(request, response);
    }
}
