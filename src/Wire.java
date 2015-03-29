import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Wire extends EComponent {

//	public Wire(int x, int y) {
//		super(x, y, 0, 0, 1, 1);
//	}
	
	public Wire(Input in, Output out) {
		super(0, 0, 0, 0, 1, 1);
		inputs[0] = in;
		outputs[0] = out;
		in.connect(out);
	}

	@Override
	void update() {
		outputs[0].setState(inputs[0].getState());
	}

	@Override
	void draw(Graphics2D g) {
		g.setStroke(new BasicStroke(5));
		g.drawLine(x, y, x + width, x + height);
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

class WireCreator{
	
	Point start, end;
	Input in;
	Output out;
	
	public WireCreator(){
//		start = new Point();
//		end = new Point();
		System.out.println("Started");
	}
	
//	public WireCreator(Point start){
//		System.out.println("Started");
//		this.start = start;
//		end = new Point();
//	}
	
	void setStartPoint(Point p){
		System.out.println("start set");
		start.setLocation(p);
	}
	
	void setEndPoint(Point p){
		System.out.println("set end");
		end.setLocation(p);
	}
	
	void setInput(Input in){
		this.in = in;
	}
	
	void setOutput(Output out){
		this.out = out;
	}
	
	Wire create(){
		System.out.println(in + "paired with " + out);
		return new Wire(in, out);
	}
	
}
