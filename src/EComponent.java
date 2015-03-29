import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;

public abstract class EComponent {

	int x, y, width, height;
	boolean pickedUp = false;
	Input[] inputs;
	Output[] outputs;

	public EComponent() {
		this(0, 0, 100, 100,0,0);
	}

	public EComponent(int x, int y, int width, int height, int inputsAmount, int outputsAmount) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.inputs = new Input[inputsAmount];
		for (int i =0; i < inputsAmount; i++)
			this.inputs[i] = new Input();
		this.outputs = new Output[outputsAmount];
		for (int i =0; i < outputsAmount; i++)
			this.outputs[i] = new Output();
		say(Arrays.toString(this.inputs));
		say(Arrays.toString(this.outputs));
		
	}

	public void translate(int xOff, int yOff) {
		x += xOff;
		y += yOff;
	}

	public void pickup() {
		pickedUp = true;
	}

	public void drop() {
		pickedUp = false;
	}
	
	public boolean hasInputs(){
		return inputs.length > 0;
	}
	
	public boolean hasOutputs(){
		return outputs.length > 0;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	abstract void update();

	abstract void draw(Graphics2D g);

	abstract boolean checkIfClicked(Point p);

	public void say(Object x) {
		System.out.println(x);
	}

	public void acceptOutput(EComponent eCompToCheckOutputsOf) {
		for (int i=0; i <inputs.length; i++){
			for (int j=0; j <outputs.length; j++){
				if (this.getInputHovers()[i].intersects(eCompToCheckOutputsOf.getOutputHovers()[j])){
					inputs[i].connect(outputs[j]);
					say("      " + inputs[i] + " is connected to: " + outputs[j]);
				}
				else{
					say("      " + getInputHovers()[i] + " does not intersect: " + eCompToCheckOutputsOf.getOutputHovers()[j]);
					if (!(eCompToCheckOutputsOf instanceof Wire)){
						if (inputs[i].isConnectedTo(outputs[j])){
							inputs[i].disconnect();
							say("      " + inputs[i] + " disconnected from " + outputs[j]);
						}
					}
				}
			}
		}
	}
	
	public void acceptInput(EComponent eCompToCheckInputsOf) {
		
	}
	
	abstract Rectangle[] getInputHovers();
	abstract Rectangle[] getOutputHovers();

}
