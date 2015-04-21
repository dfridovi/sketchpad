/*************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file defines an interface for constraints upon primitives.
 ************************************************************************/

public interface Constraint extends Comparable<Constraint> {
    
    // required methods
    void execute();
    double squaredError();
    Shape operand();
    Shape target();
    int compareTo(Constraint c);
}