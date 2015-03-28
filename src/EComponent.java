import java.awt.Graphics2D;


public abstract class EComponent {

	public EComponent() {
		
	}
	
	abstract void update();
	
	abstract void draw(Graphics2D g);
	
	

}
