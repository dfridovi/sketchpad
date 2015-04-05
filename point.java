/*****************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file implements a mutable point object. (Mutable, because a constraint
 * may later be imposed which forces it to change position.)
 ****************************************************************************/

public class Point implements Shape {
    
    private double x;
    private double y;

    // initialize this point
    public Point(double x, double y) {
	this.x = x;
	this.y = y;
    }

    // compare to another point
    public boolean equals(Point p) {
	if (this.x == p.x && this.y == p.y)
	    return true;
	else 
	    return false;		
    }

    // get Euclidean distance to another point
    public double distTo(Point p) {
	double deltaX = p.x - this.x;
	double deltaY = p.y - this.y;
	return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    // get slope from this point to another point
    public double slopeTo(Point p) {
	double deltaX = p.x - this.x;
	double deltaY = p.y - this.y;
	return deltaY / deltaX;
    }

    // get polar angle to another point
    public double angleTo(Point p) {
	return Math.atan2(this.slopeTo(p));
    }

    // move to another location, a fixed length away from a given point
    // in a particular direction specified by a polar angle
    public void moveTo(Point p, double angle, double length) {
	this.x = p.x + length * Math.cos(angle);
	this.y = p.y + length * Math.sin(angle);
    }

    // translate by a specified delta
    public void translate(double deltaX, double deltaY) {
	this.x += deltaX;
	this.y += deltaY;
    }

    // duplicate this point
    public Point duplicate() {
	return new Point(this.x, this.y);
    }

    // draw to StdDraw
    public void draw() {
	StdDraw.point(this.x, this.y);
    }
}