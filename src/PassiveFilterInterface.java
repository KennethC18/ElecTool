import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PassiveFilterInterface extends JFrame {
    private JComboBox<String> typeComboBox;
    private JTextField frecCortField;
    private JTextField rField;
    private JTextField cField;
    private JLabel resultLabel;
    private PassiveFilterPanel backgroundPanel;

    public PassiveFilterInterface() {
        setTitle("Calculadora de Filtro Pasivo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(400, 350));
        setLocationRelativeTo(null);

        // Panel con la imagen de fondo
        backgroundPanel = new PassiveFilterPanel("resources/RClowpass.png");
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

        public void updateImage(String filterType) {
            String imagePath = filterType.equals("Pasa Bajas (L_P)") ? "resources/RClowpass.png" : "resources/RChighpass.png";
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

            if (imageLoaded && backgroundImage != null) {
                g.drawImage(backgroundImage, xImage, yImage, IMAGE_WIDTH, IMAGE_HEIGHT, this);
            } else {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, panelWidth, panelHeight);
            }
        }
    }
}