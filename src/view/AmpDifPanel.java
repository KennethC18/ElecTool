package view;

import util.ResourceLoader;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class AmpDifPanel extends JPanel {
    private Image backgroundImage;
    private boolean imageLoaded;
    private double r1Value;
    private double r2Value;
    private static final int IMAGE_WIDTH = 640;
    private static final int IMAGE_HEIGHT = 340;

    public AmpDifPanel(String imagePath) {
        setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT + 150));
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
        this.r1Value = 0.0;
        this.r2Value = 0.0;
    }

    public void updateValues(double r1Value, double r2Value) {
        this.r1Value = r1Value;
        this.r2Value = r2Value;
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

        if (r1Value > 0 && r2Value > 0) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));

            String r1Text = formatResistance(r1Value);
            String r2Text = formatResistance(r2Value);

            int r1X = xImage + 140;
            int r1Y = yImage + 40;
            int r2X = xImage + 380;
            int r2Y = yImage + 40;
            int r1PrimeX = xImage + 140;
            int r1PrimeY = yImage + 220;
            int r2PrimeX = xImage + 380;
            int r2PrimeY = yImage + 220;

            g.drawString(r1Text, r1X, r1Y);
            g.drawString(r2Text, r2X, r2Y);
            g.drawString(r1Text, r1PrimeX, r1PrimeY);
            g.drawString(r2Text, r2PrimeX, r2PrimeY);
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
}