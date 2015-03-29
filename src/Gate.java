import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Gate extends EComponent {
	
	

	BufferedImage[] images;
	private final int TOP = 0, BOTTOM = 1;
	private final int NONE = 0, IN1 = 1, IN2 = 2, BOTH = 3;
	private boolean[] hovers = { false, false, false };
	private float[][] hoverRatios;
	private float[][] boundsRatios;
	private Polygon bounds;

	public Gate(int x, int y, Gates type) {
		super(x, y, 600 / 4, 360 / 4, 2, 1);
		images = ImageTools.getGateImageGroup(type);
	}

	@Override
	void update() {
		checkHover();
		States state = calculateState();
		outputs[0].setState(state);
		//say(state);
//		 say(inputs[0] + " " + inputs[0].getState() + " + " + inputs[1] + " " +
//				 inputs[1].getState() + " = " + outputs[0].state + " " + outputs[0]);
		// say(this + " " + inputs[top].getState() + " + " +
		// inputs[bottom].getState() + " = " + outputs[0].state);
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
		hovers[0] = inputHovers[TOP].contains(Simulator.mouse);
		hovers[1] = inputHovers[BOTTOM].contains(Simulator.mouse);
		hovers[2] = outputHovers[0].contains(Simulator.mouse);
		// say(Arrays.toString(hovers));
	}

	@Override
	void draw(Graphics2D g) {
		int sub = (inputs[TOP].getState().getBoolean() ? (inputs[BOTTOM].getState().getBoolean() ? BOTH : IN1) : (inputs[BOTTOM].getState()
				.getBoolean() ? IN2 : NONE));
		//say(sub);
		g.drawImage(images[sub], x, y, width, height, null);
		g.setColor(Color.blue);
		if (hovers[0])
			g.draw(inputHovers[TOP]);
		if (hovers[1])
			g.draw(inputHovers[BOTTOM]);
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
	Rectangle outputHovers[] = new Rectangle[1];

	protected void generateHovers() {

		int size = hoverRatios[0].length;

		int xs[] = new int[size];
		int ys[] = new int[size];

		for (int i = 0; i < size; i++) {
			xs[i] = Math.round(x + hoverRatios[0][i] * width);
			ys[i] = Math.round(y + hoverRatios[1][i] * height);
		}

		inputHovers[TOP] = new Rectangle(xs[0], ys[0], xs[1] - xs[0], ys[2] - ys[1]);

		for (int i = 0; i < size; i++) {
			xs[i] = Math.round(x + hoverRatios[2][i] * width);
			ys[i] = Math.round(y + hoverRatios[3][i] * height);
		}

		inputHovers[BOTTOM] = new Rectangle(xs[0], ys[0], xs[1] - xs[0], ys[2] - ys[1]);

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

}

class AND extends Gate {
	
	public static int AND_Counter = 0;
	final int ID;

	public AND(int x, int y) {
		super(x, y, Gates.AND);
		setHoverRatios(RatioGroups.AND_GATE_HOVER_RATIOS.getRatioGroup());
		setBoundsRatios(RatioGroups.AND_GATE_BOUNDS_RATIOS.getRatioGroup());
		ID = AND_Counter++;
	}

	@Override
	States calculateState() {
		return States.getEnum(inputs[0].getState() == States.ON && inputs[1].getState() == States.ON);
	}
	
	public String toString(){
		return "AND[" + ID + "]";
	}

}

class OR extends Gate {
	
	public static int OR_Counter = 0;
	final int ID;
	
	public OR(int x, int y) {
		super(x, y, Gates.OR);
		setHoverRatios(RatioGroups.OR_GATE_HOVER_RATIOS.getRatioGroup());
		setBoundsRatios(RatioGroups.OR_GATE_BOUNDS_RATIOS.getRatioGroup());
		ID = OR_Counter++;
	}

	@Override
	States calculateState() {
		return States.getEnum(inputs[0].getState() == States.ON || inputs[1].getState() == States.ON);
	}
	
	public String toString(){
		return "OR[" + ID + "]";
	}
}
