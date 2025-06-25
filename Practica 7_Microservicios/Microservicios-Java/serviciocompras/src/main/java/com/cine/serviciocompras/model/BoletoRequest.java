package com.cine.serviciocompras.model;

import java.util.List;



public class BoletoRequest {
    private String nombreCliente;
    private List<Boleto> boletos;

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public List<Boleto> getBoletos() {
        return boletos;
    }

    public void setBoletos(List<Boleto> boletos) {
        this.boletos = boletos;
    }
}
