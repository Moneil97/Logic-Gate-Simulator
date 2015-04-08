import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

public abstract class EComponent {

	int x, y, width, height;
	private boolean pickedUp = false;
	Input[] inputs;
	Output[] outputs;

	/**
	 * Empty constructor of EComponent, default Values: (0, 0, 100, 100, 0, 0)
	 */
	
	public EComponent() {
		this(0, 0, 100, 100, 0, 0);
	}
	
	/**
	 * @param x xPostion of eComp
	 * @param y yPostion of eComp
	 * @param width Width of eComp
	 * @param height Height of eComp
	 * @param inputsAmount Amount of Inputs eComp has
	 * @param outputsAmount Amount of Outputs eComp has
	 */

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
	
	/**
	 * Called when eComp is resized
	 */
	abstract void onResize();

	/**
	 * Move the EComp Relative to it's current location
	 * @param xOff Change in x direction
	 * @param yOff Change in y direction
	 */
	
	public void translate(int xOff, int yOff) {
		x += xOff;
		y += yOff;
	}

	/**
	 * If inputs.length > 0
	 */

	public boolean hasInputs() {
		return inputs.length > 0;
	}

	/**
	 * If outputs.length > 0
	 */
	
	public boolean hasOutputs() {
		return outputs.length > 0;
	}
	
	/**
	 * Set the EComponent's pickedUp field
	 */

	public void setPickedUp(boolean pickedUp) {
		this.pickedUp = pickedUp;
	}
	
	/**
	 * @return Value of pickedUp
	 */
	
	boolean isPickedUp() {
		return pickedUp;
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
	
	public void setSize(int width, int height){
		this.width = width;
		this.height = height;
		onResize();
	}

	
	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}

	abstract void update();

	abstract void draw(Graphics2D g);

	abstract boolean contains(Point p);

	public void say(Object x) {
		System.out.println(x);
	}
	
	/**
	 * If the passed EComponent's Outputs overlap any of this EComponent's Inputs, link them.
	 * @param eCompToCheckOutputsOf The EComponent to check the Outputs of
	 */

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
	
	/**
	 * If the passed EComponent's inputs overlap any of this EComponent's Outputs, link them.
	 * @param eCompToCheckInputsOf The EComponent to check the Inputs of
	 */

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

	EComponentPopup popup = new EComponentPopup();
	
	/**
	 * Open Pop-up for EComponents
	 * @param component Component that the menu was opened from
	 * @param x xPosition
	 * @param y yPosition
	 */
	
	public void doPopup(Component component, int x, int y) {
		popup.show(this, component, x, y);
	}
	
	/** 
	 * Sets All Inputs and Outputs to new Objects
	 */

	public void resetIO() {
		for (int i=0; i < inputs.length; i++)
			inputs[i] = new Input();
		for (int i=0; i < outputs.length; i++)
			outputs[i] = new Output();
	}

}

@SuppressWarnings("serial")
class EComponentPopup extends JPopupMenu{
	
	private EComponent temp;
	
	public EComponentPopup() {
		
			JMenuItem reSize = new JMenuItem("Resize");
			reSize.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try{
						String[] options = JOptionPane.showInputDialog("Input new size:\nwidth height", temp.getWidth() + " " + temp.getHeight()).split(" ");
						temp.setSize(Integer.parseInt(options[0]), Integer.parseInt(options[1]));
					}
					catch(Exception error){
						System.err.println(error.getMessage());
						JOptionPane.showMessageDialog(null, "Invalid size, size will remain unchanged.");
					}
					finally{
						temp = null;
					}
					
				}
			});
		this.add(reSize);
			JMenuItem font = new JMenuItem("Remove Wires");
			font.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try{
						Simulator.removeConnections(temp);
						temp.resetIO();
					}catch(Exception error){
						try{
							Simulator.removeConnections(temp);
							temp.resetIO();
						}catch(Exception error2){
							error2.printStackTrace();
						}
					}
					finally{
						temp = null;
					}
				}
			});
		this.add(font);
			JMenuItem delete = new JMenuItem("Delete");
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try{
						Simulator.delete(temp);
					}catch(Exception error){
						try{
							Simulator.delete(temp);
						}catch(Exception error2){
							error2.printStackTrace();
						}
					}
					finally{
						temp = null;
					}
					
				}
			});
		this.add(delete);
		
	}
	
	public void show(EComponent eComp, Component invoker, int x, int y) {
		temp = eComp;
		show(invoker, x, y);
	}
	
}
