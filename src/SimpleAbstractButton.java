import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public abstract class SimpleAbstractButton {

	private int x, y, width, height;
	private String text;
	
	private Font font = new Font("arial", Font.BOLD, 30);

	private boolean hovering, pressed;

	private Rectangle rekt;
	private int arcWidth = 0, arcHeight = 0;

	private boolean forceHilight = false;

	public SimpleAbstractButton(String text, int x, int y, int width, int height) {
		this(text, x, y, width, height, 0, 0, 30);
	}

	public SimpleAbstractButton(String text, int x, int y, int width, int height, int arcWidth, int arcHeight, int fontSize) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.arcWidth = arcWidth;
		this.arcHeight = arcHeight;
		font = new Font("arial", Font.BOLD, fontSize);

		rekt = new Rectangle(x, y, width, height);
	}

	abstract void onPress();

	public void draw(Graphics2D g2) {

		g2.setColor(Color.orange);
		g2.setFont(font);

		if (hovering || forceHilight) {
			g2.setColor(Color.blue);
			g2.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
		} else {
			g2.setColor(Color.orange);
			g2.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
		}

		g2.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
 
		g2.setColor(Color.black);
		g2.drawString(text, (x + width / 2) - g2.getFontMetrics().stringWidth(text) / 2, (y + height / 2) + g2.getFontMetrics().getHeight() / 4);
	}

	public void updateHover(MouseEvent e) {
		updateHover(e.getPoint());
	}

	public void updateHover(Point p) {
		if (rekt.contains(p.getX(), p.getY())) {
			if (!hovering) {
				hovering = true;
			}
		} else {
			hovering = false;
		}
	}

	public void updatePressed() {
		if (hovering) {
			pressed = true;
			onPress();
		}
	}

	public void updatePressed(Point p) {
		updateHover(p);
		updatePressed();
	}

	public void updatePressed(MouseEvent e) {
		updatePressed(e.getPoint());
	}

	public boolean isPressed() {
		return pressed;
	}

	public void setX(int x) {
		this.x = x;
		rekt = new Rectangle(x, y, width, height);
	}

	public void setY(int y) {
		this.y = y;
		rekt = new Rectangle(x, y, width, height);
	}

	public void reset() {
		pressed = false;
		hovering = false;
		forceHilight = false;
	}

	public void resetPressed() {
		pressed = false;
	}

	public void setPressed(boolean b) {
		pressed = b;
	}

	public void say(Object s) {
		System.out.println(this.getClass().getName() + ": " + s);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Rectangle2D getUpperBounds() {
		return new Rectangle2D.Double(x + 5, y + 1, width - 5 - 5, 1);
	}

	public Rectangle2D getLowerBounds() {
		return new Rectangle2D.Double(x + 5, y + height - 1, width - 5 - 5, 1);
	}

	public Rectangle2D getRightBounds() {
		return new Rectangle2D.Double(x + width - 1, y + 5, 1, height - 5 - 5);
	}

	public Rectangle2D getLeftBounds() {
		return new Rectangle2D.Double(x - 1, y + 5, 1, height - 5 - 5);
	}

	public Rectangle2D getBounds() {

		return new Rectangle2D.Double(x, y, width, height);
	}

	public void forceHilight(boolean b) {
		forceHilight = b;
	}

	public void setHovered(boolean b) {
		hovering = b;
	}
}