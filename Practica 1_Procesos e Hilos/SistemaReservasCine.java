/*
 * Nombre: Cardoso Osorio Atl Yosafat
 * Grupo: 7CM1
 * Materia: Sistemas Distribuidos
 * Practica 1: Procesos e Hilos 
 * Fecha: 24 de Febrero del 2025
 */
class Cine { // Clase Cine que simula la venta de boletos en un cine
    private int boletosDisponibles;
    // Constructor de la clase Cine 
    public Cine(int boletosDisponibles) {
        this.boletosDisponibles = boletosDisponibles;
    }

    public void comprarBoleto(String cliente, int cantidad) { // Método para comprar boletos
        boolean successful = false;
        synchronized (this) { // Sincronizar el acceso a la sección crítica
            if (boletosDisponibles >= cantidad) {
                System.out.println(cliente + " está comprando " + cantidad + " boleto(s).");
                boletosDisponibles -= cantidad;
                System.out.println(cliente + " completó la compra. Boletos restantes: " + boletosDisponibles);
                successful = true;
            } else {
                System.out.println(cliente + " intentó comprar " + cantidad + " boleto(s), pero no hay suficientes disponibles.");
            }
        }
        if (successful) { // Simular tiempo de procesamiento de la compra
            try {
                Thread.sleep(10000); // Simular tiempo de procesamiento de la compra
            } catch (InterruptedException e) {
                System.err.println("Thread interrupted: " + e.getMessage());
            }
        }
    }

    public int getBoletosDisponibles() { // Método para obtener los boletos disponibles
        return boletosDisponibles;
    }
}

class Cliente extends Thread { // Clase Cliente que simula a un cliente comprando boletos
    private final Cine cine;
    private final String nombre;
    private final int boletos;

    public Cliente(Cine cine, String nombre, int boletos) { // Constructor de la clase Cliente
        this.cine = cine;
        this.nombre = nombre;
        this.boletos = boletos;
    }

    @Override
    public void run() { // Método run que simula la compra de boletos por parte del cliente
        cine.comprarBoleto(nombre, boletos);
    }
}

public class SistemaReservasCine { // Clase principal que simula el sistema de reservas de un cine
    public static void main(String[] args) { 
        Cine cine = new Cine(20); // Solo hay 20 boletos disponibles

        // Simulación de clientes intentando comprar boletos al mismo tiempo
        Thread cliente1 = new Cliente(cine, "Cliente 1", 6); 
        Thread cliente2 = new Cliente(cine, "Cliente 2", 4);
        Thread cliente3 = new Cliente(cine, "Cliente 3", 8); 
        Thread cliente4 = new Cliente(cine, "Cliente 4", 2);
        Thread cliente5 = new Cliente(cine, "Cliente 5", 5);
        Thread cliente6 = new Cliente(cine, " Cliente 6", 3);

        // Iniciar los hilos
        cliente1.start(); 
        cliente2.start();
        cliente3.start();
        cliente4.start();
        cliente5.start();
        cliente6.start();

        try { // Esperar a que todos los clientes terminen de comprar boletos
            cliente1.join();
            cliente2.join();
            cliente3.join();
            cliente4.join();
            cliente5.join();
            cliente6.join();
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted: " + e.getMessage());
        }

        System.out.println("Reservas finalizadas. Boletos restantes: " + cine.getBoletosDisponibles());
    }
}
