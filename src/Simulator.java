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
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Simulator extends JFrame implements Runnable, MouseMotionListener, MouseListener, KeyListener {

	public static int ups = 30;
	public static Point mouse = new Point(0, 0);
	private JPanel panel;
	private ArrayList<EComponent> eComps = new ArrayList<EComponent>();
	private WireCreator creator;
	private ArrayList<UserLabel> labels = new ArrayList<UserLabel>();
	private Point mouseDraggedLast = new Point(0, 0);
	private boolean dragged = false;
	private ArrayList<Wire> wires = new ArrayList<Wire>();

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
		}else if (e.getKeyChar() == 't') {
			labels.add(new UserLabel(mouse.x, mouse.y));
		}else if (e.getKeyCode() == 127) {
			delete();
		}else
			say(e.getKeyChar() + " " + e.getKeyCode() + " " + e.getExtendedKeyCode());
	}

	/**
	 * Delete eComp that mouse is hovering over
	 */
	
	private void delete() {
		
		//Loop though all eComps to find which one (if any) to delete
		Iterator<EComponent> iter = eComps.iterator();
		while (iter.hasNext()){
			EComponent nextElement = iter.next();
			if (nextElement.contains(mouse)){
				//Found one to delete
				EComponent deleted = nextElement;
				
				//Loop through all wires to find ones connected to the deleted eComp
				Iterator<Wire> wireIter = wires.iterator();
				while (wireIter.hasNext()){
					Wire wire = wireIter.next();
					
					//Find Wire Inputs connected to the deleted eComp Outputs
					for (int i =0; i < wire.inputs.length; i++){
						for (int j =0; j < deleted.outputs.length; j++){
							if (wire.inputs[i].isConnectedTo(deleted.outputs[j])){
								
								//Have found a wire to delete (Wires only have one Output object)
								Output deletedWireOutput = wire.outputs[0];
								
								//Loop through all the eComps again to find inputs connected to the outputs of the deleted wires
								Iterator<EComponent> iter2 = eComps.iterator();
								while (iter2.hasNext()){
									EComponent next = iter2.next();
									if (next != deleted){
										//Loop through each input to see if it is connected to the deleted output
										for (int r=0; r < next.inputs.length; r++){
											if (next.inputs[r].isConnectedTo(deletedWireOutput)){
												//Remove deleted output from Input's list
												next.inputs[r].disconnect(deletedWireOutput);
											}
										}	
									}
								}
								//Delete the wire
								wireIter.remove();
							}
						}
					}
					
					//Find Wire Outputs connected to the deleted eComp Inputs
					for (int i =0; i < wire.outputs.length; i++){
						for (int j =0; j < deleted.inputs.length; j++){
							if (deleted.inputs[j].isConnectedTo(wire.outputs[i])){
								//Can just remove the wire, Inputs store everything,
								//outputs do not store anything, so do not need to be reset
								wireIter.remove();
							}
						}
					}
				}
				
				
				//Loop through all physical contacts
				Iterator<EComponent> otherIter = eComps.iterator();
				while (otherIter.hasNext()){
					EComponent other = otherIter.next();
					
					for (int i=0; i < deleted.outputs.length; i++){
						for (int j=0; j < other.inputs.length; j++){
							if (other.inputs[j].isConnectedTo(deleted.outputs[i])){
								other.inputs[j].disconnect(deleted.outputs[i]);
							}
						}
					}
					
				}
				
				
				//Finally remove the eComp
				iter.remove();
				//We only want to delete one of them
				break;
			}
		}
			
		Iterator<UserLabel> labelIter = labels.iterator();
		while (labelIter.hasNext()){
			if (labelIter.next().contains(mouse)){
				labelIter.remove();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
