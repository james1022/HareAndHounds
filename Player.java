package com.oose2015.jchoi100.hareandhounds;

//Assignment 1
//Name: Joon Hyuck Choi
//JHED: jchoi100
//email: jchoi100@jhu.edu
//Phone: 667-239-0288
//Section: 600.421

/**
 * This class is extended by HoundPlayer and HarePlayer.
 * 
 * @author Joon Hyuck Choi
 *
 */
public class Player {

	/** The gameID of the player. */
	public String playerId;
	
	/** The piece type. */
	public String pieceType;
	
	/** Shows whether the player is the Hare. */
	public boolean isHare;
	
	/** Shows whether the player is the Hound. */
	public boolean isHound;

	/**
	 * Constructor for the Player class.
	 * 
	 * @param id player's gameID.
	 * @param piece the piece type of the player.
	 */
	public Player(String pieceType) {
		this.pieceType = pieceType;
		if (pieceType.equals("HARE")) {
			this.isHare = true;
			this.isHound = false;
		} else {
			this.isHound = true;
			this.isHare = false;
		}
	}
	
}
