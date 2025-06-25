package com.servicioweb.cine;
import com.servicioweb.cine.model.Boleto; 
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CineService {
    private static final Logger logger = LoggerFactory.getLogger(CineService.class);
    private int boletosDisponibles = 30;
    private List<Boleto> ventas = new ArrayList<>();

        public synchronized void agregarBoletos(int cantidad) {
            boletosDisponibles += cantidad;
        }
    
        @Scheduled(fixedRate = 60000) 
        public synchronized void generarBoletosAutomaticos() {
            int boletosGenerados = 10;  
            boletosDisponibles += boletosGenerados;
            System.out.println("Generados " + boletosGenerados + " boletos automáticamente.");
        }
    public synchronized int obtenerBoletosDisponibles() {
        return boletosDisponibles;
    }

    public synchronized String comprarBoleto(String cliente, int cantidad) {
        if (boletosDisponibles < cantidad) {
            logger.warn("Compra fallida: Cliente={} intentó comprar {} boletos. Boletos disponibles={}", cliente, cantidad, boletosDisponibles);
            return "No hay suficientes boletos disponibles.";
        }
        boletosDisponibles -= cantidad;
        ventas.add(new Boleto(cliente, cantidad));
        logger.info("Compra exitosa: Cliente={} compró {} boletos. Boletos restantes={}", cliente, cantidad, boletosDisponibles);
        return "Compra exitosa. Boletos restantes: " + boletosDisponibles;
    }

    public List<Boleto> obtenerVentas() {
        return ventas;
    }
}

