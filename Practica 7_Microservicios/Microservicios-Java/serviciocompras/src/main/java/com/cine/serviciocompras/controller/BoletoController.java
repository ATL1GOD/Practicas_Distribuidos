package com.cine.serviciocompras.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cine.serviciocompras.model.BoletoRequest;
import com.cine.serviciocompras.service.BoletoService;

@RestController
@RequestMapping("/compras")
public class BoletoController {

    private final BoletoService boletoService;

    @Autowired
    public BoletoController(BoletoService boletoService) {
        this.boletoService = boletoService;
    }

    @PostMapping
    public String comprarBoletos(@RequestBody BoletoRequest boletoRequest) {
        return boletoService.procesarCompra(boletoRequest);
    }
}
