/*
 * Nombre: Cardoso Osorio Atl Yosafat
 * Grupo: 7CM1
 * Materia: Sistemas Distribuidos
 * Practica 4: Modelo Multicliente/MultiServidor
 * Fecha: 17 de Marzo del 2025
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class BalanceadorCarga { // Clase BalanceadorCarga
    private static final int PUERTO_BALANCEADOR = 8888; // Puerto del balanceador de carga
    private static final List<String> SERVIDORES = Arrays.asList("192.168.1.74", "192.168.1.82");

    public static void main(String[] args) { // Metodo main de la clase BalanceadorCarga
        try (ServerSocket serverSocket = new ServerSocket(PUERTO_BALANCEADOR)) { // Se crea un objeto de la clase ServerSocket
            System.out.println("Balanceador de carga iniciado en el puerto " + PUERTO_BALANCEADOR);

            while (true) { // Se crea un ciclo infinito
                try { // Se utiliza un try-catch para manejar excepciones
                    Socket clienteSocket = serverSocket.accept(); 
                    System.out.println("Conexión entrante desde: " + clienteSocket.getInetAddress());

                    String servidorElegido = seleccionarServidor();
                    System.out.println("Conexión redirigida al servidor: " + servidorElegido);

                    new Thread(new ManejadorConexion(clienteSocket, servidorElegido, 8889)).start();
                } catch (IOException e) {
                    System.err.println("Error al aceptar conexión del cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el balanceador de carga: " + e.getMessage());
        }
    }

    private static String seleccionarServidor() {
        // El balanceador redirige aleatoriamente a uno de los servidores disponibles, 
        //en caso de tener mucha carga en el solicitado.
        Random rand = new Random();
        return SERVIDORES.get(rand.nextInt(SERVIDORES.size())); // Se regresa un servidor aleatorio
    }
}


class ManejadorConexion implements Runnable {
    private final Socket clienteSocket;
    private final String servidorDestino;
    private final int puertoServidor;

    public ManejadorConexion(Socket clienteSocket, String servidorDestino, int puertoServidor) {
        this.clienteSocket = clienteSocket;
        this.servidorDestino = servidorDestino;
        this.puertoServidor = puertoServidor;
    }

    @Override
    public void run() {
        try (Socket servidorSocket = new Socket(servidorDestino, puertoServidor)) {
            InputStream clienteInput = clienteSocket.getInputStream();
            OutputStream clienteOutput = clienteSocket.getOutputStream();
            InputStream servidorInput = servidorSocket.getInputStream();
            OutputStream servidorOutput = servidorSocket.getOutputStream();

            Thread clienteAServidor = new Thread(new Proxy(clienteInput, servidorOutput));
            Thread servidorACliente = new Thread(new Proxy(servidorInput, clienteOutput));
            
            clienteAServidor.start();
            servidorACliente.start();
            
            clienteAServidor.join();
            servidorACliente.join();
        } catch (IOException e) {
            System.err.println("Error al conectar con el servidor " + servidorDestino + ": " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error en la ejecución del hilo de conexión: " + e.getMessage());
        } finally {
            try {
                clienteSocket.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket del cliente: " + e.getMessage());
            }
        }
    }
}

class Proxy implements Runnable {
    private final InputStream in;
    private final OutputStream out;

    public Proxy(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1024];
            int bytesLeidos;
            while ((bytesLeidos = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesLeidos);
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("Error de I/O en Proxy: " + e.getMessage());
        }
    }
}
