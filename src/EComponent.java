import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;

public abstract class EComponent {

	int x, y, width, height;
	private boolean pickedUp = false;
	Input[] inputs;
	Output[] outputs;

	public EComponent() {
		this(0, 0, 100, 100, 0, 0);
	}

	public EComponent(int x, int y, int width, int height, int inputsAmount, int outputsAmount) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.inputs = new Input[inputsAmount];
		for (int i = 0; i < inputsAmount; i++)
			this.inputs[i] = new Input();
		this.outputs = new Output[outputsAmount];
		for (int i = 0; i < outputsAmount; i++)
			this.outputs[i] = new Output();
		say(Arrays.toString(this.inputs));
		say(Arrays.toString(this.outputs));

	}

	public void translate(int xOff, int yOff) {
		x += xOff;
		y += yOff;
	}

	public void pickup() {
		setPickedUp(true);
	}

	public void drop() {
		setPickedUp(false);
	}

	public boolean hasInputs() {
		return inputs.length > 0;
	}

	public boolean hasOutputs() {
		return outputs.length > 0;
	}
	
	boolean isPickedUp() {
		return pickedUp;
	}

	void setPickedUp(boolean pickedUp) {
		this.pickedUp = pickedUp;
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

	abstract boolean contains(Point p);

	public void say(Object x) {
		System.out.println(x);
	}

	public void acceptOutput(EComponent eCompToCheckOutputsOf) {
		for (int i = 0; i < inputs.length; i++) {
			for (int j = 0; j < eCompToCheckOutputsOf.outputs.length; j++) {
				if (this.getInputHovers()[i].intersects(eCompToCheckOutputsOf.getOutputHovers()[j])) {
					inputs[i].connect(eCompToCheckOutputsOf.outputs[j]);
					say("      " + inputs[i] + " is connected to: " + eCompToCheckOutputsOf.outputs[j]);
				} else {
					say("      " + getInputHovers()[i] + " does not intersect: " + eCompToCheckOutputsOf.getOutputHovers()[j]);
					if (!(eCompToCheckOutputsOf instanceof Wire)) {
						if (inputs[i].isConnectedTo(eCompToCheckOutputsOf.outputs[j])) {
							
							inputs[i].disconnect(eCompToCheckOutputsOf.outputs[j]);
							say("      " + inputs[i] + " disconnected from " + eCompToCheckOutputsOf.outputs[j]);
						}
					} else {
						say("lookout! It's got a wire!");
					}
				}
			}
		}
	}

	public void acceptInput(EComponent eCompToCheckInputsOf) {
		Input[] inputs = eCompToCheckInputsOf.inputs;
		for (int i = 0; i < outputs.length; i++) {
			for (int j = 0; j < inputs.length; j++) {
				if (this.getOutputHovers()[i].intersects(eCompToCheckInputsOf.getInputHovers()[j])) {
					inputs[j].connect(outputs[i]);
					say("      " + inputs[j] + " is connected to: " + outputs[i]);
				} else {
					say("      " + eCompToCheckInputsOf.getInputHovers()[j] + " does not intersect: " + getOutputHovers()[i]);
					if (!(eCompToCheckInputsOf instanceof Wire)) {
						if (inputs[j].isConnectedTo(outputs[i])) {
							say("class: " + eCompToCheckInputsOf.getClass());
							inputs[j].disconnect(outputs[i]);
							say("      " + inputs[j] + " disconnected from " + outputs[i]);
						}
					} else {
						say("wire you doing this");
					}
				}
			}
		}
	}

	abstract Rectangle[] getInputHovers();

	abstract Rectangle[] getOutputHovers();

	public void resetInputs() {
		
		say("resetting: " + Arrays.toString(inputs));
		
		for (int i=0; i < inputs.length; i++)
			inputs[i] = new Input();
		
		say("set to: "  + Arrays.toString(inputs));
	}


}
