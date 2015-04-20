/*****************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file implements a mutable point object. (Mutable because a constraint
 * may later be imposed which forces it to change position.)
 ****************************************************************************/

import java.util.TreeSet;

public class Point implements Shape {
    
    private double x;
    private double y;
    private boolean highlight;

    // initialize this point
    public Point(double x, double y) {
	this.x = x;
	this.y = y;
	this.highlight = false;
    }

    // compare to another point
    public boolean equals(Point p) {
	if (this.x == p.x && this.y == p.y)
	    return true;
	else 
	    return false;		
    }

    // calculate the gradient according to a set of constraints, 
    // and move in that direction at some speed -- returns final error
    public double moveGradient(TreeSet<Constraint> constraints, double speed) {
	
	// get initial error
	double initial_error = 0.0;
	for (Constraint c : constraints)
	    initial_error += c.squaredError();

	// jitter in +x direction, and calculate error
	this.translate(0.001, 0);
	double x_error = 0.0;
	for (Constraint c : constraints)
	    x_error += c.squaredError();
	this.translate(-0.001, 0);

	// jitter in +y direction, and calculate error
	this.translate(0, 0.001);
	double y_error = 0.0;
	for (Constraint c : constraints)
	    y_error += c.squaredError();
	this.translate(0, -0.001);

	// move one step in the direction of the negative gradient (descent)
	double grad_x = x_error - initial_error;
	double grad_y = y_error - initial_error;

	this.translate(speed * -grad_x, speed * -grad_y);

	// compute final error
	double final_error = 0.0;
	for (Constraint c : constraints)
	    final_error += c.squaredError();
	return final_error;
    }

    // comparable interface
    public int compareTo(Shape s) {
	
	// Points are less than Lines and Composites
	if (!s.getClass().equals(Point.class)) return -1;
	s = (Point) s;

	// order based on distance from the origin (arbitrary)
	if (this.distTo(0, 0) < s.distTo(0, 0)) return -1;
	if (this.distTo(0, 0) > s.distTo(0, 0)) return 1;
	return 0;
    }

    // get Euclidean distance to another point
    public double distTo(Point p) {
	double deltaX = p.x - this.x;
	double deltaY = p.y - this.y;
	return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    // get Euclidean distance to particular location (not a point object)
    public double distTo(double x, double y) {
	double deltaX = x - this.x;
	double deltaY = y - this.y;
	return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    // get delta x to another point
    public double deltaX(Point p) {
	return p.x - this.x;
    }

    // get delta y to another point
    public double deltaY(Point p) {
	return p.y - this.y;
    }
    
    // get slope from this point to another point
    public double slopeTo(Point p) {
	double deltaX = p.x - this.x;
	double deltaY = p.y - this.y;
	return deltaY / deltaX;
    }

    // get polar angle to another point
    public double angleTo(Point p) {
	return Math.atan2(p.y - this.y, p.x - this.x);
    }

    // move to another location, a fixed length away from a given point
    // in a particular direction specified by a polar angle
    public void moveTo(Point p, double angle, double length) {
	this.x = p.x + length * Math.cos(angle);
	this.y = p.y + length * Math.sin(angle);
    }

    // make this point the same as another point
    public void setPoint(Point operand, Point target) {
	if (!this.equals(operand)) return;
	this.x = target.x;
	this.y = target.y;
    }

    // translate by a specified delta
    public void translate(double deltaX, double deltaY) {
	this.x += deltaX;
	this.y += deltaY;
    }

    // duplicate this point
    public Point duplicate() {
	return new Point(this.x, this.y);
    }

    // return a queue with only this point in it
    public Queue<Point> getPoints() {
	Queue<Point> iter = new Queue<Point>();
	iter.enqueue(this);
	return iter;
    }

    // draw to StdDraw
    public void draw() {
	if (this.highlight) {
	    StdDraw.setPenRadius(0.02);
	    StdDraw.setPenColor(StdDraw.ORANGE);
	    StdDraw.point(this.x, this.y);
	    StdDraw.setPenRadius(0.005);
	    StdDraw.setPenColor();
	} else {
	    StdDraw.setPenRadius(0.02);
	    StdDraw.point(this.x, this.y);
	    StdDraw.setPenRadius(0.005);
	}
    }

    // highlight orange
    public void highlight() {
	this.highlight = true;
    }

    // unhighlight
    public void unhighlight() {
	this.highlight = false;
    }

    // draw line to another point
    public void drawLineTo(Point p) {
	StdDraw.line(this.x, this.y, p.x, p.y); 
    }
}