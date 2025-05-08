import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ResistorInterface extends JFrame {
    private ResistorPanel resistorPanel;
    private JComboBox<String>[] comboBoxes = new JComboBox[4];
    private JLabel resistanceLabel; // JLabel para mostrar la resistencia equivalente

    public ResistorInterface() {
        setTitle("Calculadora de Resistencias");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear el panel de la resistencia con la imagen de fondo
        resistorPanel = new ResistorPanel("resources/resistor.png");
        add(resistorPanel, BorderLayout.CENTER);

        // Panel para los controles
        JPanel controlPanel = new JPanel(new GridLayout(5, 2, 5, 5)); // 5 filas para incluir la resistencia
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Definir los colores posibles para cada banda
        String[] coloresDigitos = {"Negro", "Marrón", "Rojo", "Naranja", "Amarillo", "Verde", "Azul", "Violeta", "Gris", "Blanco"};
        String[] coloresMultiplicador = {"Negro", "Marrón", "Rojo", "Naranja", "Amarillo", "Verde", "Azul", "Violeta", "Gris", "Blanco", "Oro", "Plata"};
        String[] coloresTolerancia = {"Marrón", "Rojo", "Verde", "Azul", "Violeta", "Gris", "Oro", "Plata", "Ninguno"};

        // Inicializar el JLabel para la resistencia equivalente
        controlPanel.add(new JLabel("Resistencia:"));
        resistanceLabel = new JLabel("0 Ω ± 0%"); // Valor inicial
        controlPanel.add(resistanceLabel);

        // Inicializar los JComboBox
        comboBoxes[0] = new JComboBox<>(coloresDigitos);    // Banda 1: Dígito
        comboBoxes[1] = new JComboBox<>(coloresDigitos);    // Banda 2: Dígito
        comboBoxes[2] = new JComboBox<>(coloresMultiplicador); // Banda 3: Multiplicador
        comboBoxes[3] = new JComboBox<>(coloresTolerancia); // Banda 4: Tolerancia

        // Configurar los controles y asociarlos a las bandas
        for (int i = 0; i < 4; i++) {
            controlPanel.add(new JLabel("Banda " + (i + 1) + ":"));
            controlPanel.add(comboBoxes[i]);
            comboBoxes[i].setSelectedIndex(0); // Seleccionar el primer color por defecto
        }

        // Añadir los ActionListener después de establecer los índices iniciales
        for (int i = 0; i < 4; i++) {
            final int index = i;
            comboBoxes[i].addActionListener(e -> {
                String selected = (String) comboBoxes[index].getSelectedItem();
                Color color = getColorFromString(selected);
                resistorPanel.setBandColor(index, color); // Actualizar color
                updateResistanceLabel(); // Actualizar el valor de la resistencia
            });
        }

        add(controlPanel, BorderLayout.SOUTH);

        // Ajustar el tamaño de la ventana para acomodar la imagen y los controles
        pack();
        setMinimumSize(new Dimension(850, 300)); // Aumentado para más espacio
        setLocationRelativeTo(null); // Centrar la ventana
        setVisible(true);

        // Actualizar la resistencia inicial
        updateResistanceLabel();
    }

    // Método para convertir el nombre del color a un objeto Color
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

    // Método para calcular y actualizar la resistencia equivalente
    private void updateResistanceLabel() {
        // Obtener los colores seleccionados
        String banda1 = (String) comboBoxes[0].getSelectedItem();
        String banda2 = (String) comboBoxes[1].getSelectedItem();
        String multiplicador = (String) comboBoxes[2].getSelectedItem();
        String tolerancia = (String) comboBoxes[3].getSelectedItem();

        // Calcular los valores numéricos
        int digito1 = getDigitValue(banda1);
        int digito2 = getDigitValue(banda2);
        double multiplicadorValor = getMultiplierValue(multiplicador);
        String toleranciaValor = getToleranceValue(tolerancia);

        // Calcular la resistencia
        double resistencia = ((digito1 * 10) + digito2) * multiplicadorValor;

        // Formatear el valor de la resistencia
        String resistenciaTexto;
        if (resistencia >= 1_000_000) {
            resistenciaTexto = String.format("%.2f MΩ", resistencia / 1_000_000);
        } else if (resistencia >= 1_000) {
            resistenciaTexto = String.format("%.2f kΩ", resistencia / 1_000);
        } else {
            resistenciaTexto = String.format("%.2f Ω", resistencia);
        }

        // Actualizar el JLabel
        resistanceLabel.setText(resistenciaTexto + " ± " + toleranciaValor);
    }

    // Método para obtener el valor numérico de un dígito
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

    // Método para obtener el valor del multiplicador
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

    // Método para obtener el valor de la tolerancia
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

    public static void main(String[] args) {
        // Ejecutar la interfaz en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new ResistorInterface());
    }
}