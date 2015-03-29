
enum States{
	ON(true), OFF(false);
	
	boolean b;
	
	States(boolean b){
		this.b = b;
	}
	
	boolean getBoolean(){
		return b;
	}

	public static States getEnum(boolean b) {
		return (b? ON:OFF);
	}
}

public abstract class IO {

	

}


class Input extends IO{
	
	Output out = new Output();
	States always = null;
	
	public Input(){
		
	}
	
	public Input(States state) {
		always = state;
	}

	public void connect(Output out) {
		this.out = out;
	}
	
	public States getState(){
		if (always != null) return always;
		else return out.state;
	}
	
	public static Input getDefault(){
		Input temp = new Input();
		return temp;
	}
}

class Output extends IO{
	States state = States.OFF;

	public void setState(States newState) {
		state = newState;
	}
}
