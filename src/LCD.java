import java.awt.Graphics2D;
import java.awt.Rectangle;

public class LCD extends Gate{

	public static final int DEFAULT_WIDTH = 100, DEFAULT_HEIGHT = 100;
	
	public LCD(int x, int y) {
		super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, Gates.LCD, 1, 0);
	}
	
	public LCD(int x, int y, int width, int height){
		super(x, y, width, height, Gates.LCD, 1, 0);
	}
	
	@Override
	void update() {
		checkHover();
	}
	
	@Override
	void draw(Graphics2D g) {
		g.drawImage((inputs[0].getState().getBoolean() ? ImageTools.LCD_ON: ImageTools.LCD_OFF), x, y, width, height, null);
	}
	
	@Override
	protected void generateHovers() {
		int size = 4; // Rectangle

		int xs[] = new int[size];
		int ys[] = new int[size];

		for (int i = 0; i < size; i++) {
			xs[i] = Math.round(x + hoverRatios[0][i] * width);
			ys[i] = Math.round(y + hoverRatios[1][i] * height);
		}

		inputHovers[0] = new Rectangle(xs[0], ys[0], xs[1] - xs[0], ys[2] - ys[1]);
	}

	@Override
	public float[][] getBoundsRatios() {
		return RatioGroups.LCD_BOUNDS_RATIOS.getRatioGroup();
	}

	@Override
	public float[][] getHoverRatios() {
		return RatioGroups.LCD_BOUNDS_RATIOS.getRatioGroup();
	}

	@Override
	States calculateState() {
		return inputs[0].getState();
	}



}
