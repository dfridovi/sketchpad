/*****************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file implements a parallel line constraint. It maintains
 * state that consists of two lines (the target and the operand), and
 * provides a method which will cause the operand to move to be parallel to
 * the target.
 ****************************************************************************/

public class ParallelLineConstraint implements Constraint {
    
    public Line operand;
    public Line target;

    // initialize operand and target
    public ParallelLineConstraint(Line operand, Line target) {
	this.operand = operand;
	this.target = target;
    }

    // comparable interface
    public int compareTo(Constraint c) {
	
	// This constraint is greater than Point constraints and SameLength but 
	// less than Perpendicular constraints
	if (c.getClass().equals(SamePointConstraint.class)) return 1;
	if (c.getClass().equals(SameLengthConstraint.class)) return 1;
	if (c.getClass().equals(PerpendicularLineConstraint.class)) return -1;
	ParallelLineConstraint s = (ParallelLineConstraint) c;

	// order based on total magnitude (arbitrary)
	if (this.operand.length() + this.target.length() 
	    < s.operand.length() + s.target.length()) 
	    return -1;
	if (this.operand.length() + this.target.length() 
	    > s.operand.length() + s.target.length()) 
	    return -1;

	return 0;
    }

    // return squared error 
    public double squaredError() {
	return ((this.operand.slope() - this.target.slope()) *
		(this.operand.slope() - this.target.slope()));
    }

    // execute this constraint by changing only the operand
    public void execute() {
	this.operand.makeParallelTo(this.target);
    }

    // return operand, target
    public Line operand() {return this.operand;} 
    public Line target() {return this.target;} 
}