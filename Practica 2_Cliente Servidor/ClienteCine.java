/*
 * Nombre: Cardoso Osorio Atl Yosafat
 * Grupo: 7CM1
 * Materia: Sistemas Distribuidos
 * Practica 2: Modelo Cliente/Servidor
 * Fecha: 01 de Marzo del 2025
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteCine { // Clase ClienteCine
    public static void main(String[] args) {
        final String SERVIDOR = "127.0.0.1"; // Dirección del servidor (localhost)
        final int PUERTO = 8888; // Mismo puerto que el servidor

        try (Socket socket = new Socket(SERVIDOR, PUERTO);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Conectado al cine."); // Mensaje de conexión exitosa

            // Ingresar nombre del cliente
            System.out.print("Ingrese su nombre: "); // Solicitar nombre al cliente
            String nombre = scanner.nextLine();
            salida.println(nombre); // Enviar nombre al servidor
            System.out.println(entrada.readLine()); // Mensaje de bienvenida del servidor

            while (true) {
                // Leer y mostrar el menú hasta que se reciba "Seleccione una opción:"
                String linea;
                while (!(linea = entrada.readLine()).contains("Seleccione una opción")) {
                    System.out.println(linea);
                }
                System.out.println(linea); // Imprime "Seleccione una opción:"

                // Leer opción del usuario
                System.out.print("Opción: "); // Solicitar opción al cliente
                String opcionStr = scanner.nextLine();
                salida.println(opcionStr); // Enviar opción al servidor

                switch (opcionStr) {
                    case "1", "3" -> {
                        // Si es ver funciones o salir, recibir respuesta del servidor
                        System.out.println("Servidor: " + entrada.readLine());
                        if (opcionStr.equals("3")) {
                            System.out.println("Saliendo del cine... ¡Nos vemos Cinefilo!");
                            return; // Salir del método si elige salir
                        }
                    }
                    case "2" -> {
                        // Comprar boletos
                        System.out.println(entrada.readLine()); // Esperar mensaje del servidor "Ingrese la cantidad..."
                        String cantidad = scanner.nextLine();
                        salida.println(cantidad); // Enviar cantidad de boletos al servidor
                        String respuesta = entrada.readLine(); // Recibir respuesta de compra
                        System.out.println("Servidor: " + respuesta);
                    }
                    default -> System.out.println("Opción no válida.");
                }
            }
        } catch (IOException e) { // Excepción de entrada/salida
            System.err.println("Error en el cliente: " + e.getMessage());
        }
    }
}
