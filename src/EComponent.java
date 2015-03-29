import java.awt.Graphics2D;
import java.awt.Point;

public abstract class EComponent {

	int x, y, width, height;
	boolean pickedUp = false;
	Input[] inputs;
	Output[] outputs;

	public EComponent() {
		this(0, 0, 100, 100);
	}

	public EComponent(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void translate(int xOff, int yOff) {
		x += xOff;
		y += yOff;
	}

	public void pickup() {
		pickedUp = true;
	}

	public void drop() {
		pickedUp = false;
	}
	
	public boolean hasInputs(){
		return inputs.length > 0;
	}
	
	public boolean hasOutputs(){
		return outputs.length > 0;
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	abstract void update();

	abstract void draw(Graphics2D g);

	abstract boolean checkIfClicked(Point p);

	public void say(Object x) {
		System.out.println(x);
	}

}
