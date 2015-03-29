import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;

public class Wire extends EComponent {

	final EComponent inputParent;
	final EComponent outputParent;
	int inputCenter = 0;
	int outputCenter = 0;
	
	public Wire(EComponent inputParent, EComponent outputParent, int i, int j) {
		super(0, 0, 0, 0, 1, 1);
		inputParent.inputs[i].connect(outputs[0]);
		inputs[0].connect(outputParent.outputs[j]);
		this.inputParent = inputParent;
		this.outputParent = outputParent;
		
		if (inputParent instanceof Gate){
			float[] inputYRatios = RatioGroups.getHoverRatioGroup(Gates.getValue(inputParent))[1+(i*2)];
			inputCenter = Math.round(inputParent.getHeight()*(inputYRatios[1] + inputYRatios[2])/2.0f);
			say(Arrays.toString(inputYRatios));
		}
		else if (inputParent instanceof Switch){
			inputCenter = inputParent.height/2;
		}
		else{
			
		}
		
		
		if (outputParent instanceof Gate){
			float[] outputYRatios = RatioGroups.getHoverRatioGroup(Gates.getValue(outputParent))[1+(j+inputParent.inputs.length)*2];
			outputCenter =  Math.round(outputParent.getHeight()*((outputYRatios[1] + outputYRatios[2])/2.0f));
			say(Arrays.toString(outputYRatios));
		}
		else if (outputParent instanceof Switch){
			outputCenter = outputParent.height/2;
		}
		else{
			
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
		
		
		
		g.drawLine(inputParent.getX(), inputParent.getY() + inputCenter, outputParent.getX() + outputParent.getWidth(),
				outputParent.getY() + outputCenter);
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

//	Input in;
//	Output out;
	EComponent parent1, parent2;
private int i;
private int j;

	public WireCreator() {
		System.out.println("Started");
	}


//	void setInput(Input in) {
//		this.in = in;
//	}
//
//	void setOutput(Output out) {
//		this.out = out;
//	}

	Wire create() {
		//System.out.println(in + "paired with " + out);
		return new Wire(/*in, out, */parent1, parent2, i, j);
	}

	public void setInputParent(EComponent eComp, int i) {
		this.i = i;
		parent1 = eComp;
	}

	public void setOutputParent(EComponent eComp, int j) {
		this.j = j;
		parent2 = eComp;
	}

}
