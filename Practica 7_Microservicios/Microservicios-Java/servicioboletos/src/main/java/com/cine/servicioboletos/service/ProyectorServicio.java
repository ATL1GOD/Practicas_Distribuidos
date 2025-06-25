package com.cine.servicioboletos.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ProyectorServicio {

    private static final Logger logger = LoggerFactory.getLogger(ProyectorServicio.class);

    private final CineServicio cineServicio;

    public ProyectorServicio(CineServicio cineServicio) {
        this.cineServicio = cineServicio;
    }

    // Reponer boletos cada 10 minutos (100000 ms)
    @Scheduled(fixedRate = 100000)
    public void reponerBoletosPeliculas() {
        List<String> peliculas = Arrays.asList(
                "El Padrino", "Titanic", "Inception", "Interestelar",
                "Avengers", "Matrix", "Parasite"
        );

        for (String titulo : peliculas) {
            cineServicio.reponerBoletos(titulo, 50);
            int disponibles = cineServicio.obtenerBoletosDisponibles(titulo);
            logger.info("Proyector reabastecido - Se añadieron 50 boletos para '{}'. Boletos disponibles: {}",
                    50, titulo, disponibles);
        }
    }
}
