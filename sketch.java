/***************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 * 
 * Final Project: This project is a minimal implementation of the SketchPad
 * demo, which was developed at MIT Lincoln Labs, by Sutherland in 1963 [1].
 * 
 * In this demo, we allow the user to specify (with the mouse) a primitive
 * shape from one of a set of buttons at the top of the screen, then define
 * it's location on the screen by left-clicking with the mouse. The user may
 * also select a set of primitives already placed on the screen, and combine
 * them into a single object (which may then be used/inserted as though it
 * were a primitive). Finally, the user may specify certain constraints on the
 * geometry (such as lines being parallel), and the system will satisfy the
 * constraints if possible, and if not possible it will output an error message
 * to that effect.
 *
 * [1] Ivan E. Sutherland. "SKETCHPAD: A MAN-MACHINE GRAPHICAL COMMUNICATION
 *     SYSTEM." Spring Joint Computer Conference, 1963.
 *
 * Dependencies: StdDraw.java, Queue.java
 * 
 * Other files in this project: 
 * -- Canvas.java = an abstraction for the drawing canvas, which also includes
 *                  an optimizeGeometry() function
 * -- Button.java = an abstraction for a button
 * -- Point.java = a point primitive object
 * -- Line.java = a line primitive object
 * -- Composite.java = an object which represents a collection of primitives
 *                     and constraints upon those primitives
 * -- SamePointConstraint.java = constraint that makes two points identical
 * -- ParallelLineConstraint.java = constraint that makes lines parallel
 * -- PerpendicularLineConstraint.java = constraint makes lines perpendicular
 * -- SameLengthConstraint.java = constraint that makes lines same length
 *
 * Note: Constraints are constructed to keep only references to primitives,
 * so that by executing a constraint we affect the original primitive, which
 * allows us to maintain separate lists of primitives and constraints, and
 * cycle through the constraints and execute them sequentially, trusting that
 * they will affect the correct primitives.
 ***************************************************************************/

public class Sketch {

    // delay in polling loop
    private static final int delayMillis = 100;

    // allow user to place a point on the canvas
    private static void handlePoint(Canvas canvas) {
	
    	// wait for next mouse click
	waitForMouse();
	
	// create a point and add to canvas
	Point p = new Point(StdDraw.mouseX(), StdDraw.mouseY());
	canvas.addShape(p);
	canvas.show();
    }

    // allow user to place a line on the canvas
    private static void handleLine(Canvas canvas) {
	
	// create two points from mouse clicks
	waitForMouse();
	Point p = new Point(StdDraw.mouseX(), StdDraw.mouseY());
	waitForMouse();
	Point q = new Point(StdDraw.mouseX(), StdDraw.mouseY());

	// create line and add to canvas
	Line l = new Line(p, q);
	canvas.addShape(l);
	canvas.show();
    }

    // handle all other composite collections of primitives
    private static void handleComposite(Canvas canvas) {}

    // allow user to select a group of primitives and bundle into a composite
    private static void handleGroup(Canvas canvas) {}

    // handle same point constraint
    private static void handleSamePoint(Canvas canvas) {}

    // handle parallel lines constraint
    private static void handleParallel(Canvas canvas) {}

    // handle perpendicular lines constraint
    private static void handlePerpendicular(Canvas canvas) {}

    // handle same length constraint
    private static void handleSameLength(Canvas canvas) {}

    // helper method: select a primitive
    private static Shape select(Canvas canvas) {}

    // helper method: wait for mouse click
    private static void waitForMouse() {
	while (true) {
	    Thread.sleep(delayMillis);
	    if (StdDraw.mousePressed()) {
		return;
	    }
	}
    }

    // implement a finite-state machine to draw different primitives
    public static void main(String[] args) {
	
	// set up canvas with buttons
	Canvas canvas = new Canvas();

	canvas.addButton(0.05, 0.95, 0.04, 0.02, "Point");
	canvas.addButton(0.1, 0.95, 0.04, 0.02, "Line");
	canvas.addButton(0.15, 0.95, 0.04, 0.02, "Composite");
	canvas.addButton(0.2, 0.95, 0.04, 0.02, "Group");

	canvas.addButton(0.25, 0.95, 0.04, 0.02, "Same Point");
	canvas.addButton(0.3, 0.95, 0.04, 0.02, "Parallel");
	canvas.addButton(0.35, 0.95, 0.04, 0.02, "Perpendicular");
	canvas.addButton(0.4, 0.95, 0.04, 0.02, "Same Length");

	// main loop
	while (true) {
	    Thread.sleep(delayMillis);
	    
	    // if mouse is pressed determine which state to enter
	    if (StdDraw.mousePressed()) {
		String state = canvas.whichButton();
		
		if (state.equals("Point"))
		    handlePoint(canvas);
		else if (state.equals("Line"))
		    handleLine(canvas);
		else if (state.equals("Composite"))
		    handleComposite(canvas);
		else if (state.equals("Group"))
		    handleGroup(canvas);
		else if (state.equals("Same Point"))
		    handleSamePoint(canvas);
		else if (state.equals("Parallel"))
		    handleParallel(canvas);
		else if (state.equals("Perpendicular"))
		    handlePerpendicular(canvas);
		else if (state.equals("Same Length"))
		    handleSameLength(canvas);
	    }
	}

    }
}