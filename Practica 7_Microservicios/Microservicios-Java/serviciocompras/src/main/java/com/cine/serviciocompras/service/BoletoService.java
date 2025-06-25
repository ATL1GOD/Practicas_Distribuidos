package com.cine.serviciocompras.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cine.serviciocompras.model.Boleto;
import com.cine.serviciocompras.model.BoletoRequest;

@Service
public class BoletoService {

    private static final Logger logger = LoggerFactory.getLogger(BoletoService.class);

    private final RestTemplate restTemplate;
    private final LoadBalancerClient loadBalancerClient; // Inyectar LoadBalancerClient
    
    private final String INVENTARIO_URL = "http://servicioboletos/inventario";

    public BoletoService(RestTemplate restTemplate, LoadBalancerClient loadBalancerClient) {
        this.restTemplate = restTemplate;
        this.loadBalancerClient = loadBalancerClient;
    }

    public String procesarCompra(BoletoRequest boletoRequest) {
        StringBuilder resultado = new StringBuilder("Resultados de compra:\n");

        for (Boleto boleto : boletoRequest.getBoletos()) {
            String pelicula = boleto.getPelicula();
            int boletosDeseados = boleto.getCantidad();

            // Log de selección del servidor por Ribbon
            ServiceInstance instance = loadBalancerClient.choose("serviciocompras");
            if (instance != null) {
                logger.info("Load balancer for 'serviciocompras' chose server: {}:{}", instance.getHost(), instance.getPort());
            } else {
                logger.error("No available instances for 'serviciocompras' found!");
            }

            // Obtener disponibilidad actual desde cartelera-service
            Integer boletosDisponibles = restTemplate.getForObject(
                    INVENTARIO_URL + "/disponibles/" + pelicula,
                    Integer.class
            );

            if (boletosDisponibles != null && boletosDisponibles >= boletosDeseados) {
                // Realizar la compra
                restTemplate.put(
                        INVENTARIO_URL + "/comprar/" + pelicula + "/" + boletosDeseados,
                        null
                );

                // Consultar boletos restantes después de la compra
                Integer boletosRestantes = restTemplate.getForObject(
                        INVENTARIO_URL + "/disponibles/" + pelicula,
                        Integer.class
                );

                logger.info("Cliente '{}' compró {} boletos para '{}'. Disponibles antes: {}, después: {}",
                        boletoRequest.getNombreCliente(), boletosDeseados, pelicula, boletosDisponibles, boletosRestantes);

                resultado.append("Compra de ").append(boletosDeseados)
                        .append(" boletos para ").append(pelicula).append(" realizada con éxito. ")
                        .append("Boletos restantes: ").append(boletosRestantes).append("\n");

            } else {
                logger.warn("Cliente '{}' intentó comprar {} boletos para '{}', pero la disponibilidad era de {}.",
                        boletoRequest.getNombreCliente(), boletosDeseados, pelicula,
                        boletosDisponibles != null ? boletosDisponibles : "desconocido");

                resultado.append("Boletos insuficientes para ").append(pelicula).append(".\n");
            }
        }

        return resultado.toString();
    }
}
