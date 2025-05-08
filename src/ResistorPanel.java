import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ResistorPanel extends JPanel {
    private Image backgroundImage; // Imagen de fondo
    private boolean imageLoaded; // Indicador de si la imagen se cargó correctamente
    private Color[] bandColors = new Color[4]; // Colores de las 4 bandas

    // Dimensiones fijas de la imagen
    private static final int IMAGE_WIDTH = 800;
    private static final int IMAGE_HEIGHT = 180;

    // Constructor que recibe la ruta de la imagen
    public ResistorPanel(String imagePath) {
        // Establecer tamaño preferido del panel para acomodar la imagen
        setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));

        // Inicializar las bandas con un color por defecto (negro)
        for (int i = 0; i < 4; i++) {
            bandColors[i] = Color.BLACK;
        }

        // Intentar cargar la imagen
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

        // Calcular la posición para centrar la imagen
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int xImage = (panelWidth - IMAGE_WIDTH) / 2; // Centrar horizontalmente
        int yImage = (panelHeight - IMAGE_HEIGHT) / 2; // Centrar verticalmente

        // Dibujar fondo
        if (imageLoaded && backgroundImage != null) {
            // Dibujar la imagen con tamaño fijo
            g.drawImage(backgroundImage, xImage, yImage, IMAGE_WIDTH, IMAGE_HEIGHT, this);
        } else {
            // Fondo predeterminado si la imagen no se cargó
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, panelWidth, panelHeight);
        }

        // Dibujar las bandas de color encima
        int bandWidth = 40;  // Ancho de cada banda
        int bandHeight = 100; // Alto de cada banda
        // Ajustar la posición de las bandas para alinearse con la imagen centrada
        int startX = xImage + 250; // Ajustado según la imagen
        int y = yImage + (IMAGE_HEIGHT - bandHeight) / 2; // Centrar verticalmente respecto a la imagen
        int spacing = 30;    // Espacio entre bandas

        for (int i = 0; i < 4; i++) {
            if (i == 3) {
                g.setColor(bandColors[i]); // Establecer el color de la banda
                int x = startX + i * (bandWidth + spacing + 20); // Calcular posición X
                g.fillRect(x, y, bandWidth, bandHeight);   // Dibujar la banda
            } else {
                g.setColor(bandColors[i]); // Establecer el color de la banda
                int x = startX + i * (bandWidth + spacing); // Calcular posición X
                g.fillRect(x, y, bandWidth, bandHeight);   // Dibujar la banda
            }
        }
    }

    // Método para actualizar el color de una banda específica
    public void setBandColor(int bandIndex, Color color) {
        if (bandIndex >= 0 && bandIndex < 4) {
            bandColors[bandIndex] = color;
            repaint(); // Redibujar el panel con los nuevos colores
        }
    }
}