package eu.decentsoftware.holograms.components.line.content.objects;

import eu.decentsoftware.holograms.api.utils.color.DecentColor;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * This class represents an image. It is used to scale and parse the image.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
public class DecentImage {

    private BufferedImage bufferedImage;
    private DecentColor[][] colorField;

    /**
     * Creates a new DecentImage from a DecentColor[][] array.
     *
     * @param colorField DecentColor[][] array holding the image pixel colors.
     */
    public DecentImage(@NotNull DecentColor[][] colorField) {
        this.colorField = colorField;
    }

    /**
     * Creates a new DecentImage from a BufferedImage.
     *
     * @param image BufferedImage to create the DecentImage from.
     */
    public DecentImage(@NotNull BufferedImage image) {
        this.bufferedImage = image;
        this.parseColorField();
    }

    /**
     * Returns the image width in pixels;
     *
     * @return Image width in pixels.
     */
    public int getWidth() {
        return bufferedImage.getWidth();
    }

    /**
     * Returns the image height in pixels;
     *
     * @return Image height in pixels.
     */
    public int getHeight() {
        return bufferedImage.getHeight();
    }

    /**
     * Returns the image as a String array of lines.
     *
     * @return String array representing the image.
     */
    public String[] toStringLines() {
        String[] lines = new String[bufferedImage.getHeight()];
        StringBuilder line;
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            line = new StringBuilder();
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                String color = Version.supportsHex() ? colorField[y][x].toHex() : colorField[y][x].toChatColor().toString();
                line.append(color).append("\u2588");
            }
            lines[y] = line.toString();
        }
        return lines;
    }

    /**
     * Converts the BufferedImage to a DecentColor[][] array holding the image pixel colors.
     */
    public void parseColorField() {
        this.colorField = new DecentColor[bufferedImage.getHeight()][bufferedImage.getWidth()];
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                this.colorField[y][x] = new DecentColor(rgb);
            }
        }
    }

    /**
     * Scales the image to the given width.
     *
     * @param width Width to scale the image to.
     */
    public void scale(int width) {
        this.scale((float) width / this.bufferedImage.getWidth());
    }

    /**
     * Scales the image to the given scale.
     *
     * @param scale Scale to scale the image to.
     */
    public void scale(float scale) {
        BufferedImage before = this.bufferedImage;
        int width = (int) (bufferedImage.getWidth() * scale);
        int height = (int) (bufferedImage.getHeight() * scale);
        BufferedImage after = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        this.bufferedImage = scaleOp.filter(before, after);
        this.parseColorField();
    }

}
