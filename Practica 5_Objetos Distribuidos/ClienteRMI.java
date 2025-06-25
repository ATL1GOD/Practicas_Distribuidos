/*
 * Nombre: Cardoso Osorio Atl Yosafat
 * Grupo: 7CM1
 * Materia: Sistemas Distribuidos
 * Practica 5: Objetos Distribuidos
 * Fecha: 26 de Marzo del 2025
 */
import java.net.InetAddress;
import java.rmi.Naming;
import java.util.Scanner;

public class ClienteRMI {
    public static void main(String[] args) {
        try {
            // Conectar con el servidor RMI
            CineRMI cine = (CineRMI) Naming.lookup("rmi://localhost/Cine");
            System.out.println("Conexión exitosa con el servidor RMI");
            Scanner scanner = new Scanner(System.in);

            // Solicitar el nombre del cliente
            System.out.print("Por favor, ingrese su nombre: ");
            String nombreCliente = scanner.nextLine();
            String ipCliente = InetAddress.getLocalHost().getHostAddress();

            // Notificar la conexión al servidor
            cine.registrarConexion(nombreCliente, ipCliente);
            System.out.println("¡Bienvenido, " + nombreCliente + "!");

            while (true) {
                System.out.println("\n--- Menú Cine ---");
                System.out.println("1. Ver boletos disponibles");
                System.out.println("2. Comprar boletos");
                System.out.println("3. Salir");
                System.out.print("Seleccione una opción: ");

                int opcion = scanner.nextInt();

                switch (opcion) {
                    case 1 -> System.out.println("Boletos disponibles: " + cine.obtenerBoletosDisponibles());
                    case 2 -> {
                        System.out.print("Ingrese la cantidad de boletos que desea comprar: ");
                        int cantidad = scanner.nextInt();
                        boolean compraExitosa = cine.comprarBoleto(nombreCliente, cantidad);
                        if (compraExitosa) {
                            System.out.println("Compra exitosa, " + nombreCliente + ". Boletos restantes: " + cine.obtenerBoletosDisponibles());
                        } else {
                            System.out.println("Lo sentimos, no hay suficientes boletos disponibles.");
                        }
                    }
                    case 3 -> {
                        System.out.println("Gracias por visitar el cine, " + nombreCliente + ". ¡Hasta luego!");
                        cine.registrarDesconexion(nombreCliente);
                        return;
                    }
                    default -> System.out.println("Opción no válida. Intente de nuevo.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al conectar con el servidor RMI:");
            e.printStackTrace();
        }
    }
}
