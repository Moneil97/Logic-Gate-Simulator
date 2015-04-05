import java.awt.Graphics2D;

public abstract class GateGroup extends EComponent {
}

class HalfAdder extends Gate {
	
	public static final int DEFAULT_WIDTH = 300, DEFAULT_HEIGHT = 150;

	public HalfAdder(int x, int y) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public HalfAdder(int x, int y, int width, int height) {
		super(x, y, width, height, Gates.HALF_ADDER, 2, 2);
	}

	@Override
	float[][] getBoundsRatios() {
		return RatioGroups.HALF_ADDER_BOUNDS_RATIOS.getRatioGroup();
	}

	@Override
	float[][] getHoverRatios() {
		return RatioGroups.HALF_ADDER_HOVER_RATIOS.getRatioGroup();
	}

	@Override
	void draw(Graphics2D g) {

		g.drawImage(getAND() ? ImageTools.HALF_ADDER_IMAGES[3]
				: (getXOR() ? (inputs[0].getState().getBoolean() ? ImageTools.HALF_ADDER_IMAGES[1] : ImageTools.HALF_ADDER_IMAGES[2])
						: ImageTools.HALF_ADDER_IMAGES[0]), x, y, width, height, null);
	}

	@Override
	States calculateState() {
		System.err.println("Not used");
		return null;
	}

	@Override
	void update() {
		checkHover();
		outputs[0].setState(States.toState(getXOR()));
		outputs[1].setState(States.toState(getAND()));
	}

	private boolean getAND() {
		return inputs[0].getState().getBoolean() && inputs[1].getState().getBoolean();
	}

	private boolean getXOR() {
		return inputs[0].getState().getBoolean() ^ inputs[1].getState().getBoolean();
	}

}

class FullAdder extends Gate {

	public FullAdder(int x, int y, int width, int height) {
		super(x, y, width, height, Gates.FULL_ADDER);
	}

	@Override
	float[][] getBoundsRatios() {
		return null;
	}

	@Override
	float[][] getHoverRatios() {
		return null;
	}

	@Override
	States calculateState() {
		return null;
	}

}
