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
import java.util.TreeSet;

public class Canvas {

    // store buttons and shapes
    private Queue<Button> buttons;
    private Queue<Shape> shapes; 

    // store constraints
    private Queue<SameLengthConstraint> sl;
    private Queue<SamePointConstraint> sp;
    private Queue<ParallelLineConstraint> para;
    private Queue<PerpendicularLineConstraint> perp;

    // keep a graph for parallel and perpendicular constraints
    private TreeMap<Line, TreeSet<Line>> parallel_lines;
    private TreeMap<Line, TreeSet<Line>> perpendicular_lines;
    
    // set up canvas
    public Canvas() {
	this.buttons = new Queue<Button>();
	this.shapes = new Queue<Shape>();

	this.sl = new Queue<SameLengthConstraint>();
	this.para = new Queue<ParallelLineConstraint>();
	this.perp = new Queue<PerpendicularLineConstraint>();

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
    public void addConstraint(SameLengthConstraint c) {this.sl.enqueue(c);}
    public void addConstraint(SamePointConstraint c) {this.sp.enqueue(c);}
    public void addConstraint(ParallelLineConstraint c) {
	this.para.enqueue(c);
	
	// extract two lines
	Line l1 = c.operand();
	Line l2 = c.target();

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
	this.perp.enqueue(c);

	// extract two lines
	Line l1 = c.operand();
	Line l2 = c.target();

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
	for (SameLengthConstraint c : this.sl)
	    c.execute();
	for (ParallelLineConstraint c : this.para)
	    c.execute();
	for (PerpendicularLineConstraint c : this.perp)
	    c.execute();
    }

    // optimize geometry -- full version
    public void optimizeGeometry() {
	if (!this.isSatisfiable()) {
	    System.out.println("ERROR: Constraints not satisfiable.");
	    System.exit(1);
	}
    }

    // check if all constraints are simultaneously satisfiable
    private boolean isSatisfiable() {
	
	// search through all perpendicular line constraints
	for (PerpendicularLineConstraint perp_line : this.perp) {

	    // search through the graph of parallel line constraints
	    // starting from these two lines and see if there is a path from one
	    // to the other


	}
	return true;
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