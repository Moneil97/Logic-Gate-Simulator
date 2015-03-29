import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;


public abstract class Gate extends EComponent{
	
	public static final int GATE_WIDTH = 600/4, GATE_HEIGHT = 360/4;
	
	//int x,y;
	BufferedImage image;
	Input in1, in2;
	Output out;
	private boolean[] hovers = {false, false,false};
	private float[][] hoverRatios;
	private float[][] boundsRatios;
	private Polygon topInputHover, bottomInputHover, outputHover;
	private Polygon bounds; 
	
	public Gate(int x, int y, BufferedImage image){
		super(x,y);
		this.image = image;
	}

	@Override
	void update() {
		checkHover();
	}
	
	@Override
	public void translate(int xOff, int yOff) {
		super.translate(xOff, yOff);
		
		
	
	}
	
	@Override
	public void drop() {
		super.drop();
		
		//Move this to only generate on release
		generateBounds();
		generateHovers();
		
	}
	
	public void checkHover() {
		hovers[0] = bottomInputHover.contains(Simulator.mouse);
		hovers[1] = topInputHover.contains(Simulator.mouse);
		hovers[2] = outputHover.contains(Simulator.mouse);
		//say(Arrays.toString(hovers));
	}

	@Override
	void draw(Graphics2D g) {
		g.drawImage(image, x, y, GATE_WIDTH, GATE_HEIGHT, null);
		g.setColor(Color.blue);
		if (hovers[0]) g.drawPolygon(bottomInputHover);
		if (hovers[1]) g.drawPolygon(topInputHover);
		if (hovers[2]) g.drawPolygon(outputHover);
	}
	
	public void setBoundsRatios(float[][] ratios){
		boundsRatios = ratios;
		generateBounds();
	}
	
	private void generateBounds() {
		
		int size = boundsRatios[0].length;
		
		int xs[] = new int[size];
		int ys[] = new int[size];
		
		for (int i =0; i  < size; i++){
			xs[i] = Math.round(x + boundsRatios[0][i] * GATE_WIDTH);
			ys[i] = Math.round(y + boundsRatios[1][i] * GATE_HEIGHT);
		}
		
		bounds = new Polygon(xs, ys, size);
	}
	
	public void setHoverRatios(float[][] ratios){
		hoverRatios = ratios;
		generateHovers();
	}

	protected void generateHovers() {
		
		int size = hoverRatios[0].length;
		
		int xs[] = new int[size];
		int ys[] = new int[size];
		
		for (int i =0; i  < size; i++){
			xs[i] = Math.round(x + hoverRatios[0][i] * GATE_WIDTH);
			ys[i] = Math.round(y + hoverRatios[1][i] * GATE_HEIGHT);
		}
		
		bottomInputHover = new Polygon(xs, ys, size);
		
		for (int i =0; i  < size; i++){
			xs[i] = Math.round(x + hoverRatios[2][i] * GATE_WIDTH);
			ys[i] = Math.round(y + hoverRatios[3][i] * GATE_HEIGHT);
		}
		
		topInputHover = new Polygon(xs, ys, size);
		
		for (int i =0; i  < size; i++){
			xs[i] = Math.round(x + hoverRatios[4][i] * GATE_WIDTH);
			ys[i] = Math.round(y + hoverRatios[5][i] * GATE_HEIGHT);
		}
		
		outputHover = new Polygon(xs, ys, size);
		
	}
	
	@Override
	boolean checkIfClicked(Point p) {
		return (bounds.contains(p));
	}
	
	public void say(Object x) {
		System.out.println(x);
	}

}

class AND extends Gate{
	
//	private final float[] bottomXRatios = { 0.18f, 0.0f, 0.0f, 0.18f};
//	private final float[] bottomYRatios = { 0.85f, 0.85f, 0.6f, 0.6f};
//	private final float[] topXRatios = { 0.0f, 0.18f, 0.18f, 0.0f};
//	private final float[] topYRatios = { 0.13f, 0.13f, 0.4f, 0.4f};
//	private final float[] outXRatios = { 0.8f, 1.0f, 1.0f, 0.8f};
//	private final float[] outYRatios = { 0.33f, 0.33f, 0.64f, 0.64f};
//	private final float[][] ratios = {bottomXRatios, bottomYRatios, topXRatios, topYRatios, outXRatios, outYRatios};
//	private final float[][] ratios = Ratios.values();
	
	public AND(int x, int y) {
		super(x, y, ImageTools.getGateImage(Gates.AND));
		setHoverRatios(RatioGroups.AND_GATE_HOVER_RATIOS.getRatioGroup());
		setBoundsRatios(RatioGroups.AND_GATE_BOUNDS_RATIOS.getRatioGroup());
	}

}

class OR extends Gate{
	public OR(int x, int y) {
		super(x, y, ImageTools.getGateImage(Gates.OR));
		//generateHovers(ratios);
	}
}
