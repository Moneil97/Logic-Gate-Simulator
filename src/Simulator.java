import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Simulator extends JFrame implements Runnable, MouseMotionListener, MouseListener{

	public static int ups = 10;
	public static Point mouse = new Point(0,0);
	private JPanel panel;
	private AND and;
	
	
	public Simulator() {
		
		ImageTools.loadImages();
		
//		EComponent[] comps = {new AND(), new LCD()};
//		for (EComponent comp : comps)
//			comp.update();
		
		and = new AND(100,100);
		
		this.add(panel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g1) {
				super.paintComponent(g1);
				Graphics2D g = (Graphics2D) g1;
				and.draw(g);
			}
		});
		panel.setBackground(Color.white);
		
		this.setSize(1000,800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		panel.addMouseMotionListener(this);
		panel.addMouseListener(this);
		new Thread(this).start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true){
					repaint();
					
					try {
						Thread.sleep(ups/1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public void run() {
		while (true){
			
			and.update();
			
			try {
				Thread.sleep(ups/1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	Point mouseDraggedLast = new Point(0,0);

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDraggedLast.setLocation(mouse);
		mouse = e.getPoint();
		
		if (and.pickedUp){
			and.translate((mouse.x-mouseDraggedLast.x), (mouse.y-mouseDraggedLast.y));
			//say("moved by :" + (mouse.x-mouseDraggedLast.x) + " " + (mouse.y-mouseDraggedLast.y));
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouse = e.getPoint();
	}
	
	public void say(Object x) {
		System.out.println(x);
	}
	
	public static void main(String[] args) {
		new Simulator();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (and.checkIfClicked(e.getPoint()))
			and.pickup();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		and.drop();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}
