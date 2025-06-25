/*
 * Nombre: Cardoso Osorio Atl Yosafat
 * Grupo: 7CM1
 * Materia: Sistemas Distribuidos
 * Practica 5: Objetos Distribuidos
 * Fecha: 26 de Marzo del 2025
 */
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServidorRMI { // Clase principal
    public static void main(String[] args) {// Método principal
        try {
            // Crear el servidor RMI
            CineServidorRMI servidor = new CineServidorRMI(); // Crear una instancia del servidor
            int puerto = 1099; // Puerto por defecto

            // Crear un registro RMI en el puerto especificado
            try {
                LocateRegistry.createRegistry(puerto); // Crear un registro RMI en el puerto especificado
                System.out.println("Registro RMI creado en el puerto " + puerto); // Mensaje de éxito
            } catch (Exception e) {
                System.out.println("El puerto " + puerto + " ya está en uso, intentando con el puerto 1098.");
                LocateRegistry.createRegistry(1098); // Crear un registro RMI en el puerto 1098
                puerto = 1098; // Actualizar el puerto
            }

            // Registrar el servidor con el nombre "Cine"
            Naming.rebind("rmi://localhost:" + puerto + "/Cine", servidor); // Registrar el servidor con el nombre "Cine"
            System.out.println("Servidor RMI de cine iniciado en el puerto " + puerto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
