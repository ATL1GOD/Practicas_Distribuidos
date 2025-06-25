/*
 * Nombre: Cardoso Osorio Atl Yosafat
 * Grupo: 7CM1
 * Materia: Sistemas Distribuidos
 * Practica 5: Objetos Distribuidos
 * Fecha: 26 de Marzo del 2025
 */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.time.LocalDateTime;

public class CineServidorRMI extends UnicastRemoteObject implements CineRMI {// Clase que implementa la interfaz remota
    private static final String URL = "jdbc:mysql://localhost:3306/cine?useSSL=false&serverTimezone=UTC"; // URL de la base de datos
    private static final String USER = "root"; // Usuario de la base de datos
    private static final String PASSWORD = "Atl1God$"; // Contraseña de la base de datos

    protected CineServidorRMI() throws RemoteException { // Constructor
        super(); // Llama al constructor de la clase padre
        // Iniciar el hilo que generará boletos en intervalos
        Thread repositorBoletos = new Thread(new RepositorBoletos(this));
        repositorBoletos.start(); // Inicia el hilo
    }

    @Override // Implementación de los métodos remotos
    public int obtenerBoletosDisponibles() throws RemoteException {
        int stock = 0; // Inicializar el stock
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement(); // Crear una sentencia
             ResultSet rs = stmt.executeQuery("SELECT stock FROM boletos WHERE producto='Boleto'")
        ) { // Ejecutar una consulta
            if (rs.next()) { /// Si hay un resultado
                stock = rs.getInt("stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stock;
    }

    @Override // Implementación de los métodos remotos
    public synchronized boolean comprarBoleto(String nombreCliente, int cantidad) throws RemoteException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) { // Crear una conexión
            conn.setAutoCommit(false); // Deshabilitar el modo de autocommit

            try (Statement stmt = conn.createStatement(); // Crear una sentencia
                 ResultSet rs = stmt.executeQuery("SELECT stock FROM boletos WHERE producto='Boleto' FOR UPDATE")) {
                if (rs.next()) { // Si hay un resultado
                    int stockActual = rs.getInt("stock"); // Obtener el stock actual
                    if (stockActual >= cantidad) { // Si hay suficientes boletos
                        try (PreparedStatement pstmt = conn.prepareStatement( // Crear una sentencia preparada
                                "UPDATE boletos SET stock = stock - ? WHERE producto='Boleto'")) { // Actualizar el stock
                            pstmt.setInt(1, cantidad); // Establecer el valor del parámetro
                            pstmt.executeUpdate(); // Ejecutar la sentencia
                        }
                        conn.commit(); // Confirmar la transacción
                        System.out.println("[" + LocalDateTime.now() + "] Cliente '" + nombreCliente + "' compró " + cantidad + " boletos. Stock restante: " + (stockActual - cantidad));
                        return true; // Compra exitosa
                    }
                }
            }
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void anadirStock(int cantidad) throws RemoteException { // Método para añadir boletos al stock
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE boletos SET stock = stock + ? WHERE producto='Boleto'")) {
            pstmt.setInt(1, cantidad); // Establecer el valor del parámetro
            pstmt.executeUpdate(); 
            int nuevoStock = obtenerBoletosDisponibles(); // Obtener el stock actualizado
            System.out.println("Se añadieron " + cantidad + " boletos. Nuevo stock: " + nuevoStock);
        } catch (SQLException e) { // Capturar excepciones
            e.printStackTrace(); // Imprimir la traza de la pila
        }
    }

    @Override
    public void registrarConexion(String nombreCliente, String ipCliente) throws RemoteException { // Método para registrar la conexión de un cliente
        System.out.println("[" + LocalDateTime.now() + "] Cliente conectado: " + nombreCliente + " desde la IP: " + ipCliente); // Imprimir mensaje
    }

    @Override // Implementación de los métodos remotos
    public void registrarDesconexion(String nombreCliente) throws RemoteException {
        System.out.println("Cliente desconectado: " + nombreCliente);
    }

    // Hilo que repone boletos automáticamente
    private static class RepositorBoletos implements Runnable {
        private final CineServidorRMI servidor;

        public RepositorBoletos(CineServidorRMI servidor) {
            this.servidor = servidor; // Inicializar el servidor
        }

        @Override
        public void run() { // Método run
            try { // Capturar excepciones
                while (true) { // Bucle infinito
                    Thread.sleep(10000); // Espera 10 segundos
                    servidor.anadirStock(10); // Añadir 10 boletos al stock
                }
            } catch (InterruptedException | RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
