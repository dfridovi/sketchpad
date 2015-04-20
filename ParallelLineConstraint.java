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

    // return squared error 
    public double squaredError() {
	return ((this.operand.slope() - this.target.slope()) *
		(this.operand.slope() - this.target.slope()));
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
	return ((a.slope() - b.slope()) *
		(a.slope() - b.slope()));
    }

    // execute this constraint by changing only the operand
    public void execute() {
	this.operand.makeParallelTo(this.target);
    }

    // return operand, target
    public Line operand() {return this.operand;} 
    public Line target() {return this.target;} 
}