import java.util.ArrayList;

public abstract class IO {}

enum States {
	ON(true), OFF(false);

	private boolean b;

	private States(boolean b) {
		this.b = b;
	}

	boolean getBoolean() {
		return b;
	}
	
	/**
	 * Converts boolean to State
	 * @param b boolean
	 * @return true --> ON, false --> OFF
	 */

	public static States toState(boolean b) {
		return (b ? ON : OFF);
	}
}

class Input{

	private ArrayList<Output> outputs = new ArrayList<Output>();
	private final int ID;
	private static int Icounter = 0;

	public Input() {
		ID = Icounter++;
	}

	public void connect(Output out) {
		outputs.add(out);
	}

	@SuppressWarnings("unchecked")
	public States getState() {
		for (Output out : (ArrayList<Output>) outputs.clone())
			if (out.getState().getBoolean())
				return States.ON;
		return States.OFF;
	}

	public static Input getDefault() {
		Input temp = new Input();
		return temp;
	}

	public boolean isConnectedTo(Output output) {
		for (Output out : outputs)
			if (out == output)
				return true;
		return false;
	}
	
	/**
	 * Remove Output from array
	 * @param out Output to be removed
	 */

	public void disconnect(Output out) {
		System.out.println("before: " + outputs);
		System.out.println("removing: " + out);
		outputs.remove(out);
		System.out.println("after: " + outputs);
	}
	
	public String toString(){
		return "Input[" + ID + "]"; 
	}
}

class Output{
	private States state = States.OFF;
	private final int ID;
	static int Ocounter = 0;
	
	public States getState() {
		return state;
	}

	public Output() {
		ID = Ocounter++;
	}
	
	public void setState(States newState) {
		state = newState;
	}

	public String toString(){
		return "Output[" + ID + "]"; 
	}
}
