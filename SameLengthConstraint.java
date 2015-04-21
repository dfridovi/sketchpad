/*****************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file implements a same length line constraint. It maintains
 * state that consists of two lines (the target and the operand), and
 * provides a method which will cause the operand be the same length as
 * the target.
 ****************************************************************************/

public class SameLengthConstraint implements Constraint {
    
    private Line operand;
    private Line target;

    // initialize operand and target
    public SameLengthConstraint(Line operand, Line target) {
	this.operand = operand;
	this.target = target;
    }

    // comparable interface
    public int compareTo(Constraint c) {
	
	// This constraint is greater than Point constraints but 
	// less than other Line constraints
	if (c.getClass().equals(SamePointConstraint.class)) return 1;
	if (c.getClass().equals(ParallelLineConstraint.class)) return -1;
	if (c.getClass().equals(PerpendicularLineConstraint.class)) return -1;
	SameLengthConstraint s = (SameLengthConstraint) c;

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
	return ((this.operand.length() - this.target.length()) *
		(this.operand.length() - this.target.length()));
    }

    // execute this constraint by changing only the operand
    public void execute() {
	this.operand.makeSameLengthAs(this.target);
    }

    // return operand, target
    public Line operand() {return this.operand;} 
    public Line target() {return this.target;} 
}