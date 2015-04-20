/*************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file defines an interface for geometric shapes 
 * (points, lines, and composites).
 ************************************************************************/

import java.util.TreeSet;

public interface Shape extends Comparable<Shape> {
    
    // must be able to implement these functions
    void translate(double deltaX, double deltaY);
    Shape duplicate();
    Queue<Point> getPoints();
    void setPoint(Point operand, Point target);
    int compareTo(Shape s);
    double distTo(double x, double y);
    double moveGradient(TreeSet<Constraint> constraints, double speed);
    void draw();
    void highlight();
    void unhighlight();
}