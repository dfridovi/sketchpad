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

    // execute this constraint by changing only the operand
    public void execute() {
	this.operand.makeSameLengthAs(this.target);
    }

    // return operand, target
    public Line operand() {return this.operand;} 
    public Line target() {return this.target;} 
}