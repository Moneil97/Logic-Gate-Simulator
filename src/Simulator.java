import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class Simulator extends JFrame implements Runnable, MouseMotionListener, MouseListener, KeyListener {

	public static int ups = 30;
	public static Point mouse = new Point(0, 0);
	private JPanel panel;
	protected static ArrayList<EComponent> eComps = new ArrayList<EComponent>();
	protected static WireCreator creator;
	protected static ArrayList<UserLabel> labels = new ArrayList<UserLabel>();
	private Point mouseDraggedLast = new Point(0, 0);
	private boolean dragged = false;
	protected static ArrayList<Wire> wires = new ArrayList<Wire>();
	protected DefaultPopup defaultPopup = new DefaultPopup();

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
				for (UserLabel label : labels)
					label.draw(g);
				if (creator != null)
					creator.draw(g, mouse);
			}
		});
		panel.setBackground(Color.white);
		
		class MyMenuBar extends JMenuBar{
			
			public MyMenuBar() {
				
				JMenu tools = new JMenu("Tools");
					JMenuItem reset = new JMenuItem("Clear");
					reset.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							reset();
						}
					});
					tools.add(reset);
				this.add(tools);
				
			}
		}

		this.setJMenuBar(new MyMenuBar());
		this.setTitle("Logic Gate Simulator [Cameron O'Neil]");
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

	@Override
	public void mouseDragged(MouseEvent e) {
		dragged = true;
		mouseDraggedLast.setLocation(mouse);
		mouse = e.getPoint();
		
		int xOff = mouse.x - mouseDraggedLast.x;
		int yOff = mouse.y - mouseDraggedLast.y;
		
		for (UserLabel label : labels)
			if (label.isPickedUp())
				label.translate(xOff, yOff);

		for (EComponent eComp : eComps)
			if (eComp.isPickedUp())
				eComp.translate(xOff, yOff);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouse = e.getPoint();
	}

	public static void say(Object x) {
		System.out.println(x);
	}

	public static void main(String[] args) {
		new Simulator();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		
		if (e.getButton() == MouseEvent.BUTTON1){
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
				if (creator.inputParent != null && creator.outputParent != null){
					wires.add(creator.create());
					creator = null;
				}
			}
			
			for (EComponent eComp : eComps)
				if (eComp.contains(e.getPoint())) {
					eComp.setPickedUp(true);
					break;
				}
			
			for (UserLabel label : labels)
				if (label.contains(e.getPoint())){
					label.pickUp();
					break;
				}
		}
		else if (e.getButton() == MouseEvent.BUTTON3){
			
			for (EComponent eComp : eComps)
				if (eComp.contains(e.getPoint())) {
					eComp.doPopup(e.getComponent(), e.getX(), e.getY());
					return;
				}
			
			for (UserLabel label : labels)
				if (label.contains(e.getPoint())){
					label.doPopup(e.getComponent(), e.getX(), e.getY());
					return;
				}
			
			defaultPopup.show(e.getComponent(), e.getX(), e.getY());
			
		}
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (dragged) {
			for (EComponent droppedEComp : eComps)
				if (droppedEComp.isPickedUp()) {
					droppedEComp.setPickedUp(false);
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
			for (UserLabel label : labels)
				if (label.isPickedUp()){
					label.drop();
				}
			dragged = false;
		} else {
			
			for (EComponent eComp : eComps)
				if (eComp.isPickedUp()) {
					if (eComp instanceof Switch)
						((Switch) eComp).toggle();
					eComp.setPickedUp(false);
				}
			for (UserLabel label : labels)
				if (label.isPickedUp()){
					label.drop();
				}
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'a') {
			eComps.add(new AND(mouse.x, mouse.y));
		} else if (e.getKeyChar() == 'o') {
			eComps.add(new OR(mouse.x, mouse.y));
		} else if (e.getKeyChar() == 'n') {
			eComps.add(new NOT(mouse.x, mouse.y));
		} else if (e.getKeyChar() == 'x') {
			eComps.add(new XOR(mouse.x, mouse.y));
		} else if (e.getKeyChar() == 's') {
			eComps.add(new Switch(mouse.x, mouse.y));
		}else if (e.getKeyChar() == 'd') {
			eComps.add(new Splitter(mouse.x, mouse.y));
		}else if (e.getKeyChar() == 'c') {
			creator = new WireCreator();
		}else if (e.getKeyChar() == 'h') {
			eComps.add(new Hub(mouse.x, mouse.y));
		}else if (e.getKeyChar() == 'l') {
			eComps.add(new LCD(mouse.x, mouse.y));
		}else if (e.getKeyChar() == 'b') {
			eComps.add(new HalfAdder(mouse.x, mouse.y));
		}else if (e.getKeyChar() == 't') {
			labels.add(new UserLabel(mouse.x, mouse.y));
		}else if (e.getKeyCode() == 127) {
			delete(mouse);
		}else
			say(e.getKeyChar() + " " + e.getKeyCode() + " " + e.getExtendedKeyCode());
	}
	
	/**
	 * Find eComp at point and delete it
	 * @param p Point to delete eComp at
	 */
	
	protected static void delete(Point p) {
		// Loop though all eComps to find which one (if any) to delete
		Iterator<EComponent> iter = eComps.iterator();
		while (iter.hasNext()) {
			EComponent nextElement = iter.next();
			if (nextElement.contains(p)) {
				// Found one to delete
				EComponent deleted = nextElement;
				removeConnections(deleted);
				// Finally remove the eComp
				iter.remove();
				// We only want to delete one of them
				break;
			}
		}

		Iterator<UserLabel> labelIter = labels.iterator();
		while (labelIter.hasNext()) {
			if (labelIter.next().contains(p)) {
				labelIter.remove();
			}
		}
	}
	
	protected static void delete(EComponent toDelete){
		removeConnections(toDelete);
		eComps.remove(toDelete);
	}

	/**
	 * Delete specified eComp and all wires associated with it
	 */
	
	protected static void removeConnections(EComponent toDelete) {
		
		// Loop through all wires to find ones connected to the deleted eComp
		Iterator<Wire> wireIter = wires.iterator();
		while (wireIter.hasNext()) {
			Wire wire = wireIter.next();

			// Find Wire Inputs connected to the deleted eComp Outputs
			for (int i = 0; i < wire.inputs.length; i++) {
				for (int j = 0; j < toDelete.outputs.length; j++) {
					if (wire.inputs[i].isConnectedTo(toDelete.outputs[j])) {

						// Have found a wire to delete (Wires only have one
						// Output object)
						Output deletedWireOutput = wire.outputs[0];

						// Loop through all the eComps again to find inputs
						// connected to the outputs of the deleted wires
						Iterator<EComponent> iter2 = eComps.iterator();
						while (iter2.hasNext()) {
							EComponent next = iter2.next();
							if (next != toDelete) {
								// Loop through each input to see if it is
								// connected to the deleted output
								for (int r = 0; r < next.inputs.length; r++) {
									if (next.inputs[r].isConnectedTo(deletedWireOutput)) {
										// Remove deleted output from Input's
										// list
										next.inputs[r].disconnect(deletedWireOutput);
									}
								}
							}
						}
						// Delete the wire
						wireIter.remove();
					}
				}
			}

			// Find Wire Outputs connected to the deleted eComp Inputs
			for (int i = 0; i < wire.outputs.length; i++) {
				for (int j = 0; j < toDelete.inputs.length; j++) {
					if (toDelete.inputs[j].isConnectedTo(wire.outputs[i])) {
						// Can just remove the wire, Inputs store everything,
						// outputs do not store anything, so do not need to be
						// reset
						wireIter.remove();
					}
				}
			}
		}

		// Loop through all physical contacts
		Iterator<EComponent> otherIter = eComps.iterator();
		while (otherIter.hasNext()) {
			EComponent other = otherIter.next();

			for (int i = 0; i < toDelete.outputs.length; i++) {
				for (int j = 0; j < other.inputs.length; j++) {
					if (other.inputs[j].isConnectedTo(toDelete.outputs[i])) {
						other.inputs[j].disconnect(toDelete.outputs[i]);
					}
				}
			}

		}
	}
	
	/**
	 * Deletes all eComps, wires, and labels
	 */
	
	private void reset(){
		eComps.clear();
		wires.clear();
		labels.clear();
	}


	@Override
	public void keyReleased(KeyEvent e) {}

	/**
	 * The pop-up that will display if right-clicked on no objects
	 * @author Cameron O'Neil
	 */
	
	class CreateGateActionListener implements ActionListener{

		private final int width, height;
		private final Gates type;
		private final Point location;
		
		public CreateGateActionListener(Gates gate, Sizes size, Point location) {
			this.type = gate;
			this.location = location;
			this.width = Math.round(Gate.DEFAULT_WIDTH * size.getRatio());
			this.height = Math.round(Gate.DEFAULT_HEIGHT * size.getRatio());
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (type){
				case AND:
					say(eComps);
					say(location);
					say(width);
					say(height);
					eComps.add(new AND(location.x,location.y,width,height));
					break;
				case OR:
					eComps.add(new OR(location.x,location.y,width,height));
					break;
				case NOT:
					eComps.add(new NOT(location.x,location.y,width,height));
					break;
				case XOR:
					eComps.add(new XOR(location.x,location.y,width,height));
					break;
				default:
					System.err.println("Default Case!");
					break;
			
			}
		}
		
	}
	
	class DefaultPopup extends JPopupMenu{
		
		/**
		 * Point stored to be used to place objects.
		 * Is final since passed to CreateGateActionListener
		 */
		private final Point mouseLocation = new Point();
		
		public DefaultPopup() {
			
				JMenu gates = new JMenu("Create Gate: ");
					JMenu andGate = new JMenu("AND Gate");
						JMenuItem smallAnd = new JMenuItem(Strings.SMALL);
							smallAnd.addActionListener(new CreateGateActionListener(Gates.AND, Sizes.small, mouseLocation));
							andGate.add(smallAnd);
						JMenuItem mediumAnd = new JMenuItem(Strings.MEDUIM);
							mediumAnd.addActionListener(new CreateGateActionListener(Gates.AND, Sizes.medium, mouseLocation));
							andGate.add(mediumAnd);
						JMenuItem largeAnd = new JMenuItem(Strings.LARGE);
							largeAnd.addActionListener(new CreateGateActionListener(Gates.AND, Sizes.large, mouseLocation));
							andGate.add(largeAnd);
					gates.add(andGate);
					JMenuItem orGate = new JMenuItem("OR Gate");
						orGate.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								Simulator.eComps.add(new OR(mouseLocation.x, mouseLocation.y));
							}
						});
					gates.add(orGate);
					JMenuItem xorGate = new JMenuItem("XOR Gate");
						xorGate.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								Simulator.eComps.add(new XOR(mouseLocation.x, mouseLocation.y));
							}
						});
					gates.add(xorGate);
					JMenuItem notGate = new JMenuItem("NOT Gate");
						notGate.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								Simulator.eComps.add(new NOT(mouseLocation.x, mouseLocation.y));
							}
						});
					gates.add(notGate);
					JMenuItem halfAdder = new JMenuItem("Half Adder");
						halfAdder.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								Simulator.eComps.add(new HalfAdder(mouseLocation.x, mouseLocation.y));
							}
						});
					gates.add(halfAdder);
			this.add(gates);
				JMenu other = new JMenu("Create Other: ");
					JMenuItem lcd = new JMenuItem("LCD");
					lcd.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							Simulator.eComps.add(new LCD(mouseLocation.x, mouseLocation.y));
						}
					});
					other.add(lcd);
					JMenuItem wire = new JMenuItem("Wire");
					wire.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							Simulator.creator = new WireCreator();
						}
					});
					other.add(wire);
					JMenuItem onOff = new JMenuItem("Switch");
					onOff.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							Simulator.eComps.add(new Switch(mouseLocation.x, mouseLocation.y));
						}
					});
					other.add(onOff);
			this.add(other);
				
				JMenuItem label = new JMenuItem("Create Label");
				label.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Simulator.labels.add(new UserLabel(mouseLocation.x, mouseLocation.y));
					}
				});
			this.add(label);
			
		}
		
		@Override
		public void show(Component invoker, int x, int y) {
			mouseLocation.setLocation(x,y);
			super.show(invoker, x, y);
		}
		
	}
	
}
