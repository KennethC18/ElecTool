package view;

import util.ResourceLoader;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class PassiveFilterPanel extends JPanel {
    private Image backgroundImage;
    private boolean imageLoaded;
    private double rValue;
    private double cValue;
    private String filterType;
    private static final int IMAGE_WIDTH = 600;
    private static final int IMAGE_HEIGHT = 380;

    public PassiveFilterPanel(String initialImagePath) {
        setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT + 150));
        loadImage(initialImagePath);
        this.rValue = 0.0;
        this.cValue = 0.0;
        this.filterType = "Pasa Bajas (L_P)";
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
        this.filterType = filterType;
        repaint();
    }

    public void updateValues(double rValue, double cValue, String filterType) {
        this.rValue = rValue;
        this.cValue = cValue;
        this.filterType = filterType;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int xImage = (panelWidth - IMAGE_WIDTH) / 2;
        int yImage = (panelHeight - IMAGE_HEIGHT - 150) / 2;

        if (imageLoaded && backgroundImage != null) {
            g.drawImage(backgroundImage, xImage, yImage, IMAGE_WIDTH, IMAGE_HEIGHT, this);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, panelWidth, panelHeight);
        }

        if (rValue > 0 && cValue > 0) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));

            String rText = formatResistance(rValue);
            String cText = formatCapacitance(cValue);

            int upperLeftX = xImage + 140;
            int upperLeftY = yImage + 180;
            int rightCenterX = xImage + 360;
            int rightCenterY = yImage + 240;

            if (filterType.equals("Pasa Bajas (L_P)")) {
                g.drawString(rText, upperLeftX, upperLeftY);
                g.drawString(cText, rightCenterX, rightCenterY);
            } else {
                g.drawString(cText, upperLeftX, upperLeftY);
                g.drawString(rText, rightCenterX, rightCenterY);
            }
        }
    }

    private String formatResistance(double resistance) {
        if (resistance >= 1_000_000) {
            return String.format("%.1fM Ω", resistance / 1_000_000);
        } else if (resistance >= 1_000) {
            return String.format("%.1fk Ω", resistance / 1_000);
        } else {
            return String.format("%.1f Ω", resistance);
        }
    }

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