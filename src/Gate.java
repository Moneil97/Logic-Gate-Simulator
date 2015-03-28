import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;


public abstract class Gate extends EComponent{
	
	public static final int GATE_WIDTH = 600/4, GATE_HEIGHT = 360/4;
	
	int x,y;
	BufferedImage image;
	Input in1, in2;
	Output out;

	/*public Gate() {
		this(0,0,ImageTools.getDefaultImage(new Dimension(GATE_WIDTH, GATE_HEIGHT)));
	}
	
	public Gate(BufferedImage image){
		this(0,0,image);
	}
	
	public Gate(int x, int y){
		this(x,y,ImageTools.getDefaultImage(new Dimension(GATE_WIDTH, GATE_HEIGHT)));
	}*/
	
	public Gate(int x, int y, BufferedImage image){
		this.x = x;
		this.y = y;
		this.image = image;
	}

	@Override
	void update() {
		checkHover();
	}
	
	public void move(){
		
	}
	
	private boolean[] hovers = {false, false,false};
	
	
	public void checkHover() {
		hovers[0] = bottom.contains(Simulator.mouse);
		hovers[1] = top.contains(Simulator.mouse);
		hovers[2] = right.contains(Simulator.mouse);
		say(Arrays.toString(hovers));
	}

	@Override
	void draw(Graphics2D g) {
		g.drawImage(image, x, y, GATE_WIDTH, GATE_HEIGHT, null);
		g.setColor(Color.blue);
		if (hovers[0]) g.drawPolygon(bottom);
		if (hovers[1]) g.drawPolygon(top);
		if (hovers[2]) g.drawPolygon(right);
	}
	
	private Polygon top, bottom, right; 
	
	protected void generateHovers(float[][] ratios) {
		
		int size = ratios[0].length;
		
		int xs[] = new int[size];
		int ys[] = new int[size];
		
		for (int i =0; i  < size; i++){
			xs[i] = Math.round(x + ratios[0][i] * GATE_WIDTH);
			ys[i] = Math.round(y + ratios[1][i] * GATE_HEIGHT);
		}
		
		bottom = new Polygon(xs, ys, size);
		
		for (int i =0; i  < size; i++){
			xs[i] = Math.round(x + ratios[2][i] * GATE_WIDTH);
			ys[i] = Math.round(y + ratios[3][i] * GATE_HEIGHT);
		}
		
		top = new Polygon(xs, ys, size);
		
		for (int i =0; i  < size; i++){
			xs[i] = Math.round(x + ratios[4][i] * GATE_WIDTH);
			ys[i] = Math.round(y + ratios[5][i] * GATE_HEIGHT);
		}
		
		right = new Polygon(xs, ys, size);
		
	}
	
	@Override
	void checkIfClicked(Point p) {
		if (new Rectangle(x,y,GATE_HEIGHT, GATE_WIDTH).contains(p)){
			
		}
	}
	
	public void say(Object x) {
		System.out.println(x);
	}

}

class AND extends Gate{
	
	private final float[] bottomXRatios = { 0.18f, 0.0f, 0.0f, 0.18f};
	private final float[] bottomYRatios = { 0.85f, 0.85f, 0.6f, 0.6f};
	private final float[] topXRatios = { 0.0f, 0.18f, 0.18f, 0.0f};
	private final float[] topYRatios = { 0.13f, 0.13f, 0.4f, 0.4f};
	private final float[] outXRatios = { 0.8f, 1.0f, 1.0f, 0.8f};
	private final float[] outYRatios = { 0.33f, 0.33f, 0.64f, 0.64f};
	private final float[][] ratios = {bottomXRatios, bottomYRatios, topXRatios, topYRatios, outXRatios, outYRatios};
	
	public AND(int x, int y) {
		super(x, y, ImageTools.getGateImage(Gates.AND));
		generateHovers(ratios);
	}
	
}

class OR extends Gate{
	public OR(int x, int y) {
		super(x, y, ImageTools.getGateImage(Gates.OR));
		//generateHovers(ratios);
	}
}
