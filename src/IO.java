
enum States{
	ON, OFF;
}

public abstract class IO {

	States state = States.OFF;

}


class Input extends IO{
	
	
	
	public static Input getDefault(){
		Input temp = new Input();
		return temp;
	}
}

class Output extends IO{
	
}
