/*****************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file implements an object which is, in effect, a container for many
 * different primitives.
 ****************************************************************************/

public class Composite implements Shape {

    // make a queue for each supported type
    private Queue<Shape> shapes;

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

    // draw to StdDraw
    public void draw() {
	for (Shape s : this.shapes)
	    s.draw();
    }
}