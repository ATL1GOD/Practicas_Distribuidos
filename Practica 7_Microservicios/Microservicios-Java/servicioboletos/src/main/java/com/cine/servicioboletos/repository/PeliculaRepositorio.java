package com.cine.servicioboletos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cine.servicioboletos.model.Pelicula;

public interface PeliculaRepositorio extends JpaRepository<Pelicula, Long> {
    Pelicula findByTitulo(String titulo);
}
