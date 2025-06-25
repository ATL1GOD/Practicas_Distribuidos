/*
 * Nombre: Cardoso Osorio Atl Yosafat
 * Grupo: 7CM1
 * Materia: Sistemas Distribuidos
 * Practica 5: Objetos Distribuidos
 * Fecha: 26 de Marzo del 2025
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
// Métodos remotos
// Interfaz remota
public interface CineRMI extends Remote { // Interfaz remota
    int obtenerBoletosDisponibles() throws RemoteException; // Métodos remotos
    boolean comprarBoleto(String nombreCliente, int cantidad) throws RemoteException;
    void registrarConexion(String nombreCliente, String ipCliente) throws RemoteException;
    void registrarDesconexion(String nombreCliente) throws RemoteException;
}

