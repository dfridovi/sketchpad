/*****************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file implements an abstraction for the SKETCHPAD canvas. It uses
 * the StdDraw library for all graphics and mouse monitoring, but it also
 * is designed to interact with Button objects and create drop-down menus 
 * (for composite selection).
 ****************************************************************************/

import java.util.TreeMap;
import java.util.HashMap;
import java.util.TreeSet;

public class Canvas {

    // store buttons and shapes
    private Queue<Button> buttons;
    private Queue<Shape> shapes; 

    // store constraints
    private Queue<SamePointConstraint> sp;
    private Queue<Constraint> line_constraints;
    public HashMap<Shape, TreeSet<Constraint>> constraint_map;

    // keep a graph for parallel and perpendicular constraints
    private TreeMap<Line, TreeSet<Line>> parallel_lines;
    private TreeMap<Line, TreeSet<Line>> perpendicular_lines;

    // constants for gradient descent optimization
    private final double tolerance = 0.001;
    private final double delta_tolerance = 0.00005;
    private final double speed = 1.0;

    // set up canvas
    public Canvas() {
	this.buttons = new Queue<Button>();
	this.shapes = new Queue<Shape>();

	this.sp = new Queue<SamePointConstraint>();
	this.line_constraints = new Queue<Constraint>();
	this.constraint_map = new HashMap<Shape, TreeSet<Constraint>>();

	this.parallel_lines = new TreeMap<Line, TreeSet<Line>>();
	this.perpendicular_lines = new TreeMap<Line, TreeSet<Line>>();

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

    // add a constraint -- overloaded methods
    public void addConstraint(SamePointConstraint c) {
	this.sp.enqueue(c);
	System.out.println("Entered valid SamePointConstraint.");
    }
    public void addConstraint(SameLengthConstraint c) {
	this.line_constraints.enqueue(c);
	System.out.println("Entered valid SameLengthConstraint.");

	// extract two lines
	Line l1 = c.operand();
	Line l2 = c.target();

	// add to constraint map
	if (this.constraint_map.containsKey(l1))
	    this.constraint_map.get(l1).add(c);
	else {
	    TreeSet<Constraint> set = new TreeSet<Constraint>();
	    set.add(c);
	    this.constraint_map.put(l1, set);
	}
	if (this.constraint_map.containsKey(l2))
	    this.constraint_map.get(l2).add(c);
	else {
	    TreeSet<Constraint> set = new TreeSet<Constraint>();
	    set.add(c);
	    this.constraint_map.put(l2, set);
	}
    }
    public void addConstraint(ParallelLineConstraint c) {
	
	// test if satisfiable
	if (!this.isSatisfiable(c)) {;
	    System.out.println("ERROR: Constraints not satisfiable.");
	    return;
	}

	// add to queue
	this.line_constraints.enqueue(c);
	System.out.println("Entered valid ParallelLineConstraint.");	

	// extract two lines
	Line l1 = c.operand();
	Line l2 = c.target();

	// add to constraint map
	if (this.constraint_map.containsKey(l1))
	    this.constraint_map.get(l1).add(c);
	else {
	    TreeSet<Constraint> set = new TreeSet<Constraint>();
	    set.add(c);
	    this.constraint_map.put(l1, set);
	}
	if (this.constraint_map.containsKey(l2))
	    this.constraint_map.get(l2).add(c);
	else {
	    TreeSet<Constraint> set = new TreeSet<Constraint>();
	    set.add(c);
	    this.constraint_map.put(l2, set);
	}

	// update parallel line graph
	if (this.parallel_lines.containsKey(l1))
	    this.parallel_lines.get(l1).add(l2);
	else {
	    TreeSet<Line> set = new TreeSet<Line>();
	    set.add(l2);
	    this.parallel_lines.put(l1, set);
	}
	if (this.parallel_lines.containsKey(l2))
	    this.parallel_lines.get(l2).add(l1);
	else {
	    TreeSet<Line> set = new TreeSet<Line>();
	    set.add(l1);
	    this.parallel_lines.put(l2, set);
	}
    }

    public void addConstraint(PerpendicularLineConstraint c) {

	// test if satisfiable
	if (!this.isSatisfiable(c)) {
	    System.out.println("ERROR: Constraints not satisfiable.");
	    return;
	}

	// add to queue
	this.line_constraints.enqueue(c);
	System.out.println("Entered valid PerpendicularLineConstraint.");	

	// extract two lines
	Line l1 = c.operand();
	Line l2 = c.target();

	// add to constraint map
	if (this.constraint_map.containsKey(l1))
	    this.constraint_map.get(l1).add(c);
	else {
	    TreeSet<Constraint> set = new TreeSet<Constraint>();
	    set.add(c);
	    this.constraint_map.put(l1, set);
	}
	if (this.constraint_map.containsKey(l2))
	    this.constraint_map.get(l2).add(c);
	else {
	    TreeSet<Constraint> set = new TreeSet<Constraint>();
	    set.add(c);
	    this.constraint_map.put(l2, set);
	}

	// update perpendicular line graph
	if (this.perpendicular_lines.containsKey(l1))
	    this.perpendicular_lines.get(l1).add(l2);
	else {
	    TreeSet<Line> set = new TreeSet<Line>();
	    set.add(l2);
	    this.perpendicular_lines.put(l1, set);
	}
	if (this.perpendicular_lines.containsKey(l2))
	    this.perpendicular_lines.get(l2).add(l1);
	else {
	    TreeSet<Line> set = new TreeSet<Line>();
	    set.add(l1);
	    this.perpendicular_lines.put(l2, set);
	}
    }

    // add a button
    public Button addButton(double center_x, double center_y, 
			    double half_width, double half_height, String name) {
	Button b = new Button(center_x, center_y, 
			      half_width, half_height, name);
	this.buttons.enqueue(b);
	return b;
    }

    // return whichever button is being pressed, or null if
    // no button is currently being pressed
    public Button whichButton() {
	for (Button b : this.buttons) {
	    if (b.isPressed())
		return b;
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

    // find nearest point to mouse, within specified tolerance
    public Point findNearestPoint(double tolerance) {
	double x = StdDraw.mouseX();
	double y = StdDraw.mouseY();
	double min_dist = Double.POSITIVE_INFINITY;
        Point nearest = null;

	for (Shape s : this.shapes) {
	    for (Point p : s.getPoints()) {
		
		double dist = p.distTo(x, y);
	    
		if (dist < min_dist) {
		    min_dist = dist;
		    nearest = p;
		}
	    }
	}

	if (min_dist <= tolerance)
	    return nearest;
	else 
	    return null;
    }

    // search all shapes and call setPoint()
    public void replacePoints(Point operand, Point target) {
	for (Shape s : this.shapes)
	    s.setPoint(operand, target);
    }

    // optimize geometry -- simple version, no error checking or 
    // operation reordering
    public void optimizeGeometrySimple() {
	for (SamePointConstraint c : this.sp)
	    c.execute();
	for (Constraint c : this.line_constraints)
	    c.execute();
    }

    // get error in constraints
    private double getError() {
	double error = 0.0;
	for (Constraint c : this.line_constraints)
	    error += c.squaredError();
	//	for (Constraint c : this.sp)
	//  error += c.squaredError();
	return error;
    }

    // optimize geometry -- full version
    public void optimizeGeometry() {

	// handle same point constraints first
	for (SamePointConstraint c : this.sp)
	    c.execute();

	// loop until error is below threshold
	int iter = 0;
	double error = getError();
	double delta_error = error;
	while (error > this.tolerance && 
	       delta_error > this.delta_tolerance &&
	       iter < 250) {
	    System.out.printf("Iteration %3d: %8.6f\n", iter, error);

	    for (Shape s : this.shapes) {
		if (this.constraint_map.containsKey(s))
		    s.moveGradient(this.constraint_map.get(s), this.speed);
	    }

	    double new_error = getError();
	    delta_error = Math.abs(new_error - error);
	    error = new_error;
	    iter++;
	    this.show();
	}

	System.out.println("Optimized geometry. Final error: " + error);   
    }

    // check if all constraints are simultaneously satisfiable
    private boolean isSatisfiable(ParallelLineConstraint c) {
	
	// extract two lines
	Line l1 = c.operand();
	Line l2 = c.target();

	// return true if perpendicular graph doesn't contain either line
	if (!this.perpendicular_lines.containsKey(l1) ||
	    !this.perpendicular_lines.containsKey(l2))
	    return true;

	// assume satisfiable up to this point
	// ensure that there are an even number of edges between l1 and l2
	// on the perpendicular line graph
	int perp_length = this.perpPathLength(l1, l2, 0, new TreeSet<Line>());
	if (perp_length < 0) return true;
	if (perp_length % 2 == 1)
	    return false;

	return true;
    }

    // check if all constraints are simultaneously satisfiable
    private boolean isSatisfiable(PerpendicularLineConstraint c) {
	
	// extract two lines
	Line l1 = c.operand();
	Line l2 = c.target();

	// return true if both graphs don't contain either line
	if ((!this.perpendicular_lines.containsKey(l1) ||
	     !this.perpendicular_lines.containsKey(l2)) &&
	    (!this.parallel_lines.containsKey(l1) ||
	     !this.parallel_lines.containsKey(l2)))
	    return true;

	// if either line is missing from only parallel graph, then check
	// just perpendicular graph
	if (!this.parallel_lines.containsKey(l1) ||
	    !this.parallel_lines.containsKey(l2)) {
	    int perp_length = this.perpPathLength(l1, l2, 0, new TreeSet<Line>());
	    if (perp_length % 2 == 1) return true;
	    return false;
	}

	// if either line is missing from only perpendicular graph, then check
	// just parallel graph
	if (!this.perpendicular_lines.containsKey(l1) ||
	    !this.perpendicular_lines.containsKey(l2)) {
	    int para_length = this.paraPathLength(l1, l2, 0, new TreeSet<Line>());
	    if (para_length < 0) return true;
	    return false;
	}
	    
	// else, got to check both graphs
	// assume satisfiable up to this point
	// ensure that there are an odd number of edges between l1 and l2
	// on the perpendicular line graph, and that there is no path between
	// the two lines on the parallel line graph
	int perp_length = this.perpPathLength(l1, l2, 0, new TreeSet<Line>());
	int para_length = this.paraPathLength(l1, l2, 0, new TreeSet<Line>());

	if (perp_length < 0 && para_length < 0) return true;
	if (para_length >= 0) return false;
	if (perp_length % 2 == 1) return true;

	return false;
    }

    // helper method: depth first search to get path length to line 
    // in perpendicular line graph
    private int perpPathLength(Line start, Line goal, 
			       int len, TreeSet<Line> marked) {

	// base case
	if (start.equals(goal)) return len;

	// mark this line
	marked.add(start);

  	// recursive step for all non-marked lines
	for (Line l : this.perpendicular_lines.get(start)) {
	    if (!marked.contains(l))
		return perpPathLength(l, goal, ++len, marked);
	}

	// if gets here, then there is no connection
	return -1;
    }

    // helper method: depth first search to get path length to line 
    // in parallel line graph
    private int paraPathLength(Line start, Line goal, 
			       int len, TreeSet<Line> marked) {

	// base case
	if (start.equals(goal)) return len;

	// mark this line
	marked.add(start);

	// recursive step for all non-marked lines
	for (Line l : this.parallel_lines.get(start)) {
	    if (!marked.contains(l))
		return paraPathLength(l, goal, ++len, marked);
	}

	// if gets here, then there is no connection
	return -1;
    }

    // draw current state
    public void show() {
	StdDraw.clear();
	for (Button b : this.buttons)
	    b.draw();
	for (Shape s : this.shapes)
	    s.draw();
	StdDraw.show(5);
    }
}