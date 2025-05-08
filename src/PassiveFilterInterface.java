import javax.swing.*;
import java.awt.*;

public class PassiveFilterInterface extends JFrame {
    private JComboBox<String> typeComboBox;
    private JTextField frecCortField;
    private JTextField rField;
    private JTextField cField;
    private JLabel resultLabel;

    public PassiveFilterInterface() {
        setTitle("Calculadora de Filtro Pasivo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(400, 350));
        setLocationRelativeTo(null); // Centrar la ventana

        // Panel para los controles de entrada
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
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

        // Campo para la resistencia
        inputPanel.add(new JLabel("Resistencia R (Ω):"));
        rField = new JTextField("10000.0");
        inputPanel.add(rField);

        // Campo para el capacitor
        inputPanel.add(new JLabel("Capacitancia C (F):"));
        cField = new JTextField("");
        inputPanel.add(cField);

        // Botón para calcular
        JButton calculateButton = new JButton("Calcular");
        calculateButton.addActionListener(e -> calculatePassiveFilter());
        inputPanel.add(calculateButton);

        // Etiqueta para mostrar el resultado
        resultLabel = new JLabel("Resultado: Ingrese valores y presione Calcular");
        inputPanel.add(resultLabel);

        add(inputPanel, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    private void calculatePassiveFilter() {
        try {
            // Obtener valores de los campos
            String typeText = (String) typeComboBox.getSelectedItem();
            double frecCort = Double.parseDouble(frecCortField.getText().trim());
            String rText = rField.getText().trim();
            String cText = cField.getText().trim();

            // Validar que solo uno de R o C esté especificado
            if (rText.isEmpty() && cText.isEmpty()) {
                throw new IllegalArgumentException("Especifique R o C");
            }
            if (!rText.isEmpty() && !cText.isEmpty()) {
                throw new IllegalArgumentException("Especifique solo R o C, no ambos");
            }

            // Determinar el tipo de filtro
            TipoFiltroPasivo tipo = typeText.equals("Pasa Bajas (L_P)") ? TipoFiltroPasivo.L_P : TipoFiltroPasivo.H_P;

            // Crear instancia de FiltroPasivo
            Double r = rText.isEmpty() ? null : Double.parseDouble(rText);
            Double c = cText.isEmpty() ? null : Double.parseDouble(cText);
            FiltroPasivo filter = new FiltroPasivo(tipo, frecCort, r, c);

            // Calcular
            filter.calcular();

            // Mostrar resultado
            resultLabel.setText(String.format("Resultado: R = %.1f Ω, C = %.3e F", filter.getR(), filter.getC()));
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