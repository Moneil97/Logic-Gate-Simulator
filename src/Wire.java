import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Wire extends EComponent {

	EComponent inputParent;
	EComponent outputParent;

	public Wire(Input in, Output out, EComponent inputParent, EComponent outputParent) {
		super(0, 0, 0, 0, 1, 1);
		in.connect(outputs[0]);
		inputs[0].connect(out);
		this.inputParent = inputParent;
		this.outputParent = outputParent;
	}

	@Override
	void update() {
		outputs[0].setState(inputs[0].getState());
		say("      " + inputs[0] + "(" + inputs[0].getOut() + ")   -->  " + outputs[0] + "(" + outputs[0].getState());
	}

	@Override
	void draw(Graphics2D g) {
		g.setStroke(new BasicStroke(5));
		g.drawLine(inputParent.getX(), inputParent.getY() + inputParent.getHeight() / 2, outputParent.getX() + outputParent.getWidth(),
				outputParent.getY() + outputParent.getHeight() / 2);
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

class WireCreator {

	Input in;
	Output out;
	EComponent parent1, parent2;

	public WireCreator() {
		System.out.println("Started");
	}


	void setInput(Input in) {
		this.in = in;
	}

	void setOutput(Output out) {
		this.out = out;
	}

	Wire create() {
		System.out.println(in + "paired with " + out);
		return new Wire(in, out, parent1, parent2);
	}

	public void setInputParent(EComponent eComp) {
		parent1 = eComp;
	}

	public void setOutputParent(EComponent eComp) {
		parent2 = eComp;
	}

}
