import javax.swing.*;
import java.awt.*;
import java.io.File;
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
    private double rValue = 0.0; // Almacenar el valor calculado de R
    private double cValue = 0.0; // Almacenar el valor calculado de C

    public PassiveFilterInterface() {
        setTitle("Calculadora de Filtro Pasivo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(400, 350));
        setLocationRelativeTo(null);

        // Panel con la imagen de fondo
        backgroundPanel = new PassiveFilterPanel("RClowpass.png");
        
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);

        // Panel para los controles de entrada
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
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

        // Añadir el panel de entrada en la parte inferior
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

            // Almacenar valores calculados
            rValue = filter.getR();
            cValue = filter.getC();

            // Actualizar la etiqueta de resultado
            resultLabel.setText(String.format("Resultado: R = %.1f Ω, C = %.3e F", rValue, cValue));

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
    private class PassiveFilterPanel extends JPanel {
        private Image backgroundImage;
        private boolean imageLoaded;
        private static final int IMAGE_WIDTH = 600;
        private static final int IMAGE_HEIGHT = 380;

        public PassiveFilterPanel(String initialImagePath) {
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
            String imagePath = filterType.equals("Pasa Bajas (L_P)") ? "RClowpass.png" : "RChighpass.png";
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

            // Dibujar los valores de R y C sobre la imagen
            if (rValue > 0 && cValue > 0) {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 20));

                // Formatear los valores
                String rText = formatResistance(rValue);
                String cText = formatCapacitance(cValue);

                // Determinar posiciones según el tipo de filtro
                String filterType = (String) typeComboBox.getSelectedItem();
                int upperLeftX = xImage + 140;  // Cuadrante superior izquierdo
                int upperLeftY = yImage + 180;
                int rightCenterX = xImage + 360; // Centro del lado derecho
                int rightCenterY = yImage + 240;

                if (filterType.equals("Pasa Bajas (L_P)")) {
                    // R en cuadrante superior izquierdo, C en centro del lado derecho
                    g.drawString(rText, upperLeftX, upperLeftY);
                    g.drawString(cText, rightCenterX, rightCenterY);
                } else {
                    // C en cuadrante superior izquierdo, R en centro del lado derecho
                    g.drawString(cText, upperLeftX, upperLeftY);
                    g.drawString(rText, rightCenterX, rightCenterY);
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