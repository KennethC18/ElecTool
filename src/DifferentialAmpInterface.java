import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DifferentialAmpInterface extends JFrame {
    private JTextField ganDifField;
    private JTextField r1Field;
    private JTextField r2Field;
    private JLabel resultLabel;

    public DifferentialAmpInterface() {
        setTitle("Calculadora de Amplificador Diferencial");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null); // Centrar la ventana

        // Panel para los controles de entrada
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Campos de entrada
        inputPanel.add(new JLabel("Ganancia Diferencial (ganDif):"));
        ganDifField = new JTextField("1.0");
        inputPanel.add(ganDifField);

        inputPanel.add(new JLabel("Resistencia R1 (Ω):"));
        r1Field = new JTextField("10000.0");
        inputPanel.add(r1Field);

        inputPanel.add(new JLabel("Resistencia R2 (Ω):"));
        r2Field = new JTextField("");
        inputPanel.add(r2Field);

        // Botón para calcular
        JButton calculateButton = new JButton("Calcular");
        calculateButton.addActionListener(e -> calculateDifferentialAmp());
        inputPanel.add(calculateButton);

        // Etiqueta para mostrar el resultado
        resultLabel = new JLabel("Resultado: Ingrese valores y presione Calcular");
        inputPanel.add(resultLabel);

        add(inputPanel, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    private void calculateDifferentialAmp() {
        try {
            // Obtener valores de los campos
            double ganDif = Double.parseDouble(ganDifField.getText().trim());
            String r1Text = r1Field.getText().trim();
            String r2Text = r2Field.getText().trim();

            // Validar que solo uno de R1 o R2 esté especificado
            if (r1Text.isEmpty() && r2Text.isEmpty()) {
                throw new IllegalArgumentException("Especifique R1 o R2");
            }
            if (!r1Text.isEmpty() && !r2Text.isEmpty()) {
                throw new IllegalArgumentException("Especifique solo R1 o R2, no ambos");
            }

            // Crear instancia de AmpDif
            Double r1 = r1Text.isEmpty() ? null : Double.parseDouble(r1Text);
            Double r2 = r2Text.isEmpty() ? null : Double.parseDouble(r2Text);
            AmpDif amp = new AmpDif(ganDif, r1, r2);

            // Calcular
            amp.calcular();

            // Mostrar resultado
            resultLabel.setText(String.format("Resultado: R1 = %.1f Ω, R2 = %.1f Ω", amp.getR1(), amp.getR2()));
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
}