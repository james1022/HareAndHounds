package com.oose2015.jchoi100.hareandhounds;

//Assignment 1
//Name: Joon Hyuck Choi
//JHED: jchoi100
//email: jchoi100@jhu.edu
//Phone: 667-239-0288
//Section: 600.421
/**
 * Class HoundPlayer has one array of Hounds.
 * The Game class has one HoundPlayer.
 * 
 * @author Joon Hyuck Choi
 *
 */
public class HoundPlayer extends Player {

	/** Array of 3 Hounds. */
	public Hound[] hounds;

	/**
	 * Constructor for the Player class.
	 * 
	 * @param id player's gameID.
	 * @param piece the piece type of the player.
	 */
	public HoundPlayer(String pieceType) {
		super(pieceType);
		this.pieceType = pieceType;
		this.isHound = true;
		this.isHare = false;
		this.hounds = new Hound[3];
		this.hounds[0] = new Hound(1, 0);
		this.hounds[1] = new Hound(0, 1);
		this.hounds[2] = new Hound(1, 2);
	}
	
	public Hound getHoundAtXY(int x, int y) {
		for (int i = 0; i < 3; i++) {
			if (this.hounds[i].x == x && this.hounds[i].y == y) {
				return this.hounds[i];
			}
		}
		//We already know that a hound exists at x and y.
		return null;
	}
	
}
