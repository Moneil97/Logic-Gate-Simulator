import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Gate extends EComponent {

	BufferedImage image;
	//Input inTop = new Input(), inBottom = new Input();
	//Output out = new Output();
	private final int top = 0, bottom = 1;
	private boolean[] hovers = { false, false, false };
	private float[][] hoverRatios;
	private float[][] boundsRatios;
	//private Rectangle topInputHover, bottomInputHover, outputHover;
	private Polygon bounds;

	public Gate(int x, int y, BufferedImage image) {
		super(x, y, 600 / 4, 360 / 4, 2, 1);
		this.image = image;
	}

	@Override
	void update() {
		checkHover();
		outputs[0].setState(calculateState());
		// say(inTop + " " + inTop.getState() + " + " + inBottom + " " +
		// inBottom.getState() + " = " + out.state + " " + out);
		//say(this + " " + inputs[top].getState() + " + " + inputs[bottom].getState() + " = " + outputs[0].state);
	}

	abstract States calculateState();

	@Override
	public void translate(int xOff, int yOff) {
		super.translate(xOff, yOff);
	}

	@Override
	public void drop() {
		super.drop();
		generateBounds();
		generateHovers();
	}

	public void checkHover() {
		hovers[0] = inputHovers[top].contains(Simulator.mouse);
		hovers[1] = inputHovers[bottom].contains(Simulator.mouse);
		hovers[2] = outputHovers[0].contains(Simulator.mouse);
		// say(Arrays.toString(hovers));
	}

	@Override
	void draw(Graphics2D g) {
		g.drawImage(image, x, y, width, height, null);
		g.setColor(Color.blue);
		if (hovers[0])
			g.draw(inputHovers[top]);
		if (hovers[1])
			g.draw(inputHovers[bottom]);
		if (hovers[2])
			g.draw(outputHovers[0]);
	}

	public void setBoundsRatios(float[][] ratios) {
		boundsRatios = ratios;
		generateBounds();
	}

	private void generateBounds() {

		int size = boundsRatios[0].length;

		int xs[] = new int[size];
		int ys[] = new int[size];

		for (int i = 0; i < size; i++) {
			xs[i] = Math.round(x + boundsRatios[0][i] * width);
			ys[i] = Math.round(y + boundsRatios[1][i] * height);
		}

		bounds = new Polygon(xs, ys, size);
	}

	public void setHoverRatios(float[][] ratios) {
		hoverRatios = ratios;
		generateHovers();
	}

	Rectangle inputHovers[] = new Rectangle[2];
	Rectangle outputHovers[]= new Rectangle[1];
	
	protected void generateHovers() {

		int size = hoverRatios[0].length;

		int xs[] = new int[size];
		int ys[] = new int[size];

		for (int i = 0; i < size; i++) {
			xs[i] = Math.round(x + hoverRatios[0][i] * width);
			ys[i] = Math.round(y + hoverRatios[1][i] * height);
		}

		inputHovers[top] = new Rectangle(xs[0], ys[0], xs[1] - xs[0], ys[2] - ys[1]);

		for (int i = 0; i < size; i++) {
			xs[i] = Math.round(x + hoverRatios[2][i] * width);
			ys[i] = Math.round(y + hoverRatios[3][i] * height);
		}

		inputHovers[bottom] = new Rectangle(xs[0], ys[0], xs[1] - xs[0], ys[2] - ys[1]);

		for (int i = 0; i < size; i++) {
			xs[i] = Math.round(x + hoverRatios[4][i] * width);
			ys[i] = Math.round(y + hoverRatios[5][i] * height);
		}

		outputHovers[0] = new Rectangle(xs[0], ys[0], xs[1] - xs[0], ys[2] - ys[1]);

	}

	@Override
	boolean checkIfClicked(Point p) {
		return (bounds.contains(p));
	}
	
	@Override
	Rectangle[] getInputHovers() {
		return inputHovers;
	}

	@Override
	Rectangle[] getOutputHovers() {
		return outputHovers;
	}

//	public void checkForMatchedOutput(Gate gateRight) {
//		if (outputHover.intersects(gateRight.bottomInputHover))
//			say(this + "is paired with " + gateRight);
//
//		if (outputHover.intersects(gateRight.topInputHover)) {
//			say(this + "is paired with " + gateRight);
//			// connectToTopInput(gateRight.out);
//			gateRight.inputs[0].connect(this.outputs[0]);
//		}
//
//	}

//	public void connectToTopInput(Output out) {
//		inputs[0].connect(out);
//		say(inputs[0].getState());
//	}
}

class AND extends Gate {

	public AND(int x, int y) {
		super(x, y, ImageTools.getGateImage(Gates.AND));
		setHoverRatios(RatioGroups.AND_GATE_HOVER_RATIOS.getRatioGroup());
		setBoundsRatios(RatioGroups.AND_GATE_BOUNDS_RATIOS.getRatioGroup());
	}

	@Override
	States calculateState() {
		return States.getEnum(inputs[0].getState() == States.ON && inputs[1].getState() == States.ON);
	}

	

}

class OR extends Gate {
	public OR(int x, int y) {
		super(x, y, ImageTools.getGateImage(Gates.OR));
		setHoverRatios(RatioGroups.OR_GATE_HOVER_RATIOS.getRatioGroup());
		setBoundsRatios(RatioGroups.OR_GATE_BOUNDS_RATIOS.getRatioGroup());
	}

	@Override
	States calculateState() {
		return States.getEnum(inputs[0].getState() == States.ON || inputs[1].getState() == States.ON);
	}
}
