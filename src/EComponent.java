import java.awt.Graphics2D;
import java.awt.Point;


public abstract class EComponent {

	int x,y;
	boolean pickedUp = false;
	
	public EComponent() {
		this(0,0);
	}
	
	public EComponent(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void translate(int xOff, int yOff) {
		x+=xOff;
		y+=yOff;
	}
	
	public void pickup() {
		pickedUp = true;
	}
	
	public void drop(){
		pickedUp = false;
	}
	
	abstract void update();
	
	abstract void draw(Graphics2D g);
	
	abstract boolean checkIfClicked(Point p);
	
	public void say(Object x) {
		System.out.println(x);
	}

}
