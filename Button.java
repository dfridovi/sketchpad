/*****************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 *
 * This file provides a Button object, which is simply a rectangle
 * located at a particular spot, with the specified dimensions and text.
 * We also include a link to a child button, for use in a drop-down
 * menu structure.
 ****************************************************************************/

public class Button {

    // constant
    private static final double margin = 0.05;
    
    // instance variables
    private Button child;
    private final double center_x;
    private final double center_y;
    private final double half_width;
    private final double half_height;
    public final String name;

    // create a new button
    public Button(double center_x, double center_y, 
		  double half_width, double half_height, String name) {
	this.center_x = center_x;
	this.center_y = center_y;
	this.half_width = half_width;
	this.half_height = half_height;
	this.name = name;
    }

    // create a new child button
    public Button procreate(String name) {
	this.child = new Button(this.center_x, 
				this.center_y - 2 * this.half_height - margin, 
				this.half_width, this.half_height, name);
	return this.child;
    }

    // draw to StdDraw
    public void draw() {
	StdDraw.rectangle(this.center_x, this.center_y, 
			  this.half_width, this.half_height);
	StdDraw.text(this.center_x, this.center_y, this.name);
    }

    // check if mouse is currently clicking this button or any of its children
    public boolean isPressed() {
	double min_x = this.center_x - this.half_width;
	double max_x = this.center_x + this.half_width;
	double min_y = this.center_y - this.half_height;
	double max_y = this.center_y + this.half_height;

	if (StdDraw.mousePressed() && 
	    (StdDraw.mouseX() >= min_x) && (StdDraw.mouseX() <= max_x) &&
	    (StdDraw.mouseY() >= min_y) && (StdDraw.mouseY() <= max_y))
	    return true;
	else
	    return false;
    }
}