import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Switch extends EComponent {

	private States state = States.OFF;
	private Rectangle bounds;

	public Switch(int x, int y) {
		super(x, y, 95, 28, 0, 1);
		bounds = new Rectangle(x,y,95,28);
	}

	@Override
	void update() {
		
	}

	@Override
	void draw(Graphics2D g) {
		g.drawImage((state.getBoolean() ? ImageTools.ON : ImageTools.OFF), x, y, null);
	}

	@Override
	boolean checkIfClicked(Point p) {
		return bounds.contains(p);
	}

	public void toggle() {
		outputs[0].setState(state = States.getEnum(!state.getBoolean()));
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

}
