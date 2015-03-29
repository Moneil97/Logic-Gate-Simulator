import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Gate extends EComponent {

	BufferedImage image;
	Input inTop = new Input(), inBottom = new Input();
	Output out = new Output();
	private boolean[] hovers = { false, false, false };
	private float[][] hoverRatios;
	private float[][] boundsRatios;
	private Rectangle topInputHover, bottomInputHover, outputHover;
	private Polygon bounds;

	public Gate(int x, int y, BufferedImage image) {
		super(x, y, 600 / 4, 360 / 4);
		this.image = image;
	}

	@Override
	void update() {
		checkHover();
		out.setState(calculateState());
		// say(inTop + " " + inTop.getState() + " + " + inBottom + " " +
		// inBottom.getState() + " = " + out.state + " " + out);
		say(this + " " + inTop.getState() + " + " + inBottom.getState() + " = " + out.state);
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
		hovers[0] = bottomInputHover.contains(Simulator.mouse);
		hovers[1] = topInputHover.contains(Simulator.mouse);
		hovers[2] = outputHover.contains(Simulator.mouse);
		// say(Arrays.toString(hovers));
	}

	@Override
	void draw(Graphics2D g) {
		g.drawImage(image, x, y, width, height, null);
		g.setColor(Color.blue);
		if (hovers[0])
			g.draw(bottomInputHover);
		if (hovers[1])
			g.draw(topInputHover);
		if (hovers[2])
			g.draw(outputHover);
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

	protected void generateHovers() {

		int size = hoverRatios[0].length;

		int xs[] = new int[size];
		int ys[] = new int[size];

		for (int i = 0; i < size; i++) {
			xs[i] = Math.round(x + hoverRatios[0][i] * width);
			ys[i] = Math.round(y + hoverRatios[1][i] * height);
		}

		topInputHover = new Rectangle(xs[0], ys[0], xs[1] - xs[0], ys[2] - ys[1]);

		for (int i = 0; i < size; i++) {
			xs[i] = Math.round(x + hoverRatios[2][i] * width);
			ys[i] = Math.round(y + hoverRatios[3][i] * height);
		}

		bottomInputHover = new Rectangle(xs[0], ys[0], xs[1] - xs[0], ys[2] - ys[1]);

		for (int i = 0; i < size; i++) {
			xs[i] = Math.round(x + hoverRatios[4][i] * width);
			ys[i] = Math.round(y + hoverRatios[5][i] * height);
		}

		outputHover = new Rectangle(xs[0], ys[0], xs[1] - xs[0], ys[2] - ys[1]);

	}

	@Override
	boolean checkIfClicked(Point p) {
		return (bounds.contains(p));
	}

	public void checkForMatchedOutput(Gate gateRight) {
		if (outputHover.intersects(gateRight.bottomInputHover))
			say(this + "is paired with " + gateRight);

		if (outputHover.intersects(gateRight.topInputHover)) {
			say(this + "is paired with " + gateRight);
			// connectToTopInput(gateRight.out);
			gateRight.inTop.connect(this.out);
		}

	}

	public void connectToTopInput(Output out) {
		inTop.connect(out);
		say(inTop.getState());
	}
}

class AND extends Gate {

	public AND(int x, int y) {
		super(x, y, ImageTools.getGateImage(Gates.AND));
		setHoverRatios(RatioGroups.AND_GATE_HOVER_RATIOS.getRatioGroup());
		setBoundsRatios(RatioGroups.AND_GATE_BOUNDS_RATIOS.getRatioGroup());
	}

	@Override
	States calculateState() {
		return States.getEnum(inTop.getState() == States.ON && inBottom.getState() == States.ON);
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
		return States.getEnum(inTop.getState() == States.ON || inBottom.getState() == States.ON);
	}
}
