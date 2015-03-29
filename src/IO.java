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

	public static States getEnum(boolean b) {
		return (b ? ON : OFF);
	}
}

class Input{

	private Output out = new Output();
	private final int ID;
	private static int Icounter = 0;

	public Output getOut() {
		return out;
	}

	public Input() {
		ID = Icounter++;
	}

	public void connect(Output out) {
		this.out = out;
	}
	
	public Output getCurrentConnectedOutput(){
		return out;
	}

	public States getState() {
		return out.getState();
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
	
	public String toString(){
		return "Input[" + ID + "]"; 
	}
}

class Output{
	private States state = States.OFF;
	private final int ID;
	
	public States getState() {
		return state;
	}

	static int Ocounter = 0;

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
