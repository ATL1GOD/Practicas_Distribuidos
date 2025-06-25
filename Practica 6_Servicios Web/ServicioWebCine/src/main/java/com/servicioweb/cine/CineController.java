package com.servicioweb.cine;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.servicioweb.cine.model.Boleto; 

@CrossOrigin(origins = "*") // Permitir solicitudes desde cualquier origen
@RestController
@RequestMapping("/cine")
public class CineController {
    private final CineService cineService;

    public CineController(CineService cineService) {
        this.cineService = cineService;
    }
    
    @PostMapping("/agregar")
    public String agregarBoletos(@RequestParam int cantidad) {
        cineService.agregarBoletos(cantidad);
        return cantidad + " boletos agregados exitosamente.";
    }

    @GetMapping("/boletos")
    public int obtenerBoletosDisponibles() {
        return cineService.obtenerBoletosDisponibles();
    }

    @PostMapping("/comprar")
    public String comprarBoletos(@RequestParam String cliente, @RequestParam int cantidad) {
        if (cliente == null || cliente.isBlank()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de boletos debe ser mayor a cero.");
        }
        return cineService.comprarBoleto(cliente, cantidad);
    }
    
    @GetMapping("/ventas")
    public List<Boleto> obtenerVentas() {
        return cineService.obtenerVentas();
    }
}
