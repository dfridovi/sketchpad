/*************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file defines an interface for constraints upon primitives.
 ************************************************************************/

public interface Constraint {
    
    // require all classes which implement this interface to have an
    // execute() method, which alters the geometry to satisfy the constraint
    void execute();
}