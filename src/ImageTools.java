import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class ImageTools {

	private static final BufferedImage[] ANDImages = new BufferedImage[4];
	private static final BufferedImage[] ORImages = new BufferedImage[4];
	private static final BufferedImage[] NOTImages = new BufferedImage[2];
	public static BufferedImage ON;
	public static BufferedImage OFF;
	public static final int GATE_WIDTH = 600 / 4, GATE_HEIGHT = 360 / 4;

	public static void loadImages() {
		
		int counter = 0;
		for (String s : new String[]{"Labeled","IN1","IN2","IN1_IN2"})
			try{
				ANDImages[counter] = ImageIO.read(ImageTools.class.getResourceAsStream("/images/AND/AND_" + s + "_Gate.png"));
				ORImages[counter++] = ImageIO.read(ImageTools.class.getResourceAsStream("/images/OR/OR_" + s + "_Gate.png"));
			}catch(Exception e){
				e.printStackTrace();
			}
		
		counter = 0;
		for (String s : new String[]{"INPUT_ON", "INPUT_OFF"})
			try{
				NOTImages[counter++] = ImageIO.read(ImageTools.class.getResourceAsStream("/images/NOT/NOT_" + s + "_Gate.png"));
			}catch(Exception e){
				e.printStackTrace();
			}
		
		try {
			ON = ImageIO.read(ImageTools.class.getResourceAsStream("/images/SWITCH/ON.png"));
			OFF = ImageIO.read(ImageTools.class.getResourceAsStream("/images/SWITCH/OFF.png"));
		} catch (Exception e) {
			ON = OFF = getDefaultImage(new Dimension(GATE_WIDTH, GATE_HEIGHT));
			e.printStackTrace();
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

	public static BufferedImage[] getGateImageGroup(Gates type) {
		if (type.equals(Gates.AND))
			return ANDImages;
		else if (type.equals(Gates.OR))
			return ORImages;
		else if (type.equals(Gates.NOT))
			return NOTImages;
		else {
			System.err.println("Image does not exist");
			return null;
		}
	}

}
