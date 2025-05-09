package gui;

import model.FiltroActivo1erOrd;
import model.TipoFiltroActivo;
import util.ExcepcionCalculo;
import util.ResourceLoader;
import view.ActiveFilterPanel;
import javax.swing.*;
import java.awt.*;

public class ActiveFilterInterface extends JFrame {
    private JComboBox<String> typeComboBox;
    private JTextField frecCortField;
    private JTextField ganField;
    private JTextField r1Field;
    private JTextField cField;
    private JLabel resultLabel;
    private ActiveFilterPanel backgroundPanel;
    private double r1Value = 0.0;
    private double r2Value = 0.0;
    private double cValue = 0.0;

    public ActiveFilterInterface() {
        setTitle("Calculadora de Filtro Activo de 1er Orden");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(400, 400));
        setLocationRelativeTo(null);

        backgroundPanel = new ActiveFilterPanel("OpAmpLowPass.png");
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
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

        inputPanel.add(new JLabel("Ganancia (gan):"));
        ganField = new JTextField("2.0");
        inputPanel.add(ganField);

        inputPanel.add(new JLabel("Resistencia R1 (Ω):"));
        r1Field = new JTextField("10000.0");
        inputPanel.add(r1Field);

        inputPanel.add(new JLabel("Capacitancia C (F):"));
        cField = new JTextField("");
        inputPanel.add(cField);

        JButton calculateButton = new JButton("Calcular");
        calculateButton.addActionListener(e -> calculateActiveFilter());
        inputPanel.add(calculateButton);

        resultLabel = new JLabel("Resultado: Ingrese valores y presione Calcular");
        inputPanel.add(resultLabel);

        backgroundPanel.add(inputPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private void calculateActiveFilter() {
        try {
            String typeText = (String) typeComboBox.getSelectedItem();
            double frecCort = Double.parseDouble(frecCortField.getText().trim());
            double gan = Double.parseDouble(ganField.getText().trim());
            String r1Text = r1Field.getText().trim();
            String cText = cField.getText().trim();

            if (r1Text.isEmpty() && cText.isEmpty()) {
                throw new IllegalArgumentException("Especifique R1 o C");
            }
            if (!r1Text.isEmpty() && !cText.isEmpty()) {
                throw new IllegalArgumentException("Especifique solo R1 o C, no ambos");
            }

            TipoFiltroActivo tipo = typeText.equals("Pasa Bajas (L_P)") ? TipoFiltroActivo.L_P : TipoFiltroActivo.H_P;

            Double r1 = r1Text.isEmpty() ? null : Double.parseDouble(r1Text);
            Double c = cText.isEmpty() ? null : Double.parseDouble(cText);
            FiltroActivo1erOrd filter = new FiltroActivo1erOrd(tipo, frecCort, gan, r1, c);

            filter.calcular();

            r1Value = filter.getR1();
            r2Value = filter.getR2();
            cValue = filter.getC();

            resultLabel.setText(String.format("Resultado: R1 = %.1f Ω, R2 = %.1f Ω, C = %.3e F",
                    r1Value, r2Value, cValue));

            backgroundPanel.updateValues(r1Value, r2Value, cValue, typeText);
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

    public double getCValue() {
        return cValue;
    }

    public String getFilterType() {
        return (String) typeComboBox.getSelectedItem();
    }
}