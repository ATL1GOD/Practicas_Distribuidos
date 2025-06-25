/*
 * Nombre: Cardoso Osorio Atl Yosafat
 * Grupo: 7CM1
 * Materia: Sistemas Distribuidos
 * Practica 2: Modelo Cliente/Servidor
 * Fecha: 01 de Marzo del 2025
 */
import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

// Clase que maneja los boletos disponibles
class Cine {
    private int boletosDisponibles;

    public Cine(int boletosDisponibles) {
        this.boletosDisponibles = boletosDisponibles;
    }

    public synchronized String comprarBoleto(String cliente, int cantidad) {
        if (boletosDisponibles < cantidad) {
            System.out.println(cliente + " intentó comprar " + cantidad + " boleto(s), pero no hay suficientes disponibles.");
            return "No hay suficientes boletos. Espere a la próxima función.";
        }
        boletosDisponibles -= cantidad;
        System.out.println(cliente + " compró " + cantidad + " boleto(s). Boletos restantes: " + boletosDisponibles);
        return "Compra exitosa. Boletos restantes: " + boletosDisponibles;
    }

    public synchronized int getBoletosDisponibles() {
        return boletosDisponibles;
    }

    public synchronized void reponerBoletos(int cantidad) {
        boletosDisponibles += cantidad;
        System.out.println("Se repusieron " + cantidad + " boletos. Nuevo stock: " + boletosDisponibles);
        notifyAll();
    }
}

// Hilo que maneja la reposición de boletos
class ReposicionBoletos extends Thread {
    private final Cine cine;

    public ReposicionBoletos(Cine cine) { // Constructor
        this.cine = cine;
    }

    @Override
    public void run() { // Método run
        while (true) {
            try { // Manejo de excepciones
                Thread.sleep(20000); // Reposición cada 20 segundos
                cine.reponerBoletos(10); // Reposición de 10 boletos
            } catch (InterruptedException e) { // Excepción de interrupción
                System.err.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }
}

// Hilo para manejar cada cliente
class HiloCliente extends Thread {
    private final Socket socket;
    private final Cine cine;

    public HiloCliente(Socket socket, Cine cine) { // Constructor
        this.socket = socket;
        this.cine = cine;
    }

    @Override
    public void run() {
        try ( // Manejo de excepciones
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String nombreCliente = in.readLine(); // Leer nombre del cliente
            System.out.println(nombreCliente + " se ha conectado."); // Mensaje de conexión
            out.println("Bienvenido al Cine, " + nombreCliente + "!"); // Mensaje de bienvenida

            while (true) { // Bucle para mostrar el menú
                out.println("\n--- Menú del Cine ---"); 
                out.println("1. Ver boletos disponibles");
                out.println("2. Comprar boletos");
                out.println("3. Salir");
                out.println("Seleccione una opción:");

                String opcionStr = in.readLine(); // Leer opción del cliente
                if (opcionStr == null) break;

                int opcion; // Convertir opción a entero
                try {
                    opcion = Integer.parseInt(opcionStr);
                } catch (NumberFormatException e) {
                    out.println("Opción no válida. Intente de nuevo.");
                    continue;
                }

                switch (opcion) { // Switch para manejar las opciones
                    case 1 -> out.println("Boletos disponibles: " + cine.getBoletosDisponibles());
                    case 2 -> { // Comprar boletos
                        out.println("Ingrese la cantidad de boletos que desea comprar:");
                        String cantidadStr = in.readLine();
                        int cantidad;
                        try { // Convertir cantidad a entero
                            cantidad = Integer.parseInt(cantidadStr);
                        } catch (NumberFormatException e) {
                            out.println("Cantidad no válida. Intente de nuevo.");
                            continue;
                        } // Comprar boletos
                        String respuesta = cine.comprarBoleto(nombreCliente, cantidad);
                        out.println(respuesta);
                    }
                    case 3 -> { // Salir
                        out.println("Gracias por su compra. ¡Hasta luego!");
                        System.out.println(nombreCliente + " se ha desconectado.");
                    } // Salir del bucle si elige salir
                    default -> out.println("Opción no válida. Intente de nuevo.");
                }
            }
        } catch (IOException e) { // Excepción de entrada/salida
            System.err.println("Error en la conexión con el cliente: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) { // Excepción de entrada/salida
                System.err.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }
}

// Servidor principal
public class ServidorCine { // Clase principal
    public static void main(String[] args) { // Método principal
        int puerto = 8888; // Puerto del servidor
        Cine cine = new Cine(30); // Crear cine con 30 boletos disponibles
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(4); // Pool de hilos

        new ReposicionBoletos(cine).start(); // Iniciar hilo de reposición de boletos

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor de cine iniciado en el puerto " + puerto);

            while (true) { // Bucle para aceptar conexiones
                Socket socketCliente = serverSocket.accept();
                pool.execute(new HiloCliente(socketCliente, cine));
            }
        } catch (IOException e) { // Excepción de entrada/salida
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}
