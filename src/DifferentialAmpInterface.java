import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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
        setLocationRelativeTo(null);

        // Panel con la imagen de fondo
        AmpDifPanel backgroundPanel = new AmpDifPanel("resources/ampdif.png");
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);

        // Panel para los controles de entrada
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        inputPanel.setOpaque(false); // Hacer el panel transparente para ver la imagen de fondo

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

        // Añadir el panel de entrada en la parte inferior
        backgroundPanel.add(inputPanel, BorderLayout.SOUTH);

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

    // Clase interna para el panel con la imagen de fondo
    private class AmpDifPanel extends JPanel {
        private Image backgroundImage;
        private boolean imageLoaded;
        private static final int IMAGE_WIDTH = 640;
        private static final int IMAGE_HEIGHT = 340;

        public AmpDifPanel(String imagePath) {
            setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT + 150)); // Ajustado para espacio de controles
            try {
                File imageFile = new File(imagePath);
                if (!imageFile.exists()) {
                    throw new IOException("El archivo de imagen no existe: " + imagePath);
                }
                backgroundImage = ImageIO.read(imageFile);
                imageLoaded = true;
            } catch (IOException e) {
                imageLoaded = false;
                JOptionPane.showMessageDialog(this,
                        "Error al cargar la imagen: " + e.getMessage(),
                        "Error de Imagen",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int xImage = (panelWidth - IMAGE_WIDTH) / 2; // Centrar horizontalmente
            int yImage = (panelHeight - IMAGE_HEIGHT - 150) / 2; // Centrar verticalmente, dejando espacio para controles

            if (imageLoaded && backgroundImage != null) {
                g.drawImage(backgroundImage, xImage, yImage, IMAGE_WIDTH, IMAGE_HEIGHT, this);
            } else {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, panelWidth, panelHeight);
            }
        }
    }
}