import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Simulator extends JFrame implements Runnable, MouseMotionListener, MouseListener, KeyListener {

	public static int ups = 20;
	public static Point mouse = new Point(0, 0);
	private JPanel panel;
	private ArrayList<Gate> gates = new ArrayList<Gate>();
	private ArrayList<EComponent> eComps = new ArrayList<EComponent>();

	public Simulator() {

		ImageTools.loadImages();

		gates.add(new AND(100, 100));
		eComps.addAll(gates);

		gates.get(0).inTop = new Input(States.ON);
		gates.get(0).inBottom = new Input(States.ON);

		this.add(panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g1) {
				super.paintComponent(g1);
				Graphics2D g = (Graphics2D) g1;
				for (EComponent eComp : eComps)
					eComp.draw(g);
			}
		});
		panel.setBackground(Color.white);

		this.setSize(1000, 800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		panel.addMouseMotionListener(this);
		panel.addMouseListener(this);
		this.addKeyListener(this);
		new Thread(this).start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					repaint();

					try {
						Thread.sleep(1000 / ups);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public void run() {
		while (true) {

			for (EComponent eComp : eComps)
				eComp.update();

			try {
				Thread.sleep(1000 / ups);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	Point mouseDraggedLast = new Point(0, 0);

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDraggedLast.setLocation(mouse);
		mouse = e.getPoint();

		for (Gate gate : gates)
			if (gate.pickedUp) {
				gate.translate((mouse.x - mouseDraggedLast.x), (mouse.y - mouseDraggedLast.y));
				// say("moved by :" + (mouse.x-mouseDraggedLast.x) + " " +
				// (mouse.y-mouseDraggedLast.y));
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
		for (EComponent eComp : eComps)
			if (eComp.checkIfClicked(e.getPoint())) {
				eComp.pickup();
				break;
			}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (Gate gate : gates)
			if (gate.pickedUp) {
				gate.drop();
				
			}
		
		for (EComponent eComp : eComps)
			if (eComp.pickedUp){
				eComp.drop();
				if (eComp.hasInputs()){
					
				}
				if (eComp.hasOutputs()){
					
				}
//				if (eComp instanceof Gate){
//					for (Gate gate2 : gates)
//						if (eComp != gate2)
//							gate2.checkForMatchedOutput((Gate) eComp);
//				}else if (eComp instanceof Switch){
//					
//				}
			}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == 'a') {
			addGate(new AND(200, 200));
		} else if (e.getKeyChar() == 'o') {
			addGate( new OR(200, 200));
		}
		else if (e.getKeyChar() == 's') {
			eComps.add(new Switch(mouse.x, mouse.y));
		}else
			say(e.getKeyChar() + " " + e.getKeyCode());

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
	
	private void addGate(Gate gate){
		gates.add(gate);
		eComps.add(gate);
	}

}
