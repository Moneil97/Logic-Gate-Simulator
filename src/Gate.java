import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public abstract class Gate extends EComponent {

	private BufferedImage[] images;
	private final int TOP = 0, BOTTOM = 1;
	private final int NONE = 0, IN1 = 1, IN2 = 2, BOTH = 3;
	private boolean[] hovers;
	protected float[][] hoverRatios;
	protected float[][] boundsRatios;
	private Polygon bounds;
	protected Rectangle inputHovers[];
	protected Rectangle outputHovers[];

	public Gate(int x, int y, Gates type) {
		this(x, y, type, 2, 1);
	}

	public Gate(int x, int y, Gates type, int inputs, int outputs) {
		this(x, y, 600 / 4, 360 / 4, type, inputs, outputs);
	}
	
	public Gate(int x, int y,int width, int height, Gates type, int inputs, int outputs) {
		super(x, y, width, height, inputs, outputs);
		hovers = new boolean[inputs + outputs];
		hoverRatios = getHoverRatios();
		inputHovers = new Rectangle[inputs];
		outputHovers = new Rectangle[outputs];
		generateHovers();
		boundsRatios = getBoundsRatios();
		generateBounds();
		images = ImageTools.getGateImageGroup(type);
	}

	public abstract float[][] getBoundsRatios();
	public abstract float[][] getHoverRatios();

	@Override
	void update() {
		checkHover();
		outputs[0].setState(calculateState());
		//annoyingOutput();
	}

	@SuppressWarnings("unused")
	private void annoyingOutput() {
		/*if (inputs.length > 1)
			say(inputs[0] + " " + inputs[0].getState() + " + " + inputs[1] + " "
					+ inputs[1].getState() + " == " + outputs[0].getState() + " "+ outputs[0]
					+ "                 "+
					inputs[0].getOut() + inputs[0].getOut().getState() + "  +  " + inputs[1].getOut() + (inputs[1].getOut().getState()));
		else
			say(inputs[0] + " " + inputs[0].getState() + " --> " + outputs[0].getState() + " "+ outputs[0]);*/
	}

	abstract States calculateState();

	
	
//	@Override
//	public void drop() {
//		super.drop();
//		generateBounds();
//		generateHovers();
//	}
	//TODO check which is faster on a slow computer
	
	@Override
	public void translate(int xOff, int yOff) {
		super.translate(xOff, yOff);
		bounds.translate(xOff, yOff);
		
		for (int i=0; i < inputHovers.length; i++)
			inputHovers[i].translate(xOff, yOff);
		for (int i=0; i < outputHovers.length; i++)
			outputHovers[i].translate(xOff, yOff);
	
	}

	public void checkHover() {

		for (int i = 0; i < inputs.length; i++)
			hovers[i] = inputHovers[i].contains(Simulator.mouse);
		// hovers[0] = inputHovers[TOP].contains(Simulator.mouse);
		// hovers[1] = inputHovers[BOTTOM].contains(Simulator.mouse);

		for (int i = inputs.length, j = 0; j < outputs.length; i++, j++)
			hovers[i] = outputHovers[j].contains(Simulator.mouse);
		// hovers[2] = outputHovers[0].contains(Simulator.mouse);
		// say(Arrays.toString(hovers));
	}

	@Override
	void draw(Graphics2D g) {
		int sub = (inputs[TOP].getState().getBoolean() ? (inputs.length == 1 ? 1 : inputs[BOTTOM].getState().getBoolean() ? BOTH : IN1)
				: (inputs.length == 1 ? 0 : inputs[BOTTOM].getState().getBoolean() ? IN2 : NONE));
		g.drawImage(images[sub], x, y, width, height, null);
		g.setColor(Color.blue);

		for (int i = 0; i < inputs.length; i++)
			if (hovers[i])
				g.draw(inputHovers[i]);

		for (int i = inputs.length, j = 0; j < outputs.length; i++, j++)
			if (hovers[i])
				g.draw(outputHovers[j]);
		
		g.setColor(Color.blue);
		g.drawString(Arrays.toString(inputs), x,y);
		g.drawString(Arrays.toString(outputs), x+width/2, y+height);
	}

	protected void generateBounds() {

		int size = boundsRatios[0].length;

		int xs[] = new int[size];
		int ys[] = new int[size];

		for (int i = 0; i < size; i++) {
			xs[i] = Math.round(x + boundsRatios[0][i] * width);
			ys[i] = Math.round(y + boundsRatios[1][i] * height);
		}

		bounds = new Polygon(xs, ys, size);
	}

	protected void generateHovers() {

		int size = 4; // Rectangle

		int xs[] = new int[size];
		int ys[] = new int[size];
		int counter = 0;

		for (int k = 0; k < inputHovers.length; k++) {
			for (int i = 0; i < size; i++) {
				xs[i] = Math.round(x + hoverRatios[counter][i] * width);
				ys[i] = Math.round(y + hoverRatios[counter + 1][i] * height);
			}

			counter += 2;

			inputHovers[k] = new Rectangle(xs[0], ys[0], xs[1] - xs[0], ys[2] - ys[1]);
		}

		for (int k = 0; k < outputHovers.length; k++) {
			for (int i = 0; i < size; i++) {
				xs[i] = Math.round(x + hoverRatios[counter][i] * width);
				ys[i] = Math.round(y + hoverRatios[counter + 1][i] * height);
			}

			counter += 2;

			outputHovers[k] = new Rectangle(xs[0], ys[0], xs[1] - xs[0], ys[2] - ys[1]);
		}

	}

	@Override
	boolean contains(Point p) {
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
		ID = AND_Counter++;
	}

	@Override
	States calculateState() {
		return States.getEnum(inputs[0].getState().getBoolean() && inputs[1].getState().getBoolean());
	}

	public String toString() {
		return "AND[" + ID + "]";
	}

	@Override
	public float[][] getHoverRatios() {
		return RatioGroups.AND_GATE_HOVER_RATIOS.getRatioGroup();
	}

	@Override
	public float[][] getBoundsRatios() {
		return RatioGroups.AND_GATE_BOUNDS_RATIOS.getRatioGroup();
	}

}

