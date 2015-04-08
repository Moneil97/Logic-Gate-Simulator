import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class Simulator extends JFrame implements Runnable, MouseMotionListener, MouseListener, KeyListener {

	public static final int ups = 30;
	public static final Point mouse = new Point(0, 0);
	private final JPanel panel;
	protected static final ArrayList<EComponent> eComps = new ArrayList<EComponent>();
	protected static WireCreator creator;
	protected static final ArrayList<UserLabel> labels = new ArrayList<UserLabel>();
	private final Point mouseDraggedLast = new Point(0, 0);
	private boolean dragged = false;
	protected static ArrayList<Wire> wires = new ArrayList<Wire>();
	protected final DefaultPopup defaultPopup = new DefaultPopup();
	private boolean menu = true;
	private final SimpleAbstractButton begin = new SimpleAbstractButton("Begin", 420, 200, 100, 60, 20, 20){
		
		@Override
		void onPress() {
			menu = false;
		}
		
	};
	

	public Simulator() {

		ImageTools.loadImages();

		this.add(panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g1) {
				super.paintComponent(g1);
				Graphics2D g = (Graphics2D) g1;
				g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				
//				Center Line
				g.drawLine(this.getWidth()/2, 0, this.getWidth()/2,  this.getHeight());
				
				if (menu){
					
					int xOff = 10;
					begin.draw(g);
					begin.setX(450-xOff);
					begin.setY(230);
					g.setFont(new Font("Arial", Font.PLAIN, 90));
					int y = 80;
					g.drawString("Logic Gate", 300-xOff, y);
					g.drawString("Simulator", 320-xOff, y+80);
					
					g.setFont(new Font("Arial", Font.PLAIN, 50));
					g.drawString("Cameron O'Neil", 330-xOff, y+130);
					
				}
				else{
					for (EComponent eComp : eComps)
						eComp.draw(g);
					for (Wire wire : wires)
						wire.draw(g);
					for (UserLabel label : labels)
						label.draw(g);
					if (creator != null)
						creator.draw(g, mouse);
				}
				
			}
		});
		panel.setBackground(Color.white);
		
		class MyMenuBar extends JMenuBar{
			
			public MyMenuBar() {
				
				JMenu file = new JMenu("File");
					JMenuItem reset = new JMenuItem("Clear All");
					reset.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							reset();
						}
					});
					file.add(reset);
					
					JMenuItem source = new JMenuItem("Source Code");
					source.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								openLink(new URI("https://github.com/Moneil97/Logic-Gate-Simulator"));
							} catch (Exception e1) {
								JOptionPane.showInputDialog(panel,
										"Could not load URL on system broswer\nPlease copy and paste this link instead",
										"https://github.com/Moneil97/Logic-Gate-Simulator");
							}
						}
						
					});
					file.add(source);
				this.add(file);
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
			
			if (menu){
				begin.updateHover(mouse);
			}
			else{
				for (EComponent eComp : eComps)
					eComp.update();
				
				for (Wire wire : wires)
					wire.update();
			}

			try {
				Thread.sleep(1000 / ups);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
	
	public static void openLink(URI uri) throws Exception {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			desktop.browse(uri);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		dragged = true;
		mouseDraggedLast.setLocation(mouse);
		mouse.setLocation(e.getPoint());// = e.getPoint();
		
		if (!menu){
			int xOff = mouse.x - mouseDraggedLast.x;
			int yOff = mouse.y - mouseDraggedLast.y;
			
			for (UserLabel label : labels)
				if (label.isPickedUp())
					label.translate(xOff, yOff);
	
			for (EComponent eComp : eComps)
				if (eComp.isPickedUp())
					eComp.translate(xOff, yOff);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouse.setLocation(e.getPoint());// = e.getPoint();
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
		
		if (menu){
			begin.updatePressed(e.getPoint());
		}
		else{
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
			this.width = Math.round((gate == Gates.HALF_ADDER ? HalfAdder.DEFAULT_WIDTH:Gate.DEFAULT_WIDTH) * size.getRatio());
			this.height = Math.round((gate == Gates.HALF_ADDER ? HalfAdder.DEFAULT_HEIGHT:Gate.DEFAULT_HEIGHT) * size.getRatio());
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (type){
				case AND:
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
				case HALF_ADDER:
					eComps.add(new HalfAdder(location.x,location.y,width,height));
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
					
					JMenu orGate = new JMenu("OR Gate");
						JMenuItem smallOr = new JMenuItem(Strings.SMALL);
							smallOr.addActionListener(new CreateGateActionListener(Gates.OR, Sizes.small, mouseLocation));
							orGate.add(smallOr);
						JMenuItem mediumOr = new JMenuItem(Strings.MEDUIM);
							mediumOr.addActionListener(new CreateGateActionListener(Gates.OR, Sizes.medium, mouseLocation));
							orGate.add(mediumOr);
						JMenuItem largeOr = new JMenuItem(Strings.LARGE);
							largeOr.addActionListener(new CreateGateActionListener(Gates.OR, Sizes.large, mouseLocation));
							orGate.add(largeOr);
					gates.add(orGate);
					
					JMenu xorGate = new JMenu("XOR Gate");
						JMenuItem smallXor = new JMenuItem(Strings.SMALL);
							smallXor.addActionListener(new CreateGateActionListener(Gates.XOR, Sizes.small, mouseLocation));
							xorGate.add(smallXor);
						JMenuItem mediumXor = new JMenuItem(Strings.MEDUIM);
							mediumXor.addActionListener(new CreateGateActionListener(Gates.XOR, Sizes.medium, mouseLocation));
							xorGate.add(mediumXor);
						JMenuItem largeXor = new JMenuItem(Strings.LARGE);
							largeXor.addActionListener(new CreateGateActionListener(Gates.XOR, Sizes.large, mouseLocation));
							xorGate.add(largeXor);
					gates.add(xorGate);
					
					JMenu notGate = new JMenu("NOT Gate");
						JMenuItem smallNot = new JMenuItem(Strings.SMALL);
							smallNot.addActionListener(new CreateGateActionListener(Gates.NOT, Sizes.small, mouseLocation));
							notGate.add(smallNot);
						JMenuItem mediumNot = new JMenuItem(Strings.MEDUIM);
							mediumNot.addActionListener(new CreateGateActionListener(Gates.NOT, Sizes.medium, mouseLocation));
							notGate.add(mediumNot);
						JMenuItem largeNot = new JMenuItem(Strings.LARGE);
							largeNot.addActionListener(new CreateGateActionListener(Gates.NOT, Sizes.large, mouseLocation));
							notGate.add(largeNot);
					gates.add(notGate);
					
					JMenu halfAdder = new JMenu("Half Adder");
						JMenuItem smallHalfAdder = new JMenuItem(Strings.SMALL);
							smallHalfAdder.addActionListener(new CreateGateActionListener(Gates.HALF_ADDER, Sizes.small, mouseLocation));
							halfAdder.add(smallHalfAdder);
						JMenuItem mediumHalfAdder = new JMenuItem(Strings.MEDUIM);
							mediumHalfAdder.addActionListener(new CreateGateActionListener(Gates.HALF_ADDER, Sizes.medium, mouseLocation));
							halfAdder.add(mediumHalfAdder);
						JMenuItem largeHalfAdder = new JMenuItem(Strings.LARGE);
							largeHalfAdder.addActionListener(new CreateGateActionListener(Gates.HALF_ADDER, Sizes.large, mouseLocation));
							halfAdder.add(largeHalfAdder);
					gates.add(halfAdder);
					
			this.add(gates);
				JMenu other = new JMenu("Create Other: ");
					JMenuItem wire = new JMenuItem("Wire");
					wire.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							Simulator.creator = new WireCreator();
						}
					});
					other.add(wire);
					
					JMenu lcd = new JMenu("LCD");
						JMenuItem smallLCD = new JMenuItem(Strings.SMALL);
						smallLCD.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								Simulator.eComps.add(new LCD(mouseLocation.x, mouseLocation.y, Math.round(LCD.DEFAULT_WIDTH * Sizes.small.getRatio()),
										Math.round(LCD.DEFAULT_HEIGHT * Sizes.small.getRatio())));
							}
						});
						lcd.add(smallLCD);
						
						JMenuItem mediumLCD = new JMenuItem(Strings.MEDUIM);
						mediumLCD.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								Simulator.eComps.add(new LCD(mouseLocation.x, mouseLocation.y, Math.round(LCD.DEFAULT_WIDTH * Sizes.medium.getRatio()),
										Math.round(LCD.DEFAULT_HEIGHT * Sizes.medium.getRatio())));
							}
						});
						lcd.add(mediumLCD);
						
						JMenuItem largeLCD = new JMenuItem(Strings.LARGE);
						largeLCD.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								Simulator.eComps.add(new LCD(mouseLocation.x, mouseLocation.y, Math.round(LCD.DEFAULT_WIDTH * Sizes.large.getRatio()),
										Math.round(LCD.DEFAULT_HEIGHT * Sizes.large.getRatio())));
							}
						});
						lcd.add(largeLCD);
					other.add(lcd);
					
					JMenu switches = new JMenu("Switch");
						JMenuItem smallSwitch = new JMenuItem(Strings.SMALL);
							smallSwitch.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									Simulator.eComps.add(new Switch(mouseLocation.x, mouseLocation.y, Math.round(Switch.DEFAULT_WIDTH * Sizes.small.getRatio()),
											Math.round(Switch.DEFAULT_HEIGHT * Sizes.small.getRatio())));
								}
							});
						switches.add(smallSwitch);
						
						JMenuItem medSwitch = new JMenuItem(Strings.MEDUIM);
						medSwitch.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								Simulator.eComps.add(new Switch(mouseLocation.x, mouseLocation.y, Math.round(Switch.DEFAULT_WIDTH * Sizes.medium.getRatio()),
										Math.round(Switch.DEFAULT_HEIGHT * Sizes.medium.getRatio())));
							}
						});
						switches.add(medSwitch);
						
						JMenuItem largeSwitch = new JMenuItem(Strings.LARGE);
						largeSwitch.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								Simulator.eComps.add(new Switch(mouseLocation.x, mouseLocation.y, Math.round(Switch.DEFAULT_WIDTH * Sizes.large.getRatio()),
										Math.round(Switch.DEFAULT_HEIGHT * Sizes.large.getRatio())));
							}
						});
						switches.add(largeSwitch);
						
					other.add(switches);
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
