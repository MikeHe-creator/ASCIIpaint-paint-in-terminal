import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ASCIIpaint {

    public static String getfiles() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            String selectedPath = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println("You selected: " + selectedPath);
            frame.dispose();
            return selectedPath;
        } else {
            System.out.println("No file selected");
            frame.dispose();
            return null;
        }
    }

    public static BufferedImage graycolor() throws IOException {
        String selectedPath = getfiles();
        BufferedImage originalImage = ImageIO.read(new File(selectedPath));
        BufferedImage grayImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < originalImage.getWidth(); i++) {
            for (int j = 0; j < originalImage.getHeight(); j++) {
                int rgb = originalImage.getRGB(i, j);
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;

                int grayValue = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
                int grayRgb = (grayValue << 16) | (grayValue << 8) | grayValue;
                grayImage.setRGB(i, j, grayRgb);
            }
        }
        return grayImage;
    }

    public static int[] sizeimg(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int MAX_WIDTH = 200;
        int MAX_HEIGHT = 200;
        double ratio = Math.min((double) MAX_WIDTH / width, (double) MAX_HEIGHT / height);
        int newWidth = (int) (width * ratio);
        int newHeight = (int) (height * ratio);
        return new int[]{newWidth, newHeight};
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int newWidth, int newHeight) {
        Image tmp = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resized;
    }

    public static String pixel_to_ascii() throws IOException {
        BufferedImage grayImage = graycolor();
        int[] newSize = sizeimg(grayImage);
        BufferedImage resizedGrayImage = resizeImage(grayImage, newSize[0], newSize[1]);

        StringBuilder asciiArt = new StringBuilder();
        //String asciiChars = "@%#*+=-:. ";
        String asciiChars = " .:-=+*#%@";

        for (int y = 0; y < resizedGrayImage.getHeight(); y++) {
            for (int x = 0; x < resizedGrayImage.getWidth(); x++) {
                int gray = (resizedGrayImage.getRGB(x, y) >> 16) & 0xff;
                int charIndex = (int) ((gray / 255.0) * (asciiChars.length() - 1));
                asciiArt.append(asciiChars.charAt(charIndex));
            }
            asciiArt.append("\n");
        }
        return asciiArt.toString();
    }

    public static void main(String[] args) {
        try {
            String asciiArt = pixel_to_ascii();
            System.out.println(asciiArt);
        } catch (IOException e) {
            System.out.println("Error to handle the image: " + e.getMessage());
        }
    }
}

