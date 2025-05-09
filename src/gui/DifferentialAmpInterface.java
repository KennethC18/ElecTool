package gui;

import model.AmpDif;
import util.ExcepcionCalculo;
import util.ResourceLoader;
import view.AmpDifPanel;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class DifferentialAmpInterface extends JFrame {
    private JTextField ganDifField;
    private JTextField r1Field;
    private JTextField r2Field;
    private JLabel resultLabel;
    private AmpDifPanel backgroundPanel;
    private double r1Value = 0.0;
    private double r2Value = 0.0;

    public DifferentialAmpInterface() {
        setTitle("Calculadora de Amplificador Diferencial");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);

        backgroundPanel = new AmpDifPanel("ampdif.png");
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        inputPanel.setOpaque(false);

        inputPanel.add(new JLabel("Ganancia Diferencial (ganDif):"));
        ganDifField = new JTextField("1.0");
        inputPanel.add(ganDifField);

        inputPanel.add(new JLabel("Resistencia R1 (Ω):"));
        r1Field = new JTextField("10000.0");
        inputPanel.add(r1Field);

        inputPanel.add(new JLabel("Resistencia R2 (Ω):"));
        r2Field = new JTextField("");
        inputPanel.add(r2Field);

        JButton calculateButton = new JButton("Calcular");
        calculateButton.addActionListener(e -> calculateDifferentialAmp());
        inputPanel.add(calculateButton);

        resultLabel = new JLabel("Resultado: Ingrese valores y presione Calcular");
        inputPanel.add(resultLabel);

        backgroundPanel.add(inputPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private void calculateDifferentialAmp() {
        try {
            double ganDif = Double.parseDouble(ganDifField.getText().trim());
            String r1Text = r1Field.getText().trim();
            String r2Text = r2Field.getText().trim();

            if (r1Text.isEmpty() && r2Text.isEmpty()) {
                throw new IllegalArgumentException("Especifique R1 o R2");
            }
            if (!r1Text.isEmpty() && !r2Text.isEmpty()) {
                throw new IllegalArgumentException("Especifique solo R1 o R2, no ambos");
            }

            Double r1 = r1Text.isEmpty() ? null : Double.parseDouble(r1Text);
            Double r2 = r2Text.isEmpty() ? null : Double.parseDouble(r2Text);
            AmpDif amp = new AmpDif(ganDif, r1, r2);

            amp.calcular();

            r1Value = amp.getR1();
            r2Value = amp.getR2();

            resultLabel.setText(String.format("Resultado: R1 = %.1f Ω, R2 = %.1f Ω", r1Value, r2Value));

            backgroundPanel.updateValues(r1Value, r2Value);
            backgroundPanel.repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, ingrese valores numéricos válidos",
                    "Error de Entrada",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | ExcepcionCalculo e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error de Cálculo",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public double getR1Value() {
        return r1Value;
    }

    public double getR2Value() {
        return r2Value;
    }
}