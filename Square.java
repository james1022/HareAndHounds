package com.oose2015.jchoi100.hareandhounds;

//Assignment 1
//Name: Joon Hyuck Choi
//JHED: jchoi100
//email: jchoi100@jhu.edu
//Phone: 667-239-0288
//Section: 600.421

/**
 * One square fills in one spot in the Board's grid[][].
 * It represents one 'spot' or 'node' where each piece can sit on.
 * It gives us information about its own position,
 * whether or not there is a hare or a hound or nothing on top,
 * and its own unique square "serial number".
 * That serial number is for the programmer's convenience.
 * 
 * @author Joon Hyuck Choi
 *
 */
public class Square {

	/** Whether or not any piece can go on top of it. */
	public boolean isFree;
	
	/** The x coordinate of the Square. */
	public int x;
	
	/** The y coordinate of the Square. */
	public int y;
	
	/** Unique serial number given to each square. */
	public int squareNum;
	
	/** Whether or not a hound is sitting on top of the Square. */
	public boolean houndOnTop;
	
	/** Whether or not a hare is sitting on top of the Square. */
	public boolean hareOnTop;
	
	/**
	 * Constructor for the Square class.
	 * 
	 * @param sqNum unique serial number.
	 * @param isFree whether or not a piece can go on top.
	 * @param x column number for 'Square[][] grid' in 'Board' class.
	 * @param y row number for the 'Square[][] grid' in 'Board' class.
	 */
	public Square(int sqNum, boolean isFree, int x, int y) {
		this.squareNum = sqNum;
		this.isFree = isFree;
		this.x = x;
		this.y = y;
		this.houndOnTop = false;
		this.hareOnTop = false;
	}
	
}