class OR extends Gate {

	public static int OR_Counter = 0;
	final int ID;

	public OR(int x, int y) {
		super(x, y, Gates.OR);
		ID = OR_Counter++;
	}

	@Override
	States calculateState() {
		return States.getEnum(inputs[0].getState().getBoolean() || inputs[1].getState().getBoolean());
	}

	public String toString() {
		return "OR[" + ID + "]";
	}

	@Override
	public float[][] getHoverRatios() {
		return RatioGroups.OR_GATE_HOVER_RATIOS.getRatioGroup();
	}

	@Override
	public float[][] getBoundsRatios() {
		return RatioGroups.OR_GATE_BOUNDS_RATIOS.getRatioGroup();
	}
}

class NOT extends Gate {

	public NOT(int x, int y) {
		super(x, y, Gates.NOT, 1, 1);
	}

	@Override
	States calculateState() {
		return States.getEnum(!inputs[0].getState().getBoolean());
	}

	@Override
	public float[][] getHoverRatios() {
		return RatioGroups.NOT_GATE_HOVER_RATIOS.getRatioGroup();
	}

	@Override
	public float[][] getBoundsRatios() {
		return RatioGroups.NOT_GATE_BOUNDS_RATIOS.getRatioGroup();
	}

}

class XOR extends Gate{
	public XOR(int x, int y) {
		super(x, y, Gates.XOR);
	}
	
	@Override
	States calculateState() {
		//say(inputs[0].getState() + " + " + inputs[1].getState() + " = " + States.getEnum(inputs[0].getState().getBoolean() ^ inputs[1].getState().getBoolean()));
		return States.getEnum(inputs[0].getState().getBoolean() ^ inputs[1].getState().getBoolean());
	}

	@Override
	public float[][] getHoverRatios() {
		return RatioGroups.XOR_GATE_HOVER_RATIOS.getRatioGroup();
	}

	@Override
	public float[][] getBoundsRatios() {
		return RatioGroups.XOR_GATE_BOUNDS_RATIOS.getRatioGroup();
	}

	
}
