/*************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file defines an interface for constraints upon primitives.
 ************************************************************************/

public interface Constraint {
    
    // required methods
    void execute();
    Shape operand();
    Shape target();
}