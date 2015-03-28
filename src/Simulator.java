import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Simulator extends JFrame{

	public static int ups = 10;
	
	public Simulator() {
		EComponent[] comps = {new Gate(), new LCD()};
		for (EComponent comp : comps)
			comp.update();
	}

	public static void main(String[] args) {
		
	}

}
