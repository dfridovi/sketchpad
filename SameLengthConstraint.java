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

    // return squared error 
    public double squaredError() {
	return ((this.operand.length() - this.target.length()) *
		(this.operand.length() - this.target.length()));
    }

    // return squared error
    public double squaredError(Shape a, Shape b) {

	// check type
	if (!a.getType().equals(Line.class) ||
	    !a.getType().equals(Line.class)) {
	    System.out.println("Wrong Shape. Need a Line.");
	    return Double.POSITIVE_INFINITY;
	}
	a = (Line) a;
	b = (Line) b;

	// calculate error
	return ((a.length() - b.length()) *
		(a.length() - b.length()));
    }

    // execute this constraint by changing only the operand
    public void execute() {
	this.operand.makeSameLengthAs(this.target);
    }

    // return operand, target
    public Line operand() {return this.operand;} 
    public Line target() {return this.target;} 
}