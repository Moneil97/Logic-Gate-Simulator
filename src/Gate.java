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
	public static final int DEFAULT_WIDTH = 600 / 4, DEFAULT_HEIGHT = 360 / 4;

	/**
	 * Create Gate with default size, 2 inputs, 1 output.
	 * @param x xPosition
	 * @param y yPosition
	 * @param type type of Gate (AND, OR...)
	 */
	
	public Gate(int x, int y, Gates type) {
		this(x, y, type, 2, 1);
	}
	
	/**
	 * Create Gate with 2 inputs, 1 output
	 * @param x xPosition
	 * @param y yPosition
	 * @param width Width of eComp
	 * @param height Height of eComp
	 * @param type type of Gate (AND, OR...)
	 */
	
	public Gate(int x, int y, int width, int height, Gates type) {
		this(x, y, width, height, type, 2, 1);
	}
	
	/**
	 * Create Gate with default size
	 * @param x xPosition
	 * @param y yPosition
	 * @param type type of Gate (AND, OR...)
	 * @param inputs Number of Inputs
	 * @param outputs Number of Outputs
	 */

	public Gate(int x, int y, Gates type, int inputs, int outputs) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, type, inputs, outputs);
	}
	
	/**
	 * Create Gate
	 * @param x xPosition
	 * @param y yPosition
	 * @param width Width of eComp
	 * @param height Height of eComp
	 * @param type type of Gate (AND, OR...)
	 * @param inputs Number of Inputs
	 * @param outputs Number of Outputs
	 */
	
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

	abstract float[][] getBoundsRatios();
	abstract float[][] getHoverRatios();
	/**
	 * Calculate whether Gate is on/off
	 * @return ON/OFF
	 */
	abstract States calculateState();

	@Override
	void update() {
		checkHover();
		outputs[0].setState(calculateState());
	}
	
	/**
	 * Move the Gate and it's bounds relative to it's current location
	 * @param xOff Change in x direction
	 * @param yOff Change in y direction
	 */
	
	@Override
	public void translate(int xOff, int yOff) {
		super.translate(xOff, yOff);
		bounds.translate(xOff, yOff);
		
		for (int i=0; i < inputHovers.length; i++)
			inputHovers[i].translate(xOff, yOff);
		for (int i=0; i < outputHovers.length; i++)
			outputHovers[i].translate(xOff, yOff);
	}

	/**
	 * Check if the mouse is hovering above any inputs or outputs
	 */
	
	public void checkHover() {
		for (int i = 0; i < inputs.length; i++)
			hovers[i] = inputHovers[i].contains(Simulator.mouse);
		for (int i = inputs.length, j = 0; j < outputs.length; i++, j++)
			hovers[i] = outputHovers[j].contains(Simulator.mouse);
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
	
	@Override
	void onResize() {
		generateBounds();
		generateHovers();
	}

	/**
	 * Generate bounds of the Gate
	 */
	
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

	/**
	 * Generate hovers for Gate's inputs and outputs
	 */
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
	
	/**
	 * Check if the mouse is within the Gate
	 * @param mouse mouse location
	 */
	@Override
	boolean contains(Point mouse) {
		return (bounds.contains(mouse));
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
	final int ID = AND_Counter++;

	public AND(int x, int y) {
		super(x, y, Gates.AND);
	}
	
	public AND(int x, int y, int width, int height){
		super(x,y,width,height, Gates.AND);
	}

	@Override
	States calculateState() {
		return States.toState(inputs[0].getState().getBoolean() && inputs[1].getState().getBoolean());
	}

	@Override
	public float[][] getHoverRatios() {
		return RatioGroups.AND_GATE_HOVER_RATIOS.getRatioGroup();
	}

	@Override
	public float[][] getBoundsRatios() {
		return RatioGroups.AND_GATE_BOUNDS_RATIOS.getRatioGroup();
	}
	
	public String toString() {
		return "AND[" + ID + "]";
	}

}

class OR extends Gate {

	public static int OR_Counter = 0;
	final int ID = OR_Counter++;

	public OR(int x, int y) {
		super(x, y, Gates.OR);
	}
	
	public OR(int x, int y, int width, int height){
		super(x,y,width,height, Gates.OR);
	}

	@Override
	States calculateState() {
		return States.toState(inputs[0].getState().getBoolean() || inputs[1].getState().getBoolean());
	}

	@Override
	public float[][] getHoverRatios() {
		return RatioGroups.OR_GATE_HOVER_RATIOS.getRatioGroup();
	}

	@Override
	public float[][] getBoundsRatios() {
		return RatioGroups.OR_GATE_BOUNDS_RATIOS.getRatioGroup();
	}
	
	public String toString() {
		return "OR[" + ID + "]";
	}
}

class NOT extends Gate {
	
	public static int NOT_Counter = 0;
	final int ID = NOT_Counter++;

	public NOT(int x, int y) {
		super(x, y, Gates.NOT, 1, 1);
	}
	
	public NOT(int x, int y, int width, int height){
		super(x,y,width,height, Gates.NOT);
	}

	@Override
	States calculateState() {
		return States.toState(!inputs[0].getState().getBoolean());
	}

	@Override
	public float[][] getHoverRatios() {
		return RatioGroups.NOT_GATE_HOVER_RATIOS.getRatioGroup();
	}

	@Override
	public float[][] getBoundsRatios() {
		return RatioGroups.NOT_GATE_BOUNDS_RATIOS.getRatioGroup();
	}
	
	public String toString() {
		return "NOT[" + ID + "]";
	}

}

class XOR extends Gate{
	
	public static int XOR_Counter = 0;
	final int ID = XOR_Counter++;
	
	public XOR(int x, int y) {
		super(x, y, Gates.XOR);
	}
	
	public XOR(int x, int y, int width, int height){
		super(x,y,width,height, Gates.XOR);
	}
	
	@Override
	States calculateState() {
		return States.toState(inputs[0].getState().getBoolean() ^ inputs[1].getState().getBoolean());
	}

	@Override
	public float[][] getHoverRatios() {
		return RatioGroups.XOR_GATE_HOVER_RATIOS.getRatioGroup();
	}

	@Override
	public float[][] getBoundsRatios() {
		return RatioGroups.XOR_GATE_BOUNDS_RATIOS.getRatioGroup();
	}

	public String toString() {
		return "XOR[" + ID + "]";
	}
}
