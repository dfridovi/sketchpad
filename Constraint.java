/*************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file defines an interface for constraints upon primitives.
 ************************************************************************/

public interface Constraint {
    
    // required methods
    void execute();
    double squaredError();
    double squaredError(Shape a, Shape b);
    Shape operand();
    Shape target();
}