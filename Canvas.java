/*****************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file implements an abstraction for the SKETCHPAD canvas. It uses
 * the StdDraw library for all graphics and mouse monitoring, but it also
 * is designed to interact with Button objects and create drop-down menus 
 * (for composite selection).
 ****************************************************************************/

public class Canvas {

    private Queue<Button> buttons;
    private Queue<Shape> shapes; 
    private Queue<Constraint> constraints;

    // set up canvas
    public Canvas() {
	this.buttons = new Queue<Button>();
	this.shapes = new Queue<Shape>();
	this.constraints = new Queue<Constraint>();

	StdDraw.setCanvasSize(1200, 600);
	StdDraw.setXscale(0.0, 2.0);
	StdDraw.setYscale(0.0, 1.0);

	StdDraw.setPenRadius(0.005);
	this.show();
    }

    // add a shape
    public void addShape(Shape s) {
	this.shapes.enqueue(s);
    }

    // add a constraint
    public void addConstraint(Constraint c) {
	this.constraints.enqueue(c);
    }

    // add a button
    public void addButton(double center_x, double center_y, 
			  double half_width, double half_height, String name) {
	this.buttons.enqueue(new Button(center_x, center_y, 
					half_width, half_height, name));
    }

    // return the name of whichever button is being pressed, or null if
    // no button is currently being pressed
    public String whichButton() {
	for (Button b : this.buttons) {
	    if (b.isPressed())
		return b.name;
	}
	return null;
    }

    // find nearest shape to mouse, within specified tolerance
    public Shape findNearestShape(double tolerance) {
	double x = StdDraw.mouseX();
	double y = StdDraw.mouseY();
	double min_dist = Double.POSITIVE_INFINITY;
        Shape nearest = null;

	for (Shape s : this.shapes) {
	    double dist = s.distTo(x, y);
	    
	    if (dist < min_dist) {
		min_dist = dist;
		nearest = s;
	    }
	}

	if (min_dist <= tolerance)
	    return nearest;
	else 
	    return null;
    }

    // optimize geometry
    public void optimizeGeometry() {
	for (Constraint c : this.constraints)
	    c.execute();
    }

    // draw current state
    public void show() {
	StdDraw.clear();
	for (Button b : this.buttons)
	    b.draw();
	for (Shape s : this.shapes)
	    s.draw();
	StdDraw.show();
    }
}