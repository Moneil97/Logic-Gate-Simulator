import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Switch extends EComponent {

	private Output out;
	private States state = States.OFF;

	public Switch(int x, int y) {
		super(x, y, 50, 50, 0, 1);
	}

	public States getState() {
		return state;
	}

	public Output getOutput() {
		return out;
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
		return false;
	}

	public void toggle() {
		state = States.getEnum(!state.getBoolean());
	}

	@Override
	Rectangle[] getInputHovers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Rectangle[] getOutputHovers() {
		// TODO Auto-generated method stub
		return null;
	}

}
