import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ResistorPanel extends JPanel {
    private Image backgroundImage;
    private boolean imageLoaded;
    private Color[] bandColors = new Color[4];

    private static final int IMAGE_WIDTH = 800;
    private static final int IMAGE_HEIGHT = 180;

    public ResistorPanel(String imagePath) {
        setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        for (int i = 0; i < 4; i++) {
            bandColors[i] = Color.BLACK;
        }
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
        int xImage = (panelWidth - IMAGE_WIDTH) / 2;
        int yImage = (panelHeight - IMAGE_HEIGHT) / 2;

        if (imageLoaded && backgroundImage != null) {
            g.drawImage(backgroundImage, xImage, yImage, IMAGE_WIDTH, IMAGE_HEIGHT, this);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, panelWidth, panelHeight);
        }

        int bandWidth = 40;
        int bandHeight = 100;
        int startX = xImage + 250;
        int y = yImage + (IMAGE_HEIGHT - bandHeight) / 2;
        int spacing = 30;

        for (int i = 0; i < 4; i++) {
            if (i == 3) {
                g.setColor(bandColors[i]);
                int x = startX + i * (bandWidth + spacing + 20);
                g.fillRect(x, y, bandWidth, bandHeight);
            } else {
                g.setColor(bandColors[i]);
                int x = startX + i * (bandWidth + spacing);
                g.fillRect(x, y, bandWidth, bandHeight);
            }
        }
    }

    public void setBandColor(int bandIndex, Color color) {
        if (bandIndex >= 0 && bandIndex < 4) {
            bandColors[bandIndex] = color;
            repaint();
        }
    }
}