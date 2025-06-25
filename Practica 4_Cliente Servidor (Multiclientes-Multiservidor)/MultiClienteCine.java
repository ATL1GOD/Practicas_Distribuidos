/*
 * Nombre: Cardoso Osorio Atl Yosafat
 * Grupo: 7CM1
 * Materia: Sistemas Distribuidos
 * Practica 4: Modelo Multicliente/MultiServidor
 * Fecha: 17 de Marzo del 2025
 */
import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class MultiClienteCine { // Clase MultiClienteCine
    private final int PUERTO = 8888; // Puerto del balanceador de carga
    private Socket socket; // Socket del cliente
    private BufferedReader entrada;
    private PrintWriter salida; // PrintWriter para enviar mensajes al servidor
    private final JFrame frame; // JFrame para la interfaz grafica
    private final JTextField nombreField;
    private final JTextArea displayArea;
    private final JButton verBoletosBtn, comprarBtn, salirBtn;

    public MultiClienteCine() {
        frame = new JFrame("Cliente Cine"); // Se crea un objeto de la clase JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Se establece la operacion por defecto al cerrar la ventana
        frame.setSize(400, 300); // Se establece el tamaño de la ventana
        frame.setLayout(new BorderLayout()); // Se establece el layout de la ventana

        JPanel topPanel = new JPanel(); // Se crea un objeto de la clase JPanel
        topPanel.setLayout(new BorderLayout());
        nombreField = new JTextField();
        topPanel.add(new JLabel("Ingrese su nombre: "), BorderLayout.WEST);
        topPanel.add(nombreField, BorderLayout.CENTER);

        JButton conectarBtn = new JButton("Conectar con Sucursal");
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

        // Seleccionar servidor (sucursal)
        String[] servidores = {"Sucursal Norte (192.168.1.74)", "Sucursal Sur (192.168.1.82)"};
        String sucursalSeleccionada = (String) JOptionPane.showInputDialog( // Se crea un cuadro de dialogo
            frame, 
            "Seleccione una sucursal",  //
            "Conexión al Cine", 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            servidores, 
            servidores[0]
        );

        if (sucursalSeleccionada == null) { // Se verifica si no se selecciono una sucursal
            JOptionPane.showMessageDialog(frame, "Debe seleccionar una sucursal", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ipServidor = sucursalSeleccionada.contains("1") ? "192.168.1.74" : "192.168.1.82"; // Se selecciona la ip del servidor 

        try {
            socket = new Socket(ipServidor, PUERTO); // Se crea un objeto de la clase Socket
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Se crea un objeto de la clase BufferedReader
            salida = new PrintWriter(socket.getOutputStream(), true);
            salida.println(nombre); // Enviar nombre al servidor
            displayArea.append(entrada.readLine() + "\n"); // Mostrar mensaje del servidor
        } catch (IOException e) { // Se utiliza un catch para manejar excepciones
            JOptionPane.showMessageDialog(frame, "No se pudo conectar al servidor.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void verBoletos() { // Metodo verBoletos
        salida.println("1");
        try {
            displayArea.append("Servidor: " + entrada.readLine() + "\n"); // Se muestra el mensaje del servidor
        } catch (IOException e) {
            displayArea.append("Error al recibir información.\n"); // Se muestra un mensaje de error
        }
    }

    private void comprarBoletos() { // Metodo comprarBoletos
        String cantidadStr = JOptionPane.showInputDialog(frame, "Ingrese la cantidad de boletos:");
        if (cantidadStr == null) return;
        salida.println("2");
        salida.println(cantidadStr);
        try {
            displayArea.append("Servidor: " + entrada.readLine() + "\n");
        } catch (IOException e) {
            displayArea.append("Error al recibir respuesta.\n");
        }
    }

    private void salir() { // Metodo salir
        salida.println("3");
        try {
            displayArea.append("Servidor: " + entrada.readLine() + "\n");
            socket.close();
        } catch (IOException e) {
            displayArea.append("Error al cerrar la conexión.\n");
        }
        frame.dispose();
    }

    public static void main(String[] args) { // Metodo main
        SwingUtilities.invokeLater(MultiClienteCine::new); // Se crea un objeto de la clase MultiClienteCine
    }
}
