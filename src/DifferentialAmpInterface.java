import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class DifferentialAmpInterface extends JFrame {
    private JTextField ganDifField;
    private JTextField r1Field;
    private JTextField r2Field;
    private JLabel resultLabel;
    private AmpDifPanel backgroundPanel;
    private double r1Value = 0.0; // Almacenar el valor calculado de R1
    private double r2Value = 0.0; // Almacenar el valor calculado de R2

    public DifferentialAmpInterface() {
        setTitle("Calculadora de Amplificador Diferencial");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);

        // Panel con la imagen de fondo
        backgroundPanel = new AmpDifPanel("ampdif.png");
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

            // Almacenar valores calculados
            r1Value = amp.getR1();
            r2Value = amp.getR2();

            // Actualizar la etiqueta de resultado
            resultLabel.setText(String.format("Resultado: R1 = %.1f Ω, R2 = %.1f Ω", r1Value, r2Value));

            // Repintar el panel para mostrar los valores sobre la imagen
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

    // Clase interna para el panel con la imagen de fondo
    private class AmpDifPanel extends JPanel {
        private Image backgroundImage;
        private boolean imageLoaded;
        private static final int IMAGE_WIDTH = 640;
        private static final int IMAGE_HEIGHT = 340;

        public AmpDifPanel(String imagePath) {
            setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT + 150)); // Ajustado para espacio de controles
            try {
                InputStream imageFile = ResourceLoader.load(imagePath);
                if (imageFile == null) {
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

            // Dibujar la imagen de fondo
            if (imageLoaded && backgroundImage != null) {
                g.drawImage(backgroundImage, xImage, yImage, IMAGE_WIDTH, IMAGE_HEIGHT, this);
            } else {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, panelWidth, panelHeight);
            }

            // Dibujar los valores de las resistencias sobre la imagen
            if (r1Value > 0 && r2Value > 0) {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 20));

                // Formatear los valores de resistencia
                String r1Text = formatResistance(r1Value);
                String r2Text = formatResistance(r2Value);

                // Coordenadas para R1 y R2 (superiores)
                int r1X = xImage + 140;  // R1 superior
                int r1Y = yImage + 40;
                int r2X = xImage + 380;  // R2 superior
                int r2Y = yImage + 40;

                // Coordenadas para R1' y R2' (inferiores)
                int r1PrimeX = xImage + 140;  // R1' inferior, misma x que R1
                int r1PrimeY = yImage + 220;  // Cerca de la parte inferior
                int r2PrimeX = xImage + 380;  // R2' inferior, misma x que R2
                int r2PrimeY = yImage + 220;  // Cerca de la parte inferior

                // Dibujar los valores
                g.drawString(r1Text, r1X, r1Y);          // R1 superior
                g.drawString(r2Text, r2X, r2Y);          // R2 superior
                g.drawString(r1Text, r1PrimeX, r1PrimeY); // R1' inferior
                g.drawString(r2Text, r2PrimeX, r2PrimeY); // R2' inferior
            }
        }

        // Método para formatear los valores de resistencia (e.g., 10000 → "10k Ω")
        private String formatResistance(double resistance) {
            if (resistance >= 1_000_000) {
                return String.format("%.1fM Ω", resistance / 1_000_000);
            } else if (resistance >= 1_000) {
                return String.format("%.1fk Ω", resistance / 1_000);
            } else {
                return String.format("%.1f Ω", resistance);
            }
        }
    }
}