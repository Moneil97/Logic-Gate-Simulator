import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * ON/OFF Switch
 * @author Cameron O'Neil
 */

public class Switch extends EComponent {

	private States state = States.OFF;
	private Rectangle bounds;
	
	public static final int DEFAULT_WIDTH = 96, DEFAULT_HEIGHT = 29;

	public Switch(int x, int y) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public Switch(int x, int y, int width, int height) {
		super(x, y, width, height, 0, 1);
		bounds = new Rectangle(x,y,width,height);
	}

	@Override
	void update() {
		
	}

	@Override
	void draw(Graphics2D g) {
		g.drawImage((state.getBoolean() ? ImageTools.ON : ImageTools.OFF), x, y, width, height, null);
	}

	@Override
	boolean contains(Point p) {
		return bounds.contains(p);
	}

	public void toggle() {
		outputs[0].setState(state = States.toState(!state.getBoolean()));
	}
	
	@Override
	public void translate(int xOff, int yOff) {
		super.translate(xOff, yOff);
		bounds.translate(xOff, yOff);
	}

	@Override
	Rectangle[] getInputHovers() {
		System.err.println("Switches have no inputs");
		return null;
	}

	@Override
	Rectangle[] getOutputHovers() {
		return new Rectangle[]{bounds};
	}

	@Override
	void onResize() {
		bounds = new Rectangle(x,y,width,height);
	}

}
