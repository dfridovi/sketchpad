/*****************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file implements a parallel line constraint. It maintains
 * state that consists of two lines (the target and the operand), and
 * provides a method which will cause the operand to move to be parallel to
 * the target.
 ****************************************************************************/

public class ParallelConstraint implements Constraint {
    
    private Line operand;
    private Line target;

    // initialize operand and target
    public ParallelConstraint(Line operand, Line target) {
	this.operand = operand;
	this.target = target;
    }

    // execute this constraint by changing only the operand
    public void execute() {
	this.operand.makeParallelTo(this.target);
    }
}