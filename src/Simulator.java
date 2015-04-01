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

	public static int ups = 30;
	public static Point mouse = new Point(0, 0);
	private JPanel panel;
	private ArrayList<Gate> gates = new ArrayList<Gate>();
	private ArrayList<Switch> switches = new ArrayList<Switch>();
	private ArrayList<EComponent> eComps = new ArrayList<EComponent>();

	public Simulator() {

		ImageTools.loadImages();

		this.add(panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g1) {
				super.paintComponent(g1);
				Graphics2D g = (Graphics2D) g1;
				for (EComponent eComp : eComps)
					eComp.draw(g);
				for (Wire wire : wires)
					wire.draw(g);
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
			
			for (Wire wire : wires)
				wire.update();

			try {
				Thread.sleep(1000 / ups);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	Point mouseDraggedLast = new Point(0, 0);
	boolean dragged = false;
	ArrayList<Wire> wires = new ArrayList<Wire>();

	@Override
	public void mouseDragged(MouseEvent e) {
		dragged = true;
		mouseDraggedLast.setLocation(mouse);
		mouse = e.getPoint();

		for (EComponent eComp : eComps)
			if (eComp.isPickedUp())
				eComp.translate(mouse.x - mouseDraggedLast.x, mouse.y - mouseDraggedLast.y);
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
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		
		if (creator != null){
			for (EComponent eComp : eComps){
				if (eComp.getInputHovers() != null)
					for (int i=0; i < eComp.getInputHovers().length; i++)
						if (eComp.getInputHovers()[i].contains(mouse))
							creator.setInputParent(eComp, i);
				for (int i=0; i < eComp.getOutputHovers().length; i++)
					if (eComp.getOutputHovers()[i].contains(mouse))
						creator.setOutputParent(eComp, i);
			}
			if (creator.parent1 != null && creator.parent2 != null){
				wires.add(creator.create());
				creator = null;
			}
		}
		
		for (EComponent eComp : eComps)
			if (eComp.checkIfClicked(e.getPoint())) {
				eComp.pickup();
				break;
			}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (dragged) {
			for (EComponent droppedEComp : eComps)
				if (droppedEComp.isPickedUp()) {
					droppedEComp.drop();
					if (droppedEComp.hasInputs()) {
						say(droppedEComp + " has inputs");
						for (EComponent otherEComp : eComps) {
							if (otherEComp != droppedEComp)
								if (otherEComp.hasOutputs()) {
									say("   " + otherEComp + " has outputs");
									droppedEComp.acceptOutput(otherEComp);
								}
						}
					}
					if (droppedEComp.hasOutputs()) {
						say(droppedEComp + " has outputs");
						for (EComponent otherEComp : eComps) {
							if (otherEComp != droppedEComp)
								if (otherEComp.hasInputs()) {
									say("   " + otherEComp + " has inputs");
									droppedEComp.acceptInput(otherEComp);
								}
						}
					}
				}
			dragged = false;
		} else {
			for (Switch s : switches)
				if (s.checkIfClicked(mouse)) {
					s.toggle();
					s.drop();
				}
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	private WireCreator creator;

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == 'a') {
			addGate(new AND(mouse.x, mouse.y));
		} else if (e.getKeyChar() == 'o') {
			addGate(new OR(mouse.x, mouse.y));
		} else if (e.getKeyChar() == 'n') {
			addGate(new NOT(mouse.x, mouse.y));
		} else if (e.getKeyChar() == 'x') {
			addGate(new XOR(mouse.x, mouse.y));
		} else if (e.getKeyChar() == 's') {
			addSwitch(new Switch(mouse.x, mouse.y));
		}else if (e.getKeyChar() == 'd') {
			eComps.add(new Splitter(mouse.x, mouse.y));
		}else if (e.getKeyChar() == 'c') {
			creator = new WireCreator();
		}else if (e.getKeyChar() == 'h') {
			eComps.add(new Hub(mouse.x, mouse.y));
		} else
			say(e.getKeyChar() + " " + e.getKeyCode());

	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	private void addGate(Gate gate) {
		gates.add(gate);
		eComps.add(gate);
	}

	private void addSwitch(Switch sw) {
		switches.add(sw);
		eComps.add(sw);
	}

}
