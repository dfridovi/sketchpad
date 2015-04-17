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

import java.awt.event.KeyEvent;

public class Sketch {

    // constants
    private static final int delayMillis = 100;
    private static final double halfWidth = 0.12;
    private static final double halfHeight = 0.03;
    private static final double margin = 0.01;
    private static final double tolerance = 0.05;

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

	// also add points themselves to canvas
	canvas.addShape(p);
	canvas.addShape(q);
	canvas.show();
    }

    // handle all other composite collections of primitives
    private static void handleComposite(Canvas canvas) {}

    // allow user to select a group of primitives and bundle into a composite
    private static void handleGroup(Canvas canvas) {
	Queue<Shape> group = new Queue<Shape>();

	// capture mouse clicks with select() until the user presses 'enter',
	// then create a composite shape and add to the canvas
	try {
	    while (!StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) {
		Thread.sleep(delayMillis);
		
		if (StdDraw.mousePressed()) {
		    Shape click = canvas.findNearestShape(tolerance);
		    if (click == null)
			System.out.println("ERROR: no shape returned.");
		    else
			group.enqueue(click);
		}
	    }
	
	    canvas.addShape(new Composite(group));
	} catch (Exception e) {
	    System.err.println("Error occurred while waiting for input: " + 
			       e.getMessage());
	}
    }

    // handle same point constraint
    private static void handleSamePoint(Canvas canvas) {

	// capture two mouse clicks with select(), then create a constraint
	// and add to the canvas
	Point first_click = selectPoint(canvas);
	if (first_click == null) return;
	first_click.highlight();
	canvas.show();

	Point second_click = selectPoint(canvas);
	if (second_click == null) return;
	second_click.highlight();
	canvas.show();

	SamePointConstraint sp = new 
	    SamePointConstraint(first_click, second_click);
	canvas.addConstraint(sp);

	first_click.unhighlight();
	second_click.unhighlight();
    }

    // handle parallel lines constraint
    private static void handleParallel(Canvas canvas) {
	
	// capture two mouse clicks with select(), then create a constraint
	// and add to the canvas
	Shape first_click = select(canvas);
	if (first_click == null || !first_click.getClass().equals(Line.class))
	    return;
	first_click.highlight();
	canvas.show();

	Shape second_click = select(canvas);
	if (second_click == null || !second_click.getClass().equals(Line.class))
	    return;
	second_click.highlight();
	canvas.show();
	
	ParallelLineConstraint para = new
	    ParallelLineConstraint((Line) first_click, (Line) second_click);
	canvas.addConstraint(para);

	first_click.unhighlight();
	second_click.unhighlight();
    }

    // handle perpendicular lines constraint
    private static void handlePerpendicular(Canvas canvas) {

	// capture two mouse clicks with select(), then create a constraint
	// and add to the canvas
	Shape first_click = select(canvas);
	if (first_click == null || !first_click.getClass().equals(Line.class))
	    return;
	first_click.highlight();
	canvas.show();

	Shape second_click = select(canvas);
	if (second_click == null || !second_click.getClass().equals(Line.class))
	    return;
	first_click.highlight();
	canvas.show();
	
	PerpendicularLineConstraint perp = new
	    PerpendicularLineConstraint((Line) first_click, (Line) second_click);
	canvas.addConstraint(perp);

	first_click.unhighlight();
	second_click.unhighlight();
    }

    // handle same length constraint
    private static void handleSameLength(Canvas canvas) {

	// capture two mouse clicks with select(), then create a constraint
	// and add to the canvas
	Shape first_click = select(canvas);
	if (first_click == null || !first_click.getClass().equals(Line.class))
	    return;
	first_click.highlight();
	canvas.show();

	Shape second_click = select(canvas);
	if (second_click == null || !second_click.getClass().equals(Line.class))
	    return;
	second_click.highlight();
	canvas.show();
	
	SameLengthConstraint sl = new
	    SameLengthConstraint((Line) first_click, (Line) second_click);
	canvas.addConstraint(sl);

	first_click.unhighlight();
	second_click.unhighlight();
    }

    // helper method: select a primitive
    private static Shape select(Canvas canvas) {
	
	// wait for mouse click
	waitForMouse();
	
	// find closest shape within tolerance
	return canvas.findNearestShape(tolerance);
    }

    // helper method: select a point
    private static Point selectPoint(Canvas canvas) {
	
	// wait for mouse click
	waitForMouse();
	
	// find closest shape within tolerance
	return canvas.findNearestPoint(tolerance);
    }


    // helper method: wait for mouse click
    private static void waitForMouse() {
	try {
	    while (true) {
		Thread.sleep(delayMillis);
		if (StdDraw.mousePressed()) {
		    return;
		}
	    }
	} catch (Exception e) {
	    System.err.println("Error occurred while waiting for mouse: " + 
			       e.getMessage());
	}
    }

    // implement a finite-state machine to draw different primitives
    public static void main(String[] args) throws Exception {
	
	// set up canvas with buttons
	Canvas canvas = new Canvas();

	Button point = canvas.addButton(margin + halfWidth, 
					1.0 - margin - halfHeight, 
					halfWidth, halfHeight, "Point");
	Button line = canvas.addButton(-halfWidth + 2.0 * 
				       (margin + 2.0 * halfWidth), 
				       1.0 - margin - halfHeight, 
				       halfWidth, halfHeight, "Line");
	Button composite = canvas.addButton(-halfWidth + 3.0 * 
					    (margin + 2.0 * halfWidth), 
					    1.0 - margin - halfHeight, 
					    halfWidth, halfHeight, "Composite");
	Button group = canvas.addButton(-halfWidth + 4.0 * 
					(margin + 2.0 * halfWidth), 
					1.0 - margin - halfHeight, 
					halfWidth, halfHeight, "Group");

	Button same_pt = canvas.addButton(-halfWidth + 5.0 * 
					  (margin + 2.0 * halfWidth), 
					  1.0 - margin - halfHeight, 
					  halfWidth, halfHeight, "Same Point");
	Button para = canvas.addButton(-halfWidth + 6.0 * 
					   (margin + 2.0 * halfWidth), 
					   1.0 - margin - halfHeight, 
					   halfWidth, halfHeight, "Parallel");
	Button perp = canvas.addButton(-halfWidth + 7.0 * 
				       (margin + 2.0 * halfWidth), 
				       1.0 - margin - halfHeight, 
				       halfWidth, halfHeight, "Perpendicular");
	Button same_len = canvas.addButton(-halfWidth + 8.0 * 
					   (margin + 2.0 * halfWidth), 
					   1.0 - margin - halfHeight, 
					   halfWidth, halfHeight, "Same Length");

	canvas.show();

	// main loop
	while (true) {
	    waitForMouse();
	    
	    // if mouse is pressed determine which state to enter
	    Button state = canvas.whichButton();

	    // wait for user to release mouse
	    while (StdDraw.mousePressed()); 
	    
	    if (state == null) continue;
	    
	    // highlight button
	    state.highlight();
	    canvas.show();

	    // handle states
	    if (state.equals(point))
		handlePoint(canvas);
	    else if (state.equals(line))
		handleLine(canvas);
	    else if (state.equals(composite))
		handleComposite(canvas);
	    else if (state.equals(group))
		handleGroup(canvas);
	    else if (state.equals(same_pt))
		handleSamePoint(canvas);
	    else if (state.equals(para))
		handleParallel(canvas);
	    else if (state.equals(perp))
		handlePerpendicular(canvas);
	    else if (state.equals(same_len))
		handleSameLength(canvas);
	    
	    // update geometry and show
	    state.unhighlight();
	    canvas.optimizeGeometry();
	    canvas.show();
	}

    }
}