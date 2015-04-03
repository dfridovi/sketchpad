/***************************************************************************
 * Author: David Fridovich-Keil
 * Course: ELE 583
 * 
 * Final Project: This project is a minimal implementation of the SketchPad
 * demo, which was developed at MIT Lincoln Labs, by Sutherland in 1963 [1].
 * 
 * In this demo, we allow the user to specify (with the mouse) a primitive
 * shape from one of a set of buttons at the top of the screen, then define
 * it's location on the screen by left-clicking with the mouse. The user may
 * also select a set of primitives already placed on the screen, and combine
 * them into a single object (which may then be used/inserted as though it
 * were a primitive). Finally, the user may specify certain constraints on the
 * geometry (such as lines being parallel), and the system will satisfy the
 * constraints if possible, and if not possible it will output an error message
 * to that effect.
 *
 * [1] Ivan E. Sutherland. "SKETCHPAD: A MAN-MACHINE GRAPHICAL COMMUNICATION
 *     SYSTEM." Spring Joint Computer Conference, 1963.
 *
 * Dependencies: StdDraw.java
 * 
 * Other files in this project: 
 * -- point.java = a point primitive object
 * -- line.java = a line primitive object
 * -- samePoint.java = constraint object that sets two points to be identical
 * -- parallelConstraint.java = constraint object that operates on two lines
 * -- perpendicularConstraint.java = constraint object on two lines
 * -- sameLength.java = constraint object that operates on two lines
 * -- optimizeGeometry.java = a class which sifts constraints and attempts
 *                            to satisfy all constraints by adjusting geometry
 *
 * Note: Constraints are constructed to keep only references to primitives,
 * so that by executing a constraint we affect the original primitive, which
 * allows us to maintain separate lists of primitives and constraints, and
 * cycle through the constraints and execute them sequentially, trusting that
 * they will affect the correct primitives.
 ***************************************************************************/


