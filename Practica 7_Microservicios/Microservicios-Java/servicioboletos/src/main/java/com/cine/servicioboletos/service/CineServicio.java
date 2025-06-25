package com.cine.servicioboletos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cine.servicioboletos.model.Pelicula;
import com.cine.servicioboletos.repository.PeliculaRepositorio;

@Service
public class CineServicio {

    @Autowired
    private PeliculaRepositorio peliculaRepositorio;

    public List<Pelicula> obtenerPeliculas() {
        return peliculaRepositorio.findAll();
    }

    public int obtenerBoletosDisponibles(String titulo) {
        Pelicula pelicula = peliculaRepositorio.findByTitulo(titulo);
        if (pelicula != null) {
            return pelicula.getBoletosDisponibles();
        } else {
            return 0;
        }
    }

    public boolean comprarBoletos(String titulo, int cantidad) {
        Pelicula pelicula = peliculaRepositorio.findByTitulo(titulo);
        if (pelicula != null && pelicula.getBoletosDisponibles() >= cantidad) {
            pelicula.setBoletosDisponibles(pelicula.getBoletosDisponibles() - cantidad);
            peliculaRepositorio.save(pelicula);
            return true;
        } else {
            return false;
        }
    }

    public void reponerBoletos(String titulo, int cantidad) {
        Pelicula pelicula = peliculaRepositorio.findByTitulo(titulo);
        if (pelicula != null) {
            pelicula.setBoletosDisponibles(pelicula.getBoletosDisponibles() + cantidad);
            peliculaRepositorio.save(pelicula);
        }
    }
}
