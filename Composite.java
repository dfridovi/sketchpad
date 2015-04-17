/*****************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file implements an object which is, in effect, a container for many
 * different primitives.
 ****************************************************************************/

public class Composite implements Shape {

    // make a queue for each supported type
    private final Queue<Shape> shapes;
    
    // initialize instance variables
    public Composite(Queue<Shape> shapes) {
	this.shapes = shapes;
    }

    // translate all objects in this composite by a fixed delta
    public void translate(double deltaX, double deltaY) {
	for (Shape s : this.shapes)
	    s.translate(deltaX, deltaY);
    }

    // duplicate this composite
    public Composite duplicate() {
	Queue<Shape> dup_shapes = new Queue<Shape>();
	for (Shape s : this.shapes)
	    dup_shapes.enqueue(s.duplicate());

	Composite dup = new Composite(dup_shapes);
	return dup;
    }

    // return a queue of points in this composite
    public Queue<Point> getPoints() {
	Queue<Point> iter = new Queue<Point>();
	for (Shape s : this.shapes) {
	    for (Point p : s.getPoints())
		iter.enqueue(p);
	}

	return iter;
    }

    // check if any sub-shape contains the operand point and if so replace
    // with the target point
    public void setPoint(Point operand, Point target) {
	for (Shape s : this.shapes)
	    s.setPoint(operand, target);
    }

    // get distance from a point to the nearest part of this shape
    public double distTo(double x, double y) {
	double min_dist = Double.POSITIVE_INFINITY;
	
	for (Shape s : this.shapes) {
	    double dist = s.distTo(x, y);
	    
	    if (dist < min_dist)
		min_dist = dist;
	}

	return min_dist;
    }

    // draw to StdDraw
    public void draw() {
	for (Shape s : this.shapes)
	    s.draw();
    }

    // highlight orange
    public void highlight() {
	for (Shape s : this.shapes)
	    s.highlight();
    }

    // unhighlight
    public void unhighlight() {
	for (Shape s : this.shapes)
	    s.unhighlight();
    }
}