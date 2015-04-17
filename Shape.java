/*************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file defines an interface for geometric shapes 
 * (points, lines, and composites).
 ************************************************************************/

public interface Shape {
    
    // must be able to implement these functions
    void translate(double deltaX, double deltaY);
    Shape duplicate();
    double distTo(double x, double y);
    void draw();
    void highlight();
    void unhighlight();
}