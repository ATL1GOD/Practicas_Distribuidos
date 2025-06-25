package com.cine.apigateway.filter;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * Este filtro global genera un identificador único (X-Correlation-ID)
 * para rastrear cada solicitud que pasa por el gateway del sistema de cine.
 */
@Component
public class CorrelationFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(CorrelationFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String correlationId = UUID.randomUUID().toString();

        exchange.getRequest().mutate()
                .header("X-Correlation-ID", correlationId)
                .build();

        logger.info("🎟️ Correlation ID generado para solicitud de cine: {}", correlationId);

        return chain.filter(exchange);
    }
}
