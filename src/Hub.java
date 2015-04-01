import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Arrays;

public class Hub extends Gate {

	public Hub(int x, int y) {
		super(x, y, 100, 100, Gates.HUB, 1, 1);
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

		for (int i = 0; i < size; i++) {
			xs[i] = Math.round(x + hoverRatios[0][i] * width);
			ys[i] = Math.round(y + hoverRatios[1][i] * height);
		}

		outputHovers[0] = new Rectangle(xs[0], ys[0], xs[1] - xs[0], ys[2] - ys[1]);
	}
	
	@Override
	void draw(Graphics2D g) {
		g.drawImage(ImageTools.HUB, x, y, width, height, null);
		g.setColor(Color.blue);
		g.drawString(Arrays.toString(inputs), x,y);
		g.drawString(Arrays.toString(outputs), x+width/2, y+height);
	}

	@Override
	public float[][] getBoundsRatios() {
		return RatioGroups.HUB_BOUNDS_RATIOS.getRatioGroup();
	}

	@Override
	public float[][] getHoverRatios() {
		return RatioGroups.HUB_BOUNDS_RATIOS.getRatioGroup();
	}

	@Override
	States calculateState() {
		// If any input is on, output should be on
		for (Input in : inputs)
			if (in.getState().getBoolean())
				return States.ON;
		return States.OFF;

	}

}
