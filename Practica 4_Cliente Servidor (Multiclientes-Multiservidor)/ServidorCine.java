/*
 * Nombre: Cardoso Osorio Atl Yosafat
 * Grupo: 7CM1
 * Materia: Sistemas Distribuidos
 * Practica 4: Modelo Multicliente/MultiServidor
 * Fecha: 17 de Marzo del 2025
 */
import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

class Cine {
    private int boletosDisponibles;

    public Cine(int boletosDisponibles) {
        this.boletosDisponibles = boletosDisponibles;
    }

    public synchronized String comprarBoleto(String cliente, int cantidad) {
        if (boletosDisponibles < cantidad) {
            return "No hay suficientes boletos. Espere a la próxima función.";
        }
        boletosDisponibles -= cantidad;
        System.out.println(cliente + " compró " + cantidad + " boletos. Boletos restantes: " + boletosDisponibles); 
        return "Compra exitosa. Boletos restantes: " + boletosDisponibles;
    }

    public synchronized int getBoletosDisponibles() {
        return boletosDisponibles;
    }

    public synchronized void reponerBoletos(int cantidad) {
        boletosDisponibles += cantidad;
        notifyAll();
        System.out.println("Se han repuesto " + cantidad + " boletos. Boletos disponibles: " + boletosDisponibles); 
    }
}

class ReposicionBoletos extends Thread { // Este metodo se encarga de reponer los boletos
    private final Cine cine; // Se crea un objeto de la clase Cine

    public ReposicionBoletos(Cine cine) {// Se crea un constructor que recibe un objeto de la clase Cine
        this.cine = cine;
    }

    @Override
    public void run() { // Se crea un metodo run
        while (true) {
            try { // Se utiliza un try-catch para manejar excepciones
                Thread.sleep(60000); // Se duerme el hilo por 20 segundos
                cine.reponerBoletos(10); // Se llama al metodo reponerBoletos de la clase Cine
                System.out.println("Se han repuesto 10 boletos. Boletos disponibles: " + cine.getBoletosDisponibles());
            } catch (InterruptedException e) { 
                System.err.println("Error al reponer boletos: " + e.getMessage()); 
            }
        }
    }
}

class HiloCliente extends Thread { // Se crea una clase HiloCliente que extiende de Thread
    private final Socket socket;
    private final Cine cine;

    public HiloCliente(Socket socket, Cine cine) {
        this.socket = socket;
        this.cine = cine;
    }

    @Override
    public void run() { // Se crea un metodo run
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String nombreCliente = in.readLine(); // Se lee el nombre del cliente
            System.out.println(nombreCliente + " se ha conectado desde " + socket.getInetAddress());
            out.println("Bienvenido al Cine, " + nombreCliente + "!");

            boolean seguir = true; // Se crea una variable booleana seguir
            while (seguir) {
                String opcionStr = in.readLine(); // Se lee la opcion del cliente  
                if (opcionStr == null) break;

                int opcion; // Se crea una variable opcion
                try {
                    opcion = Integer.parseInt(opcionStr);
                } catch (NumberFormatException e) {
                    out.println("Opción no válida.");
                    continue;
                }

                switch (opcion) {
                    case 1 -> out.println("Boletos disponibles: " + cine.getBoletosDisponibles());
                    case 2 -> {
                        String cantidadStr = in.readLine();
                        int cantidad;
                        try {
                            cantidad = Integer.parseInt(cantidadStr);
                        } catch (NumberFormatException e) {
                            out.println("Cantidad no válida.");
                            continue;
                        }
                        out.println(cine.comprarBoleto(nombreCliente, cantidad));
                    }
                    case 3 -> {
                        out.println("Gracias por su compra. ¡Hasta luego!");
                        seguir = false;
                        System.out.println(nombreCliente + " ha salido.");
                    }
                    default -> out.println("Opción no válida.");
                }
            }
        } catch (IOException e) { // Se utiliza un catch para manejar excepciones
            System.err.println("Error en la conexión con el cliente: " + e.getMessage());
        } finally {
            try { // Se utiliza un try-catch para manejar excepciones
                socket.close(); // Se cierra el socket
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }
}

public class ServidorCine { // Se crea una clase ServidorCine
    public static void main(String[] args) { // Se crea un metodo main
        String ipServidor = "192.168.1.74"; //localhost";   // Se crea una variable ipServidor
        int puerto = 8889; // Se crea una variable puerto
        Cine cine = new Cine(30); // Se crea un objeto de la clase Cine
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);// Se crea un pool de hilos

        new ReposicionBoletos(cine).start(); // Se crea un objeto de la clase ReposicionBoletos y se inicia

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor de cine iniciado en " + ipServidor + ":" + puerto);

            while (true) { // Se crea un ciclo while
                Socket socketCliente = serverSocket.accept();
                pool.execute(new HiloCliente(socketCliente, cine));
            }
        } catch (IOException e) { // Se utiliza un catch para manejar excepciones
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}




