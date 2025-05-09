package gui;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("Menú Principal - ElecTool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(400, 350);
        setLocationRelativeTo(null);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("ELECTOOL ");
        titleLabel.setFont(new Font("Bauhaus 93", Font.ITALIC, 40));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel subtitleLabel = new JLabel("Seleccione una Herramienta");
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(subtitleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton resistorButton = new JButton("Calculadora de Resistencias por Colores");
        resistorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resistorButton.addActionListener(e -> new ResistorInterface());
        centerPanel.add(resistorButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton diffAmpButton = new JButton("Calculadora de Amplificador Diferencial");
        diffAmpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        diffAmpButton.addActionListener(e -> new DifferentialAmpInterface());
        centerPanel.add(diffAmpButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton passiveFilterButton = new JButton("Calculadora de Filtro Pasivo");
        passiveFilterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        passiveFilterButton.addActionListener(e -> new PassiveFilterInterface());
        centerPanel.add(passiveFilterButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton activeFilterButton = new JButton("Calculadora de Filtro Activo");
        activeFilterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        activeFilterButton.addActionListener(e -> new ActiveFilterInterface());
        centerPanel.add(activeFilterButton);

        add(centerPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}