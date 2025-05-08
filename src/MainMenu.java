import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {
    public MainMenu() {
        // Configuración de la ventana principal
        setTitle("Menú Principal - Herramientas Electrónicas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(400, 300); // Aumentado para más botones
        setLocationRelativeTo(null); // Centrar la ventana

        // Panel central con un título
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Seleccione una Herramienta");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espacio

        // Botón para abrir la calculadora de resistencias
        JButton resistorButton = new JButton("Calculadora de Resistencias por Colores");
        resistorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resistorButton.addActionListener(e -> {
            // Abrir la ventana de la calculadora de resistencias
            new ResistorInterface();
        });
        centerPanel.add(resistorButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio

        // Botón para abrir la calculadora de amplificador diferencial
        JButton diffAmpButton = new JButton("Calculadora de Amplificador Diferencial");
        diffAmpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        diffAmpButton.addActionListener(e -> {
            // Abrir la ventana de la calculadora de amplificador diferencial
            new DifferentialAmpInterface();
        });
        centerPanel.add(diffAmpButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio

        // Botón para abrir la calculadora de filtro pasivo
        JButton passiveFilterButton = new JButton("Calculadora de Filtro Pasivo");
        passiveFilterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        passiveFilterButton.addActionListener(e -> {
            // Abrir la ventana de la calculadora de filtro pasivo
            new PassiveFilterInterface();
        });
        centerPanel.add(passiveFilterButton);

        // Añadir el panel al centro de la ventana
        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        // Ejecutar el menú principal en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}