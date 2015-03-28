import java.awt.Graphics2D;
import java.awt.Point;


public abstract class EComponent {

	public EComponent() {
		
	}
	
	abstract void update();
	
	abstract void draw(Graphics2D g);
	
	abstract void checkIfClicked(Point p);
	
	//abstract checkHover

}
