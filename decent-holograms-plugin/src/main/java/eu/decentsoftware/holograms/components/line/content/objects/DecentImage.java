package eu.decentsoftware.holograms.components.line.content.objects;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.utils.color.DecentColor;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * This class represents an image. It is used to scale and parse the image.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
public class DecentImage {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();

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
     * Scales the image to the given height. Width is scaled to keep the aspect ratio.
     *
     * @param height Height to scale the image to.
     */
    public void scale(int height) {
        this.scale((float) height / this.bufferedImage.getHeight());
    }

    /**
     * Scales the image to the given scale.
     *
     * @param scale Scale to scale the image to.
     */
    public void scale(float scale) {
        scale(scale, scale);
    }

    /**
     * Scales the image to the given width and height.
     *
     * @param width  Width to scale the image to.
     * @param height Height to scale the image to.
     */
    public void scale(int width, int height) {
        this.scale((float) width / this.bufferedImage.getWidth(), (float) height / this.bufferedImage.getHeight());
    }

    /**
     * Scales the image to the given scale.
     *
     * @param width  Scale to scale the image to.
     * @param height Scale to scale the image to.
     */
    public void scale(float width, float height) {
        BufferedImage before = this.bufferedImage;
        int w = (int) (bufferedImage.getWidth() * width);
        int h = (int) (bufferedImage.getHeight() * height);
        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        AffineTransform at = new AffineTransform();
        at.scale(w, h);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        this.bufferedImage = scaleOp.filter(before, after);
        this.parseColorField();
    }

    /**
     * Parse the given {@link String} to a {@link DecentImage}. The string
     * must be in the format of an item line content.
     *
     * @param string String to parse.
     * @return DecentImage parsed from the string.
     */
    @Nullable
    public static DecentImage fromString(@NotNull String string) {
        DecentImage decentImage = null;
        if (string.contains("--file:")) {
            String fileName = getFlagValue(string, "--file:");
            File file = new File(PLUGIN.getDataFolder(), "images/" + fileName);
            try {
                BufferedImage image = ImageIO.read(file);
                decentImage = new DecentImage(image);
            } catch (IOException ignored) {
            }
        } else if (string.contains("--url:")) {
            String url = getFlagValue(string, "--url:");
            try {
                BufferedImage image = ImageIO.read(new URL(url));
                decentImage = new DecentImage(image);
            } catch (IOException ignored) {
            }
        } else if (string.contains("--player:")) {
            String playerName = getFlagValue(string, "--player:");
            String type = "avatar";
            if (string.contains("--type:")) {
                type = getFlagValue(string, "--type:");
            }
            try {
                decentImage = fromMinotar(playerName, type);
            } catch (IOException ignored) {
            }
        }

        if (decentImage == null) {
            PLUGIN.getLogger().warning("Could not parse image from string: " + string);
            return null;
        }

        if (string.contains("--size:")) {
            String scale = getFlagValue(string, "--size:");
            try {
                int size = Integer.parseInt(scale);
                decentImage.scale(size);
            } catch (NumberFormatException ignored) {
            }
        }

        return decentImage;
    }

    @NotNull
    private static String getFlagValue(@NotNull String content, @NotNull String flag) {
        int index = content.indexOf(flag) + flag.length();
        int endIndex = content.indexOf(' ', index);
        return content.substring(index, endIndex);
    }

    /**
     * Get an image from the given URL.
     *
     * @param url URL to get the image from.
     * @return DecentImage representing the image.
     * @throws IOException If the image could not be loaded.
     */
    public static DecentImage fromURL(@NotNull URL url) throws IOException {
        return new DecentImage(ImageIO.read(url));
    }

    /**
     * Get the given players skin image of the given type. Image is grabbed from the
     * <a href="https://mc-heads.net/">MCHeads</a> website.
     *
     * @param username Username of the image.
     * @param type     Type of the image.
     * @return DecentImage representing the image.
     * @throws IOException If the image could not be loaded.
     */
    public static DecentImage fromMinotar(@NotNull String username, @NotNull String type) throws IOException {
        return fromURL(new URL("https://minotar.net/" + type + "/" + username));
    }

}
