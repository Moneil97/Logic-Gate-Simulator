import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageTools {

	private static String[] names = { "AND", "OR", "NOT" };
	private static BufferedImage[] gateImages = new BufferedImage[3];
	public static BufferedImage ON, OFF;
	public static final int GATE_WIDTH = 600 / 4, GATE_HEIGHT = 360 / 4;

	public static void loadImages() {

		for (int i = 0; i < names.length; i++)
			try {
				gateImages[i] = ImageIO.read(ImageTools.class.getResourceAsStream("/images/" + names[i] + "_Gate.png"));
				say(gateImages[i]);
			} catch (IOException e) {
				gateImages[i] = getDefaultImage(new Dimension(GATE_WIDTH, GATE_HEIGHT));
				e.printStackTrace();
			}

		try {
			ON = ImageIO.read(ImageTools.class.getResourceAsStream("/images/ON.png"));
			OFF = ImageIO.read(ImageTools.class.getResourceAsStream("/images/OFF.png"));
		} catch (IOException e) {
			ON = OFF = getDefaultImage(new Dimension(GATE_WIDTH, GATE_HEIGHT));
			e.printStackTrace();
		}
	}

	public static BufferedImage getGateImage(Gates gate) {
		if (gate.equals(Gates.AND))
			return gateImages[0];// copy(AND);
		else if (gate.equals(Gates.OR))
			return gateImages[1];
		else if (gate.equals(Gates.NOT))
			return gateImages[2];
		else {
			System.err.println("Image does not exist");
			return null;
		}

	}

	public static BufferedImage copy(BufferedImage original) {
		say(original);
		BufferedImage temp = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
		temp.getGraphics().drawImage(original, 0, 0, null);
		return temp;
	}

	public static BufferedImage getDefaultImage(Dimension d) {
		return getDefaultImage(d, Color.black);
	}

	public static BufferedImage getDefaultImage(Dimension d, Color c) {
		BufferedImage image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, d.width, d.height);
		return image;
	}

	public static void say(Object x) {
		System.out.println(x);
	}

}
