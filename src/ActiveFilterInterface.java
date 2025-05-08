import javax.swing.*;
import java.awt.*;

public class ActiveFilterInterface extends JFrame {
    private JComboBox<String> typeComboBox;
    private JTextField frecCortField;
    private JTextField ganField;
    private JTextField r1Field;
    private JTextField cField;
    private JLabel resultLabel;

    public ActiveFilterInterface() {
        setTitle("Calculadora de Filtro Activo de 1er Orden");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(400, 400));
        setLocationRelativeTo(null); // Centrar la ventana

        // Panel para los controles de entrada
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Campo para el tipo de filtro
        inputPanel.add(new JLabel("Tipo de Filtro:"));
        String[] filterTypes = {"Pasa Bajas (L_P)", "Pasa Altas (H_P)"};
        typeComboBox = new JComboBox<>(filterTypes);
        inputPanel.add(typeComboBox);

        // Campo para la frecuencia de corte
        inputPanel.add(new JLabel("Frecuencia de Corte (Hz):"));
        frecCortField = new JTextField("1000.0");
        inputPanel.add(frecCortField);

        // Campo para la ganancia
        inputPanel.add(new JLabel("Ganancia (gan):"));
        ganField = new JTextField("2.0");
        inputPanel.add(ganField);

        // Campo para la resistencia R1
        inputPanel.add(new JLabel("Resistencia R1 (Ω):"));
        r1Field = new JTextField("10000.0");
        inputPanel.add(r1Field);

        // Campo para el capacitor C
        inputPanel.add(new JLabel("Capacitancia C (F):"));
        cField = new JTextField("");
        inputPanel.add(cField);

        // Botón para calcular
        JButton calculateButton = new JButton("Calcular");
        calculateButton.addActionListener(e -> calculateActiveFilter());
        inputPanel.add(calculateButton);

        // Etiqueta para mostrar el resultado
        resultLabel = new JLabel("Resultado: Ingrese valores y presione Calcular");
        inputPanel.add(resultLabel);

        add(inputPanel, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    private void calculateActiveFilter() {
        try {
            // Obtener valores de los campos
            String typeText = (String) typeComboBox.getSelectedItem();
            double frecCort = Double.parseDouble(frecCortField.getText().trim());
            double gan = Double.parseDouble(ganField.getText().trim());
            String r1Text = r1Field.getText().trim();
            String cText = cField.getText().trim();

            // Validar que solo uno de R1 o C esté especificado
            if (r1Text.isEmpty() && cText.isEmpty()) {
                throw new IllegalArgumentException("Especifique R1 o C");
            }
            if (!r1Text.isEmpty() && !cText.isEmpty()) {
                throw new IllegalArgumentException("Especifique solo R1 o C, no ambos");
            }

            // Determinar el tipo de filtro
            TipoFiltroActivo tipo = typeText.equals("Pasa Bajas (L_P)") ? TipoFiltroActivo.L_P : TipoFiltroActivo.H_P;

            // Crear instancia de FiltroActivo1erOrd
            Double r1 = r1Text.isEmpty() ? null : Double.parseDouble(r1Text);
            Double c = cText.isEmpty() ? null : Double.parseDouble(cText);
            FiltroActivo1erOrd filter = new FiltroActivo1erOrd(tipo, frecCort, gan, r1, c);

            // Calcular
            filter.calcular();

            // Mostrar resultado
            resultLabel.setText(String.format("Resultado: R1 = %.1f Ω, R2 = %.1f Ω, C = %.3e F", 
                                             filter.getR1(), filter.getR2(), filter.getC()));
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