import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JOptionPane;


public class UserLabel {

	private String text;
	private Font font;
	private int x,y;
	private boolean pickedUp;
	private Rectangle bounds;
	
	public UserLabel(int x, int y) {
		this.x = x;
		this.y = y;
		text = JOptionPane.showInputDialog("Text:");
		font = new Font("Arial", Font.PLAIN, 20);

		Rectangle2D bounds = new Canvas().getFontMetrics(font).getStringBounds(text, null);
		this.bounds = new Rectangle(x,y-(int)bounds.getHeight()+5,(int)bounds.getWidth(), (int)bounds.getHeight());
	}
	
	public void draw(Graphics2D g){
		g.setFont(font);
		g.drawString(text, x, y);
		g.draw(bounds);
	}
	
	public void translate(int xOff, int yOff){
		x += xOff;
		y+= yOff;
		bounds.translate(xOff, yOff);
	}
	
	public boolean checkIfClicked(Point p){
		return bounds.contains(p);
	}
	
	public void pickUp(){
		pickedUp = true;
	}
	
	public void drop(){
		pickedUp = false;
	}

	public boolean isPickedUp() {
		return pickedUp;
	}

}
