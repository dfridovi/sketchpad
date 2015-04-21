/*****************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file implements a same point constraint. That is, it maintains
 * state that consists of two points (the target and the operand), and
 * provides a method which will cause the operand to move toward the target.
 ****************************************************************************/

public class SamePointConstraint implements Constraint {
    
    private Point operand;
    private Point target;
    private Canvas canvas;

    // initialize the two points
    public SamePointConstraint(Point operand, Point target, Canvas canvas) {
	this.operand = operand;
	this.target = target;
	this.canvas = canvas;
    }

    // comparable interface
    public int compareTo(Constraint c) {
	
	// Points constraints are less than other constraints
	if (!c.getClass().equals(SamePointConstraint.class)) return -1;
	SamePointConstraint s = (SamePointConstraint) c;

	// order based on total distance from the origin (arbitrary)
	if (this.operand.distTo(0, 0) + this.target.distTo(0, 0) 
	    < s.operand.distTo(0, 0) + s.target.distTo(0, 0)) 
	    return -1;
	if (this.operand.distTo(0, 0) + this.target.distTo(0, 0) 
	    > s.operand.distTo(0, 0) + s.target.distTo(0, 0))
	    return 1;

	return 0;
    }

    // return squared error (this one should never be called)
    public double squaredError() {
	return (this.operand.distTo(this.target) *
		this.operand.distTo(this.target));
    }

    // search through every shape on the canvas and replace instances
    // of the operand point with references to the target point
    public void execute() {
	this.canvas.replacePoints(this.operand, this.target);
    }

    // return operand, target
    public Point operand() {return this.operand;} 
    public Point target() {return this.target;} 
}