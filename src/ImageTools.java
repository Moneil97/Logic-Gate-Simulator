import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class ImageTools {

	private static final BufferedImage[] ANDImages = new BufferedImage[4];
	private static final BufferedImage[] ORImages = new BufferedImage[4];
	private static final BufferedImage[] NOTImages = new BufferedImage[2];
	private static final BufferedImage[] XORImages = new BufferedImage[4];
	public static final BufferedImage[] HALF_ADDER_IMAGES = new BufferedImage[4];
	public static BufferedImage ON;
	public static BufferedImage OFF;
	//public static BufferedImage SPLITTER;
	static BufferedImage HUB;
	static BufferedImage LCD_ON, LCD_OFF;
	public static final int GATE_WIDTH = 600 / 4, GATE_HEIGHT = 360 / 4;

	public static void loadImages() {
		
		int counter = 0;
		for (String s : new String[]{"","_IN1","_IN2","_IN1_IN2"})
			try{
				ANDImages[counter] = ImageIO.read(ImageTools.class.getResourceAsStream("/images/AND/AND" + s + "_Gate.png"));
				ORImages[counter] = ImageIO.read(ImageTools.class.getResourceAsStream("/images/OR/OR" + s + "_Gate.png"));
				XORImages[counter++] = ImageIO.read(ImageTools.class.getResourceAsStream("/images/XOR/XOR" + s + "_Gate.png"));
			}catch(Exception e){
				e.printStackTrace();
			}
		
		counter = 0;
		for (String s : new String[]{"1", "2", "3", "4"})
			try{
				HALF_ADDER_IMAGES[counter++] = ImageIO.read(ImageTools.class.getResourceAsStream("/images/HALF_ADDER/Half_Adder_" + s + ".png"));
			}catch(Exception e){
				e.printStackTrace();
			}
		
		counter = 0;
		for (String s : new String[]{"INPUT_OFF", "INPUT_ON"})
			try{
				NOTImages[counter++] = ImageIO.read(ImageTools.class.getResourceAsStream("/images/NOT/NOT_" + s + "_Gate.png"));
			}catch(Exception e){
				e.printStackTrace();
			}
		
		try {
			ON = ImageIO.read(ImageTools.class.getResourceAsStream("/images/SWITCH/ON.png"));
			OFF = ImageIO.read(ImageTools.class.getResourceAsStream("/images/SWITCH/OFF.png"));
			//SPLITTER = ImageIO.read(ImageTools.class.getResourceAsStream("/images/SPLITTER/Splitter.png"));
			HUB = ImageIO.read(ImageTools.class.getResourceAsStream("/images/HUB/HUB.png"));
			LCD_ON = ImageIO.read(ImageTools.class.getResourceAsStream("/images/LCD/LCD_ON.png"));
			LCD_OFF = ImageIO.read(ImageTools.class.getResourceAsStream("/images/LCD/LCD_OFF.png"));
		} catch (Exception e) {
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
		else if (type.equals(Gates.XOR))
			return XORImages;
		else if (type.equals(Gates.HALF_ADDER))
			return HALF_ADDER_IMAGES;
		else {
			System.err.println("Image does not exist");
			return null;
		}
	}

}
