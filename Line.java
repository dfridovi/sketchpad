/*****************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file implements a mutable line object. (Mutable because a constraint
 * may later be imposed which forces it to change position.)
 ****************************************************************************/

public class Line implements Shape, Comparable<Line> {
    
    private Point p;
    private Point q;
    private boolean highlight;

    // initialize this line with two endpoints
    public Line(Point p, Point q) {
	this.p = p;
	this.q = q;
	this.highlight = false;
    }

    // make parallel to another line, while maintaining constant length
    // and keeping the point p fixed
    public void makeParallelTo(Line that) {
	double angle = that.p.angleTo(that.q);
	double length = this.p.distTo(this.q);
	
	this.q.moveTo(this.p, angle, length);
    }

    // make perpendicular to another line, while maintaining constant length
    // and keeping the point p fixed. do this by rotating by positive pi/2
    public void makePerpendicularTo(Line that) {
	double angle = that.p.angleTo(that.q);
	double length = this.p.distTo(this.q);
	
	this.q.moveTo(this.p, angle + Math.PI / 2.0, length);
    }

    // make same length as another line, while maintaining the current angle
    // and keeping the point p fixed
    public void makeSameLengthAs(Line that) {
	double angle = this.p.angleTo(this.q);
	double length = that.p.distTo(that.q);
	
	this.q.moveTo(this.p, angle, length);
    }

    // translate by a specified delta
    public void translate(double deltaX, double deltaY) {
	this.p.translate(deltaX, deltaY);
	this.q.translate(deltaX, deltaY);
    }

    // comparable interface
    public int compareTo(Line other) {
	
	// order by magnitude (arbitrary)
	double this_len = this.p.distTo(this.q);
	double other_len = other.p.distTo(other.q);

	if (this_len < other_len) return -1;
	if (this_len == other_len) return 0;
	return 1;
    }

    // check if this is the same as another line
    public boolean equals(Line other) {
    	if (this.p.equals(other.p) && this.q.equals(other.q)) return true;
    	if (this.q.equals(other.p) && this.p.equals(other.q)) return true;
	return false;
    }

    // duplicate this line
    public Line duplicate() {
	return new Line(this.p.duplicate(), this.q.duplicate());
    }

    // get Euclidean distance between this line and a point
    public double distTo(double x, double y) {

	// get unit vector in direction of this line
	double unitX = Math.cos(this.p.angleTo(this.q));
	double unitY = Math.sin(this.p.angleTo(this.q));

	// get vector from p to this point
	Point pt = new Point(x, y);
	double pX = this.p.deltaX(pt);
	double pY = this.p.deltaY(pt);

	// test if pt projects onto the line or not
	double dot_product = unitX * pX + unitY * pY;

	if ((dot_product <= this.p.distTo(this.q)) &&
	    (dot_product >= 0.0)) {
	    
	    // projects onto the line
	    return Math.sqrt(this.p.distTo(pt) * this.p.distTo(pt) - 
			     dot_product * dot_product);
	} else {
	    
	    // doesn't project onto the line
	    double distP = this.p.distTo(pt);
	    double distQ = this.q.distTo(pt);
	    
	    return Math.min(distP, distQ);
	}
    }

    // check if this line contains a point and if so, replace with target
    public void setPoint(Point operand, Point target) {
	if (this.p.equals(operand)) this.p = target;
	if (this.q.equals(operand)) this.q = target;
    }

    // return a queue with both points in it
    public Queue<Point> getPoints() {
	Queue<Point> iter = new Queue<Point>();
	iter.enqueue(this.p);
	iter.enqueue(this.q);
	return iter;
    }

    // draw to StdDraw
    public void draw() {
	if (this.highlight)
	    StdDraw.setPenColor(StdDraw.ORANGE);
	
	this.p.draw();
	this.q.draw();
	this.p.drawLineTo(this.q);
	
	if (this.highlight)
	    StdDraw.setPenColor();
    }

    // highlight orange
    public void highlight() {
	this.highlight = true;
    }

    // highlight orange
    public void unhighlight() {
	this.highlight = false;
    }
}