package com.cine.servicioboletos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cine.servicioboletos.model.Pelicula;
import com.cine.servicioboletos.service.CineServicio;

@RestController
@RequestMapping("/inventario")
public class CineControlador {

    @Autowired
    private CineServicio cineServicio;

    @GetMapping("/peliculas")
    public List<Pelicula> obtenerPeliculas() {
        return cineServicio.obtenerPeliculas();
    }

    @GetMapping("/boletos/{titulo}")
    public int obtenerBoletosDisponibles(@PathVariable String titulo) {
        return cineServicio.obtenerBoletosDisponibles(titulo);
    }

    @PutMapping("/comprar/{titulo}/{cantidad}")
    public String comprarBoletos(@PathVariable String titulo, @PathVariable int cantidad) {
        boolean exito = cineServicio.comprarBoletos(titulo, cantidad);
        return exito ? "Compra realizada exitosamente" : "No hay suficientes boletos disponibles";
    }
}
