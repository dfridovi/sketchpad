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

    // initialize the two points
    public SamePointConstraint(Point operand, Point target) {
	this.operand = operand;
	this.target = target;
    }

    // move operand to target
    public void execute() {
	this.operand.moveTo(this.target);
    }
}