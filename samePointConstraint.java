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