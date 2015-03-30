import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;


public class Splitter extends EComponent{

	public Splitter(int x, int y) {
		super(x,y,0,0,1,2);
	}

	@Override
	void update() {
		
	}

	@Override
	void draw(Graphics2D g) {
		g.drawImage(ImageTools.SPLITTER, x, y, null);
	}

	@Override
	boolean checkIfClicked(Point p) {
		System.err.println("not implemented");
		return false;
	}

	@Override
	Rectangle[] getInputHovers() {
		System.err.println("not implemented");
		return null;
	}

	@Override
	Rectangle[] getOutputHovers() {
		System.err.println("not implemented");
		return null;
	}

}
