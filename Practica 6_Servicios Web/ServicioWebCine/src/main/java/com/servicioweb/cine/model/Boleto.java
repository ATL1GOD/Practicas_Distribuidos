package com.servicioweb.cine.model;

public class Boleto {
    private String cliente;
    private int cantidad;

    public Boleto() {}

    public Boleto(String cliente, int cantidad) {
        this.cliente = cliente;
        this.cantidad = cantidad;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
