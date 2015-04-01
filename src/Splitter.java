import java.awt.Graphics2D;

public class Splitter extends Gate{

	public Splitter(int x, int y) {
		super(x, y, Gates.SPLITTER, 1, 2);
	}

	//Just Overriding to avoid errors for now
//	@Override
//	protected void generateHovers() {
//		// TODO Auto-generated method stub
//	}
//	
//	@Override
//	protected void generateBounds() {
//	}
	
	@Override
	void update() {
		
	}
	
	@Override
	void draw(Graphics2D g) {
		//g.drawImage(ImageTools.SPLITTER, x, y,width, height, null);
	}

	@Override
	public float[][] getBoundsRatios() {
		System.err.println("not implemented");
		return null;
	}

	@Override
	public float[][] getHoverRatios() {
		System.err.println("not implemented");
		return null;
	}

	@Override
	States calculateState() {
		System.err.println("not implemented");
		return null;
	}

}
