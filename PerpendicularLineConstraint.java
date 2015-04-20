/*****************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file implements a perpendicular line constraint. It maintains
 * state that consists of two lines (the target and the operand), and
 * provides a method which will cause the operand to move to be perpendicular
 * to the target.
 ****************************************************************************/

public class PerpendicularLineConstraint implements Constraint {
    
    private Line operand;
    private Line target;

    // initialize operand and target
    public PerpendicularLineConstraint(Line operand, Line target) {
	this.operand = operand;
	this.target = target;
    }

    // return squared error 
    public double squaredError() {
	double dx_operand = this.operand.deltaX();
	double dy_operand = this.operand.deltaY();
	double dx_target = this.target.deltaX();
	double dy_target = this.target.deltaY();
	
	double normed_inner_product = 
	    ((dx_operand * dx_target + dy_operand * dy_target) /
	     (this.operand.length() * this.operand.length *
	      this.target.length() * this.target.length));

	return normed_inner_product * normed_inner_product;
    }

    // return squared error
    public double squaredError(Shape a, Shape b) {

	// check type
	if (!a.getType().equals(Line.class) ||
	    !b.getType().equals(Line.class)) {
	    System.out.println("Wrong Shape. Need a Line.");
	    return Double.POSITIVE_INFINITY;
	}
	a = (Line) a;
	b = (Line) b;

	// determine which line a corresponds to

	// calculate error
	double dx_a = a.deltaX();
	double dy_a = a.deltaY();
	double dx_b = b.deltaX();
	double dy_b = b.deltaY();
	
	double normed_inner_product = 
	    ((dx_a * dx_b + dy_a * dy_b) /
	     (a.length() * a.length * b.length() * b.length));

	return normed_inner_product * normed_inner_product;
    }

    // execute this constraint by changing only the operand
    public void execute() {
	this.operand.makePerpendicularTo(this.target);
    }

    // return operand, target
    public Line operand() {return this.operand;} 
    public Line target() {return this.target;} 
}