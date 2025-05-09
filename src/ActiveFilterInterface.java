import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ActiveFilterInterface extends JFrame {
    private JComboBox<String> typeComboBox;
    private JTextField frecCortField;
    private JTextField ganField;
    private JTextField r1Field;
    private JTextField cField;
    private JLabel resultLabel;
    private ActiveFilterPanel backgroundPanel;
    private double r1Value = 0.0; // Almacenar el valor calculado de R1
    private double r2Value = 0.0; // Almacenar el valor calculado de R2
    private double cValue = 0.0;  // Almacenar el valor calculado de C

    public ActiveFilterInterface() {
        setTitle("Calculadora de Filtro Activo de 1er Orden");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(400, 400));
        setLocationRelativeTo(null);

        // Panel con la imagen de fondo
        backgroundPanel = new ActiveFilterPanel("OpAmpLowPass.png");
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);

        // Panel para los controles de entrada
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        inputPanel.setOpaque(false); // Hacer el panel transparente para ver la imagen de fondo

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

        // Añadir el panel de entrada en la parte inferior
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

            // Almacenar valores calculados
            r1Value = filter.getR1();
            r2Value = filter.getR2();
            cValue = filter.getC();

            // Actualizar la etiqueta de resultado
            resultLabel.setText(String.format("Resultado: R1 = %.1f Ω, R2 = %.1f Ω, C = %.3e F",
                    r1Value, r2Value, cValue));

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
    private class ActiveFilterPanel extends JPanel {
        private Image backgroundImage;
        private boolean imageLoaded;
        private static final int IMAGE_WIDTH = 600;
        private static final int IMAGE_HEIGHT = 380;

        public ActiveFilterPanel(String initialImagePath) {
            setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT + 150)); // Ajustado para espacio de controles
            loadImage(initialImagePath);
        }

        private void loadImage(String imagePath) {
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

        public void updateImage(String filterType) {
            String imagePath = filterType.equals("Pasa Bajas (L_P)") ? "OpAmpLowPass.png" : "OpAmpHighPass.png";
            loadImage(imagePath);
            repaint();
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

            // Dibujar los valores de R1, R2 y C sobre la imagen
            if (r1Value > 0 && r2Value > 0 && cValue > 0) {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 20));

                // Formatear los valores
                String r1Text = formatResistance(r1Value);
                String r2Text = formatResistance(r2Value);
                String cText = formatCapacitance(cValue);

                // Coordenadas para los valores
                int r1X = xImage + 180; // Centro a la izquierda
                int r1Y = yImage + 280;
                int r2X = xImage + 380; // Centro a la derecha
                int r2Y = yImage + 200;
                int cLowPassX = xImage + 300; // Arriba a la derecha (low-pass)
                int cLowPassY = yImage + 110;
                int cHighPassX = xImage + 90; // Arriba a la izquierda (high-pass)
                int cHighPassY = yImage + 280;

                // Determinar posiciones según el tipo de filtro
                String filterType = (String) typeComboBox.getSelectedItem();
                if (filterType.equals("Pasa Bajas (L_P)")) {
                    // Low-pass: R1 centro-izquierda, R2 centro-derecha, C arriba-derecha
                    g.drawString(r1Text, r1X, r1Y);
                    g.drawString(r2Text, r2X, r2Y);
                    g.drawString(cText, cLowPassX, cLowPassY);
                } else {
                    // High-pass: C arriba-izquierda, R1 centro-izquierda, R2 centro-derecha
                    g.drawString(cText, cHighPassX, cHighPassY);
                    g.drawString(r1Text, r1X, r1Y);
                    g.drawString(r2Text, r2X, r2Y);
                }
            }
        }

        // Formatear resistencia (e.g., 10000 → "10k Ω")
        private String formatResistance(double resistance) {
            if (resistance >= 1_000_000) {
                return String.format("%.1fM Ω", resistance / 1_000_000);
            } else if (resistance >= 1_000) {
                return String.format("%.1fk Ω", resistance / 1_000);
            } else {
                return String.format("%.1f Ω", resistance);
            }
        }

        // Formatear capacitancia (e.g., 1.592e-5 → "15.9µF")
        private String formatCapacitance(double capacitance) {
            if (capacitance < 1e-6) {
                return String.format("%.1fnF", capacitance * 1e9);
            } else if (capacitance < 1e-3) {
                return String.format("%.1fµF", capacitance * 1e6);
            } else {
                return String.format("%.1fmF", capacitance * 1e3);
            }
        }
    }
}