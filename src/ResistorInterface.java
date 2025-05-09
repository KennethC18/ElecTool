import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ResistorInterface extends JFrame {
    private ResistorPanel resistorPanel;
    private JComboBox<String>[] comboBoxes = new JComboBox[4];
    private JLabel resistanceLabel;

    public ResistorInterface() {
        setTitle("Calculadora de Resistencias");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        resistorPanel = new ResistorPanel("resistor.png");
        add(resistorPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        String[] coloresDigitos = {"Negro", "Marrón", "Rojo", "Naranja", "Amarillo", "Verde", "Azul", "Violeta", "Gris", "Blanco"};
        String[] coloresMultiplicador = {"Negro", "Marrón", "Rojo", "Naranja", "Amarillo", "Verde", "Azul", "Violeta", "Gris", "Blanco", "Oro", "Plata"};
        String[] coloresTolerancia = {"Marrón", "Rojo", "Verde", "Azul", "Violeta", "Gris", "Oro", "Plata", "Ninguno"};

        controlPanel.add(new JLabel("Resistencia:"));
        resistanceLabel = new JLabel("0 Ω ± 0%");
        controlPanel.add(resistanceLabel);

        comboBoxes[0] = new JComboBox<>(coloresDigitos);
        comboBoxes[1] = new JComboBox<>(coloresDigitos);
        comboBoxes[2] = new JComboBox<>(coloresMultiplicador);
        comboBoxes[3] = new JComboBox<>(coloresTolerancia);

        for (int i = 0; i < 4; i++) {
            controlPanel.add(new JLabel("Banda " + (i + 1) + ":"));
            controlPanel.add(comboBoxes[i]);
            comboBoxes[i].setSelectedIndex(0);
        }

        for (int i = 0; i < 4; i++) {
            final int index = i;
            comboBoxes[i].addActionListener(e -> {
                String selected = (String) comboBoxes[index].getSelectedItem();
                Color color = getColorFromString(selected);
                resistorPanel.setBandColor(index, color);
                updateResistanceLabel();
            });
        }

        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(850, 300));
        setLocationRelativeTo(null);
        setVisible(true);

        updateResistanceLabel();
    }

    private Color getColorFromString(String colorName) {
        switch (colorName) {
            case "Negro": return Color.BLACK;
            case "Marrón": return new Color(139, 69, 19);
            case "Rojo": return Color.RED;
            case "Naranja": return Color.ORANGE;
            case "Amarillo": return Color.YELLOW;
            case "Verde": return Color.GREEN;
            case "Azul": return Color.BLUE;
            case "Violeta": return new Color(148, 0, 211);
            case "Gris": return Color.GRAY;
            case "Blanco": return Color.WHITE;
            case "Oro": return new Color(255, 215, 0);
            case "Plata": return new Color(192, 192, 192);
            case "Ninguno": return Color.LIGHT_GRAY;
            default: return Color.BLACK;
        }
    }

    private void updateResistanceLabel() {
        String banda1 = (String) comboBoxes[0].getSelectedItem();
        String banda2 = (String) comboBoxes[1].getSelectedItem();
        String multiplicador = (String) comboBoxes[2].getSelectedItem();
        String tolerancia = (String) comboBoxes[3].getSelectedItem();

        int digito1 = getDigitValue(banda1);
        int digito2 = getDigitValue(banda2);
        double multiplicadorValor = getMultiplierValue(multiplicador);
        String toleranciaValor = getToleranceValue(tolerancia);

        double resistencia = ((digito1 * 10) + digito2) * multiplicadorValor;

        String resistenciaTexto;
        if (resistencia >= 1_000_000) {
            resistenciaTexto = String.format("%.2f MΩ", resistencia / 1_000_000);
        } else if (resistencia >= 1_000) {
            resistenciaTexto = String.format("%.2f kΩ", resistencia / 1_000);
        } else {
            resistenciaTexto = String.format("%.2f Ω", resistencia);
        }

        resistanceLabel.setText(resistenciaTexto + " ± " + toleranciaValor);
    }

    private int getDigitValue(String color) {
        switch (color) {
            case "Negro": return 0;
            case "Marrón": return 1;
            case "Rojo": return 2;
            case "Naranja": return 3;
            case "Amarillo": return 4;
            case "Verde": return 5;
            case "Azul": return 6;
            case "Violeta": return 7;
            case "Gris": return 8;
            case "Blanco": return 9;
            default: return 0;
        }
    }

    private double getMultiplierValue(String color) {
        switch (color) {
            case "Negro": return 1;
            case "Marrón": return 10;
            case "Rojo": return 100;
            case "Naranja": return 1_000;
            case "Amarillo": return 10_000;
            case "Verde": return 100_000;
            case "Azul": return 1_000_000;
            case "Violeta": return 10_000_000;
            case "Gris": return 100_000_000;
            case "Blanco": return 1_000_000_000;
            case "Oro": return 0.1;
            case "Plata": return 0.01;
            default: return 1;
        }
    }

    private String getToleranceValue(String color) {
        switch (color) {
            case "Marrón": return "1%";
            case "Rojo": return "2%";
            case "Verde": return "0.5%";
            case "Azul": return "0.25%";
            case "Violeta": return "0.1%";
            case "Gris": return "0.05%";
            case "Oro": return "5%";
            case "Plata": return "10%";
            case "Ninguno": return "20%";
            default: return "0%";
        }
    }
}