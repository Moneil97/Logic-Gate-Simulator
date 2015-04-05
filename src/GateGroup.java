import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;


public abstract class GateGroup extends EComponent{

	private Rectangle bounds;
	protected ArrayList<Gate> gates = new ArrayList<Gate>();

	public GateGroup(int x, int y, int width, int height, int inputsAmount, int outputsAmount) {
		super(x, y, width, height, inputsAmount, outputsAmount);
		bounds = new Rectangle(x,y,width,height);
	}

	@Override
	void update() {
		
	}
	
	@Override
	public void translate(int xOff, int yOff) {
		super.translate(xOff, yOff);
		bounds.translate(xOff, yOff);
		
		for (Gate gate : gates)
			gate.translate(xOff, yOff);
	}

	@Override
	boolean contains(Point p) {
		return bounds.contains(p);
	}

}

class HalfAdder extends GateGroup{
	
	public HalfAdder(int x, int y) {
		super(x,y,600 / 4, 360 / 4, 2,2);
		
		gates.add(new AND(x, y, 600 / 8, 360 / 8));
		
	}
	
	@Override
	void draw(Graphics2D g) {
		
		g.setColor(Color.blue);
		g.drawRect(x, y, width, height);
		g.setColor(new Color(0,0,1,.2f));
		g.fillRect(x, y, width, height);
		
		for (Gate gate : gates)
			gate.draw(g);
		
	}
	
	@Override
	Rectangle[] getInputHovers() {
		return null;
	}

	@Override
	Rectangle[] getOutputHovers() {
		return null;
	}
	
}

class FullAdder extends GateGroup{
	
	public FullAdder(int x, int y) {
		super(x,y,600 / 4, 360 / 4, 3,2);
	}

	@Override
	void draw(Graphics2D g) {
		
	}
	
	@Override
	Rectangle[] getInputHovers() {
		return null;
	}

	@Override
	Rectangle[] getOutputHovers() {
		return null;
	}

	
	
}
