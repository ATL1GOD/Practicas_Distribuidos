/*
 * Nombre: Cardoso Osorio Atl Yosafat
 * Grupo: 7CM1
 * Materia: Sistemas Distribuidos
 * Practica 3: Modelo Multicliente/Servidor
 * Fecha: 09 de Marzo del 2025
 */

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class MultiClienteCine { // Clase MultiClienteCine
    private final String SERVIDOR = "192.168.1.74"; // Se declara una constante de tipo String con la direccion IP del servidor
    private final int PUERTO = 8888; // Se declara una constante de tipo int con el puerto del servidor
    private Socket socket; // Se declara un objeto de tipo Socket
    private BufferedReader entrada; // Se declara un objeto de tipo BufferedReader
    private PrintWriter salida;
    private final JFrame frame;
    private final JTextField nombreField;
    private final JTextArea displayArea;
    private final JButton verBoletosBtn, comprarBtn, salirBtn;

    public MultiClienteCine() { // Constructor de la clase MultiClienteCine
        try {
            socket = new Socket(SERVIDOR, PUERTO); // Se crea un socket con la direccion IP y puerto del servidor
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {// Se utiliza un try-catch para manejar excepciones
            JOptionPane.showMessageDialog(null, "No se pudo conectar al servidor.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        frame = new JFrame("Cliente Cine"); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        nombreField = new JTextField();
        topPanel.add(new JLabel("Ingrese su nombre: "), BorderLayout.WEST);
        topPanel.add(nombreField, BorderLayout.CENTER);

        JButton conectarBtn = new JButton("Conectar");
        conectarBtn.addActionListener(e -> conectar());
        topPanel.add(conectarBtn, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        frame.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(3, 1));

        verBoletosBtn = new JButton("Ver Boletos");
        verBoletosBtn.addActionListener(e -> verBoletos());
        sidePanel.add(verBoletosBtn);

        comprarBtn = new JButton("Comprar");
        comprarBtn.addActionListener(e -> comprarBoletos());
        sidePanel.add(comprarBtn);

        salirBtn = new JButton("Salir");
        salirBtn.addActionListener(e -> salir());
        sidePanel.add(salirBtn);

        frame.add(sidePanel, BorderLayout.EAST);
        frame.setVisible(true);
    }

    private void conectar() { // Metodo conectar
        String nombre = nombreField.getText().trim();
        if (nombre.isEmpty()) { // Se verifica si el nombre esta vacio
            JOptionPane.showMessageDialog(frame, "Por favor, ingrese su nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        salida.println(nombre); // Se envia el nombre al servidor
        try {
            displayArea.append(entrada.readLine() + "\n");
        } catch (IOException e) {
            displayArea.append("Error al recibir mensaje de bienvenida.\n");
        }
    }

    private void verBoletos() { // Metodo verBoletos
        salida.println("1"); // Se envia un 1 al servidor
        try {
            displayArea.append("Servidor: " + entrada.readLine() + "\n");
        } catch (IOException e) { // Se utiliza un catch para manejar excepciones
            displayArea.append("Error al recibir información.\n");
        }
    }

    private void comprarBoletos() { // Metodo comprarBoletos
        String cantidadStr = JOptionPane.showInputDialog(frame, "Ingrese la cantidad de boletos:");
        if (cantidadStr == null) return; // Se verifica si la cantidad es nula
        salida.println("2"); // Se envia un 2 al servidor
        salida.println(cantidadStr);
        try {
            displayArea.append("Servidor: " + entrada.readLine() + "\n");
        } catch (IOException e) {
            displayArea.append("Error al recibir respuesta.\n");
        }
    }

    private void salir() { //  Metodo salir
        salida.println("3"); // Se envia un 3 al servidor
        try {
            displayArea.append("Servidor: " + entrada.readLine() + "\n");
            socket.close(); // Se cierra el socket
        } catch (IOException e) {
            displayArea.append("Error al cerrar la conexión.\n");
        }
        frame.dispose();
    }

    public static void main(String[] args) { // Metodo main
        SwingUtilities.invokeLater(MultiClienteCine::new);
    }
}
