package com.cine.serviciocompras.model;

public class Boleto {
    private String pelicula;
    private int cantidad;

    public Boleto() {}

    public String getPelicula() {
        return pelicula;
    }

    public void setPelicula(String pelicula) {
        this.pelicula = pelicula;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
