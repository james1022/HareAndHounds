package com.oose2015.jchoi100.hareandhounds;

//Assignment 1
//Name: Joon Hyuck Choi
//JHED: jchoi100
//email: jchoi100@jhu.edu
//Phone: 667-239-0288
//Section: 600.421

/**
 * This class represents one piece of Hare on the Board.
 * 
 * @author Joon Hyuck Choi
 *
 */
public class Hare {
	
	/** The x coordinate of the Hare's position. */
	public Integer x;
	
	/** The y coordinate of the Hare's position. */
	public Integer y;
	
	/**
	 * Constructor for the Hare class.
	 * 
	 * @param x the column position of the Hare in the grid[][].
	 * @param y the row position of the Hare in the grid[][].
	 */
	public Hare(int x, int y) {
		this.x = x;
		this.y = y;
	}

}
