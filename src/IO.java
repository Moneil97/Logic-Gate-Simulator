enum States {
	ON(true), OFF(false);

	private boolean b;

	States(boolean b) {
		this.b = b;
	}

	boolean getBoolean() {
		return b;
	}

	public static States getEnum(boolean b) {
		return (b ? ON : OFF);
	}
}

public abstract class IO {

}

class Input extends IO {

	Output out = new Output();
	States always = null;

	public Input() {

	}

	public Input(States state) {
		always = state;
	}

	public void connect(Output out) {
		this.out = out;
	}
	
	public Output getCurrentConnectedOutput(){
		return out;
	}

	public States getState() {
		if (always != null)
			return always;
		else
			return out.state;
	}

	public static Input getDefault() {
		Input temp = new Input();
		return temp;
	}

	public boolean isConnectedTo(Output output) {
		return (out == output);
	}

	public void disconnect() {
		out = new Output();
	}
}

class Output extends IO {
	States state = States.OFF;

	public void setState(States newState) {
		state = newState;
	}

//	public void disconnect() {
//		// TODO Auto-generated method stub
//		
//	}
}
