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

    // comparable interface
    public int compareTo(Constraint c) {
	
	// This constraint is greater than all other constraint types
	if (!c.getClass().equals(PerpendicularLineConstraint.class)) return 1;
	PerpendicularLineConstraint s = (PerpendicularLineConstraint) c;

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
	double dx_operand = this.operand.deltaX();
	double dy_operand = this.operand.deltaY();
	double dx_target = this.target.deltaX();
	double dy_target = this.target.deltaY();
	
	double normed_inner_product = 
	    ((dx_operand * dx_target + dy_operand * dy_target) /
	     (this.operand.length() * this.operand.length() *
	      this.target.length() * this.target.length()));

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