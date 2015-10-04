package com.oose2015.jchoi100.hareandhounds;

//Assignment 1
//Name: Joon Hyuck Choi
//JHED: jchoi100
//email: jchoi100@jhu.edu
//Phone: 667-239-0288
//Section: 600.421

/**
 * Class HarePlayer has one Hare piece.
 * The Game class has one HarePlayer.
 * 
 * @author Joon Hyuck Choi
 *
 */
public class HarePlayer extends Player {

	/** Represents the one Hare piece that this player has. */
	public Hare hare;

	/**
	 * Constructor for the Player class.
	 * 
	 * @param id player's gameID.
	 * @param piece the piece type of the player.
	 */
	public HarePlayer(String pieceType) {
		super(pieceType);
		this.pieceType = pieceType;
		this.isHare = true;
		this.isHound = false;
		this.hare = new Hare(4, 1);
	}
	
	
	
}
