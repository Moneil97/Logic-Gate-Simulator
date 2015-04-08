# Logic-Gate-Simulator

To run the program:

	Make sure you have the latest version of Java (Java 8) to run this program. It will not work with Java 7 or below

  A.
  
    1. Download ZIP
    
    2. Run the "Logic Gate Simulator.jar"
    
  B.
  
    1. Compile the code yourself
    
    2. main method is in "Simulator.java"
    


Help:

	Right click anywhere there is not already a componet to get the main creation menu, from here you can create any of the available components. You can create either a small, medium, or large version of that object
	
	Right click on a component to get that component's customized edit menu. This included things such as removing connections, deleting, resizing, changing fonts and text of labels, etc.
	
	To create a connection you can either physically connect the two components by touching the input of one component to the output of another. 
	Another way of doing it is to create a wired connection. First create a new wire and click an input or output of one of the components, then attach the wire to another component (Input --> Output) or (Output --> Input).
	You can have multiple wires leading to one input and multiple wires leaving one output. If you have multiple connections, just having one connection on will override the rest, so for it to be off, every single connection must be off.
	
LCD:

	This has an input and no outputs. If any connected input is on, it will display a 1. If all the connections are off (or there are no connections) it will display a zero. This correlates with binary (1 == ON, 0 == OFF).
	
