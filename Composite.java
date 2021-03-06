/*****************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file implements an object which is, in effect, a container for many
 * different primitives.
 ****************************************************************************/

import java.util.Map;
import java.util.TreeSet;
import java.util.TreeMap;

public class Composite implements Shape {

    // make a queue for each supported type
    private final Queue<Shape> shapes;
    private final Queue<Constraint> constraints;
    private Canvas canvas;
    
    // initialize instance variables
    public Composite(Queue<Shape> shapes, Canvas canvas) {
	this.shapes = new Queue<Shape>();
	this.canvas = canvas;
	this.constraints = new Queue<Constraint>();

	// for each shape, check canvas for associated constraints
	TreeSet<Constraint> marked_constraints = new TreeSet<Constraint>(); 
	TreeSet<Shape> marked_shapes = new TreeSet<Shape>(); 
	for (Shape s1 : shapes) {

	    // check if we've seen this shape before
	    if (marked_shapes.contains(s1)) continue;
	    marked_shapes.add(s1);
	    this.shapes.enqueue(s1);

	    if (this.canvas.constraint_map.containsKey(s1)) {
		for (Constraint c : this.canvas.constraint_map.get(s1)) {
		    
		    // skip if we've seen this constraint before
		    if (marked_constraints.contains(c)) continue;

		    // only add to this.constraints if both 
		    // target/operand in this.shapes
		    for (Shape s2 : shapes) {
			if (s2.compareTo(s1) == 0) continue;
			if (c.operand().compareTo(s2) == 0) {
			    this.constraints.enqueue(c);
			    marked_constraints.add(c);
			}
			else if (c.target().compareTo(s2) == 0) {
			    this.constraints.enqueue(c);
			    marked_constraints.add(c);
			}
		    }
		}
	    }
	}
	 
	System.out.printf("Copied %d shapes and %d constraints.\n", 
			  this.shapes.size(),
			  this.constraints.size());
    }

    // overloaded constructor -- called from this.duplicate()
    public Composite(Queue<Shape> shapes, Queue<Constraint> constraints, 
		     Canvas canvas) {
	this.shapes = shapes;
	this.canvas = canvas;
	this.constraints = constraints;

	// add shapes to canvas
	for (Shape s : this.shapes)
	    this.canvas.addShape(s);

	// add constraints to canvas
	for (Constraint c : this.constraints) {
	    //	    if (c.getClass().equals(SamePointConstraint.class))
	    //		this.canvas.addConstraint((SamePointConstraint) c);
	    if (c.getClass().equals(SameLengthConstraint.class))
		this.canvas.addConstraint((SameLengthConstraint) c);
	    if (c.getClass().equals(ParallelLineConstraint.class))
		this.canvas.addConstraint((ParallelLineConstraint) c);
	    if (c.getClass().equals(PerpendicularLineConstraint.class))
		this.canvas.addConstraint((PerpendicularLineConstraint) c);
	}

	// check for same points and add constraints
	Queue<Point> all_points = new Queue<Point>();
	TreeMap<Point, TreeSet<Point>> unique_points = 
	    new TreeMap<Point, TreeSet<Point>>();
	for (Shape s : this.shapes) {
	    for (Point p : s.getPoints())
		all_points.enqueue(p);
	}

	for (Point p : all_points) {
	    if (!unique_points.containsKey(p)) 
		unique_points.put(p, new TreeSet<Point>());
	    else
		unique_points.get(p).add(p); // looks weird, I know...
	}

	for (Map.Entry<Point, TreeSet<Point>> entry : unique_points.entrySet()) {
	    Point p1 = entry.getKey();
	    
	    for (Point p2 : entry.getValue()) {
		SamePointConstraint c = new SamePointConstraint(p2, p1, this.canvas);
		this.canvas.addConstraint(c);
	    }
	}

	System.out.printf("Copied %d shapes and %d constraints.\n", 
			  this.shapes.size(),
			  this.constraints.size());
    }

    // calculate the gradient according to a set of constraints, 
    // and move in that direction at some speed -- returns final error
    public double moveGradient(TreeSet<Constraint> constraints, double speed) {
	
	// just call moveGradient on each sub-shape and return final result
	double final_error = 0.0;
	for (Shape s : this.shapes)
	    final_error = s.moveGradient(constraints, speed);

	return final_error;
    }

    // comparable interface
    public int compareTo(Shape s) {
	
	// Composites are bigger than other shapes
	if (!s.getClass().equals(Composite.class)) return 1;
	Composite c = (Composite) s;

	// order by number of shapes
	if (this.shapes.size() < c.shapes.size()) return -1;
	if (this.shapes.size() == c.shapes.size()) return 0;
	return 1;
    }

    // translate all objects in this composite by a fixed delta
    public void translate(double deltaX, double deltaY) {
	for (Shape s : this.shapes)
	    s.translate(deltaX, deltaY);
    }

    // duplicate this composite
    public Composite duplicate() {
	Queue<Shape> dup_shapes = new Queue<Shape>();
	Queue<Constraint> dup_constraints = new Queue<Constraint>();
	TreeMap<Shape, Shape> shape_map = new TreeMap<Shape, Shape>();

	// duplicate all shapes and add to shape_map
	for (Shape s : this.shapes) {
	    if (shape_map.containsKey(s)) 
		continue;
	    Shape copy = s.duplicate();
	    dup_shapes.enqueue(copy);
	    shape_map.put(s, copy);
	}

	// make a duplicate set of this.shapes for quick searching
	TreeSet<Shape> ts = new TreeSet<Shape>();
	for (Shape s : this.shapes)
	    ts.add(s);

	// look through all constraints and duplicate with copied shapes
	for (Constraint c : this.constraints) {
	    Shape s1 = c.operand();
	    Shape s2 = c.target();
	    Constraint copy = null;
	 
	    // ensure that this.shapes contains s1 and s2
	    if (!(ts.contains(s1) && ts.contains(s2))) {
 		System.err.println("ERROR: improper constraints.");
		System.exit(1);
	    }

	    /*
	    if (c.getClass().equals(SamePointConstraint.class))
		copy = new SamePointConstraint((Point) shape_map.get(s1), 
					       (Point) shape_map.get(s2),
					       this.canvas);
	    */
	    if (c.getClass().equals(SameLengthConstraint.class))
		copy = new SameLengthConstraint((Line) shape_map.get(s1), 
						(Line) shape_map.get(s2));
	    else if (c.getClass().equals(ParallelLineConstraint.class))
		copy = new ParallelLineConstraint((Line) shape_map.get(s1), 
						  (Line) shape_map.get(s2));
	    else if (c.getClass().equals(PerpendicularLineConstraint.class))
		copy = new PerpendicularLineConstraint((Line) shape_map.get(s1), 
						       (Line) shape_map.get(s2));

	    dup_constraints.enqueue(copy);
	}

	Composite dup = new Composite(dup_shapes, dup_constraints, this.canvas);
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