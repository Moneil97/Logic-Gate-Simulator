import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;

public class Wire extends EComponent {

	final EComponent inputParent;
	final EComponent outputParent;
	final int inputCenter;
	final int outputCenter;
	
	public Wire(EComponent inputParent, EComponent outputParent, int inputSub, int outputSub) {
		super(0, 0, 0, 0, 1, 1);
		inputParent.inputs[inputSub].connect(outputs[0]);
		inputs[0].connect(outputParent.outputs[outputSub]);
		this.inputParent = inputParent;
		this.outputParent = outputParent;
		
		if (inputParent instanceof Gate){
			Rectangle rect = inputParent.getInputHovers()[inputSub];
			inputCenter = Math.round(rect.y -inputParent.getY() + rect.height/2);
		}
		else{
			inputCenter = inputParent.height/2;
		}
		
		if (outputParent instanceof Gate){
			Rectangle rect = outputParent.getOutputHovers()[outputSub];
			outputCenter = Math.round(rect.y -outputParent.getY() + rect.height/2);
		}
		else{
			outputCenter = outputParent.height/2;
		}
		
	}

	@Override
	void update() {
		outputs[0].setState(inputs[0].getState());
		//say("      " + inputs[0] + "(" + inputs[0].getOut() + ")   -->  " + outputs[0] + "(" + outputs[0].getState());
	}

	@Override
	void draw(Graphics2D g) {
		g.setStroke(new BasicStroke(5));
		
		g.setColor(outputs[0].getState().getBoolean() ?  Color.yellow: Color.black);
		
		g.drawLine(inputParent.getX(), inputParent.getY() + inputCenter, outputParent.getX() + outputParent.getWidth(),
				outputParent.getY() + outputCenter);
		
		g.setColor(Color.orange);
		g.drawString(Arrays.toString(inputs), x, y+20);
		g.drawString(Arrays.toString(outputs), x, y+40);
	}

	@Override
	boolean contains(Point p) {
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

	EComponent inputParent, outputParent;
	private int i;
	private int j;
	private int centerInput;
	private int centerOutput;

	public WireCreator() {
		System.out.println("Started");
	}

	Wire create() {
		return new Wire(inputParent, outputParent, i, j);
	}

	public void setInputParent(EComponent eComp, int i) {
		this.i = i;
		inputParent = eComp;
		
		if (inputParent instanceof Gate){
			Rectangle rect = inputParent.getInputHovers()[i];
			centerInput = Math.round(rect.y -inputParent.getY() + rect.height/2);
		}
		else{
			centerInput = inputParent.height/2;
		}
	}

	public void setOutputParent(EComponent eComp, int j) {
		this.j = j;
		outputParent = eComp;
		
		if (outputParent instanceof Gate){
			Rectangle rect = outputParent.getOutputHovers()[j];
			centerOutput = Math.round(rect.y -outputParent.getY() + rect.height/2);
		}
		else{
			centerOutput = outputParent.height/2;
		}
	}
	
	public void draw(Graphics2D g, Point mouse){
		
		if (inputParent != null){
			g.setStroke(new BasicStroke(5));
			g.setColor(Color.black);
			g.drawLine(inputParent.getX(), inputParent.getY() + centerInput, mouse.x, mouse.y);
		}else if (outputParent!= null){
			g.setStroke(new BasicStroke(5));
			g.setColor(outputParent.outputs[0].getState().getBoolean() ?  Color.yellow: Color.black);
			g.drawLine(outputParent.getX() + outputParent.getWidth(), outputParent.getY() + centerOutput, mouse.x, mouse.y);
		}

	}

}
