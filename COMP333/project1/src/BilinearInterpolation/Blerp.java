import java.util.*;
import java.util.stream.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Blerp {
    private static int get(int self, int n)
    {
        return (self >> (n * 8)) & 0xFF;
    }

    private static float lerp(float s, float e, float t)
    {
        return s + (e - s) * t;
    }

    private static float blerp(final Float c00, float c10, float c01, float c11, float tx, float ty)
    {
        return lerp(lerp(c00, c10, tx), lerp(c01, c11, tx), ty);
    }

    private static int blerpPixel(BufferedImage src, int width, int height, int x, int y)
    {
	float gx = ((float) x) / width * (src.getWidth() - 1);
        float gy = ((float) y) / height * (src.getHeight() - 1);
        int gxi = (int) gx;
        int gyi = (int) gy;
        int c00 = src.getRGB(gxi, gyi);
        int c10 = src.getRGB(gxi + 1, gyi);
        int c01 = src.getRGB(gxi, gyi + 1);
        int c11 = src.getRGB(gxi + 1, gyi + 1);
        int rgb = 0;
	for(int i = 0; i < 4; i++)
	{
	        float b00 = get(c00, 0);
	        float b10 = get(c10, 0);
	        float b01 = get(c01, 0);
	        float b11 = get(c11, 0);
	        int ble = ((int) blerp(b00, b10, b01, b11, gx - gxi, gy - gyi)) << (8 * 0);
        	rgb = rgb | ble;
	}
	return rgb;
    }

    static BufferedImage scale(BufferedImage src, float scaleX, float scaleY)
    {
        int newWidth = (int) (src.getWidth() * scaleX);
        int newHeight = (int) (src.getHeight() * scaleY);
        BufferedImage dst = new BufferedImage(newWidth, newHeight, src.getType());

        IntStream.range(0, newWidth).parallel().forEach(x -> IntStream.range(0, newHeight).parallel().forEach( y -> {
            int rgb = blerpPixel(src, newWidth, newHeight, x, y);
            dst.setRGB(x, y, rgb);
        }));

        return dst;
    }


    public static void main(String args[]) throws IOException {
        File important = new File("IMPORTANT.jpg");
        BufferedImage image = ImageIO.read(important);
        BufferedImage image2 = scale(image, 30f, 30f);
        File scaled = new File("IMPORTANT_Scaled.jpg");
        ImageIO.write(image2, "png", scaled);
    }
}

