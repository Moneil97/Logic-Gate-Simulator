import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;


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
		generateBounds();
	}
	
	private void generateBounds(){
		Rectangle2D bounds = new Canvas().getFontMetrics(font).getStringBounds(text, null);
		this.bounds = new Rectangle(x,y-(int)bounds.getHeight()+5,(int)bounds.getWidth(), (int)bounds.getHeight());
	}
	
	public void draw(Graphics2D g){
		g.setColor(Color.black);
		g.setFont(font);
		g.drawString(text, x, y);
		//g.draw(bounds);
	}
	
	public void translate(int xOff, int yOff){
		x += xOff;
		y+= yOff;
		bounds.translate(xOff, yOff);
	}
	
	public boolean contains(Point p){
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
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		generateBounds();
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
		generateBounds();
	}

	public void doPopup(Component comp, int x, int y) {
		popup.show(this, comp, x, y);
	}
	
	static LabelPopup popup = new LabelPopup();

}

@SuppressWarnings("serial")
class LabelPopup extends JPopupMenu{
	
	UserLabel temp;
	
	public LabelPopup() {
		
			JMenuItem text = new JMenuItem("Change Text");
			text.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					temp.setText(JOptionPane.showInputDialog("Change Text", temp.getText()));
					temp = null;
				}
			});
		this.add(text);
			JMenuItem font = new JMenuItem("Set Font");
			font.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try{
						String[] options = JOptionPane.showInputDialog("Font size", "arial 20").split(" ");
						temp.setFont(new Font(options[0], Font.PLAIN, Integer.parseInt(options[1])));
						temp = null;
					}catch(Exception error){
						
						JOptionPane.showMessageDialog(null, "Invalid Font.\nFont will not change");
						
						error.printStackTrace();
					}
				}
			});
		this.add(font);
			JMenuItem delete = new JMenuItem("Delete");
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try{
						Simulator.labels.remove(temp);
					}catch(Exception error){
						System.err.println(error.getMessage());
					}
					finally{
						temp = null;
					}
				}
			});
		this.add(delete);
		
	}
	
	public void show(UserLabel label, Component invoker, int x, int y) {
		temp = label;
		show(invoker, x, y);
	}
	
}

