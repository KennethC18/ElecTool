package gui;

import model.FiltroPasivo;
import model.TipoFiltroPasivo;
import util.ExcepcionCalculo;
import util.ResourceLoader;
import view.PassiveFilterPanel;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class PassiveFilterInterface extends JFrame {
    private JComboBox<String> typeComboBox;
    private JTextField frecCortField;
    private JTextField rField;
    private JTextField cField;
    private JLabel resultLabel;
    private PassiveFilterPanel backgroundPanel;
    private double rValue = 0.0;
    private double cValue = 0.0;

    public PassiveFilterInterface() {
        setTitle("Calculadora de Filtro Pasivo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(400, 350));
        setLocationRelativeTo(null);

        backgroundPanel = new PassiveFilterPanel("RClowpass.png");
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        inputPanel.setOpaque(false);

        inputPanel.add(new JLabel("Tipo de Filtro:"));
        String[] filterTypes = {"Pasa Bajas (L_P)", "Pasa Altas (H_P)"};
        typeComboBox = new JComboBox<>(filterTypes);
        typeComboBox.addActionListener(e -> backgroundPanel.updateImage((String) typeComboBox.getSelectedItem()));
        inputPanel.add(typeComboBox);

        inputPanel.add(new JLabel("Frecuencia de Corte (Hz):"));
        frecCortField = new JTextField("1000.0");
        inputPanel.add(frecCortField);

        inputPanel.add(new JLabel("Resistencia R (Ω):"));
        rField = new JTextField("10000.0");
        inputPanel.add(rField);

        inputPanel.add(new JLabel("Capacitancia C (F):"));
        cField = new JTextField("");
        inputPanel.add(cField);

        JButton calculateButton = new JButton("Calcular");
        calculateButton.addActionListener(e -> calculatePassiveFilter());
        inputPanel.add(calculateButton);

        resultLabel = new JLabel("Resultado: Ingrese valores y presione Calcular");
        inputPanel.add(resultLabel);

        backgroundPanel.add(inputPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private void calculatePassiveFilter() {
        try {
            String typeText = (String) typeComboBox.getSelectedItem();
            double frecCort = Double.parseDouble(frecCortField.getText().trim());
            String rText = rField.getText().trim();
            String cText = cField.getText().trim();

            if (rText.isEmpty() && cText.isEmpty()) {
                throw new IllegalArgumentException("Especifique R o C");
            }
            if (!rText.isEmpty() && !cText.isEmpty()) {
                throw new IllegalArgumentException("Especifique solo R o C, no ambos");
            }

            TipoFiltroPasivo tipo = typeText.equals("Pasa Bajas (L_P)") ? TipoFiltroPasivo.L_P : TipoFiltroPasivo.H_P;

            Double r = rText.isEmpty() ? null : Double.parseDouble(rText);
            Double c = cText.isEmpty() ? null : Double.parseDouble(cText);
            FiltroPasivo filter = new FiltroPasivo(tipo, frecCort, r, c);

            filter.calcular();

            rValue = filter.getR();
            cValue = filter.getC();

            resultLabel.setText(String.format("Resultado: R = %.1f Ω, C = %.3e F", rValue, cValue));

            backgroundPanel.updateValues(rValue, cValue, (String) typeComboBox.getSelectedItem());
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

    public double getRValue() {
        return rValue;
    }

    public double getCValue() {
        return cValue;
    }

    public String getFilterType() {
        return (String) typeComboBox.getSelectedItem();
    }
}