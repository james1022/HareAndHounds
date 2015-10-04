package com.oose2015.jchoi100.hareandhounds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

//Assignment 1
//Name: Joon Hyuck Choi
//JHED: jchoi100
//email: jchoi100@jhu.edu
//Phone: 667-239-0288
//Section: 600.421

/**
 * The Game class is the aggregate of all other classes in the package.
 * All the main method in the Bootstrap class needs to do is make an
 * instance of the Game class and call its runGame() method.
 * The runGame method and the constructor are the only public members
 * inside the entire class.
 * 
 * @author Joon Hyuck Choi
 *
 */
public class Game {
	
	/** This is the game board. */
	private Board board;
	
	/** The game's unique game id. */
	private String gameId;
	
	/** The game's first player. */
	private Player firstPlayer;
	
	/** The game's second player. */
	private Player secondPlayer;
	
	/** The piece type of the first player. */
	private String firstPlayerType;
	
	/** The piece type of the second player. */
	private String secondPlayerType;
	
	/** The first player's id. */
	private String firstPlayerId;
	
	/** The second player's id. */
	private String secondPlayerId;
	
	/** Out of the firstPlayer and the secondPlayer, 
	 * whoever is the hare player gets referenced. */
	private HarePlayer harePlayer;
	
	/** Out of the firstPlayer and the secondPlayer, 
	 * whoever is the hound player gets referenced. */
	private HoundPlayer houndPlayer;
	
	/** Indicates if the turn is the hound's. */
	private boolean isHoundTurn;
	
	/** Indicates if a second player has joined. */
	private boolean secondPlayerJoined;
	
	/** Describes the game's current status. */
	private String gameState;
	
	/** Indicates which piece type's turn it is. "HOUND" or "HARE". */
	private String turn;
	
	/** Saves each screenshot of the board. Used for determining stalling. */
	private HashMap<ArrayList<ArrayList<Integer>>, Integer> states;
	
	/** Lets the program easily find the coordinates of a "Square" in the board
	 * using the unique square serial number each "Square" has been assigned. */
	private HashMap<Integer, ArrayList<Integer>> squareFinder;
	
	//Numbering of the Squares on the board. ("Serial number")
	
    // 0  1   2   3   4
	// 5  6   7   8   9
	//10 11  12  13  14
	
	//0, 4, 10, 14 are empty squares, and this fact is reflected in class Board.
	
	/**
	 * Constructor for the Game class.
	 * 
	 * @param firstPlayerID the playerID of the person who started the game.
	 * @param firstPlayerType the piece type of the person who started the game.
	 */
	public Game(String pieceType) {
		
		//Set up the board.
		this.squareFinder = new HashMap<>();
		this.board = new Board(squareFinder);
		
		//We're waiting for the second player to join.
		this.secondPlayerJoined = false;
		this.gameState = "WAITNG_FOR_SECOND_PLAYER";
		this.turn = "HOUND";
		
		//Set up players.
		//Although the second player has not joined yet,
		//create instances of both players in advance.
		//This is just a matter of choice.
		//I might as well have created the second player
		//later, but just had both players created and
		//ready from the beginning.
		this.houndPlayer = new HoundPlayer("HOUND");
		this.harePlayer = new HarePlayer("HARE");
		if (pieceType.equals("HOUND")) {
			this.firstPlayerType = "HOUND";
			this.secondPlayerType = "HARE";
			this.firstPlayer = this.houndPlayer;
			this.secondPlayer = this.harePlayer;
		} else {
			this.firstPlayerType = "HARE";
			this.secondPlayerType = "HOUND";
			this.firstPlayer = this.harePlayer;
			this.secondPlayer = this.houndPlayer;
		}
		this.firstPlayerId = "p1";
		this.secondPlayerId = "p2";
		this.firstPlayer.playerId = this.firstPlayerId;
		this.secondPlayer.playerId = this.secondPlayerId;
		this.firstPlayer.pieceType = this.firstPlayerType;
		this.secondPlayer.pieceType = this.secondPlayerType;
		
		//Place the pieces on the board.
		this.board.grid[0][1].houndOnTop = true;
		this.board.grid[1][0].houndOnTop = true;
		this.board.grid[2][1].houndOnTop = true;
		this.board.grid[1][4].hareOnTop = true;
		
		//The game starts with the hound's turn.
		this.isHoundTurn = true;
		
		//HashMap to save the screenshots of the board status.
		this.states = new HashMap<ArrayList<ArrayList<Integer>>, Integer>();
	} //end constructor

	/**
	 * Runs the hound's turn.
	 */
	public void houndTurn(int fromX, int fromY, int toX, int toY) throws GameService.IllegalMoveException {
		
		//Declare and initialize variables to be used.
		int currSqNum = this.board.grid[fromY][fromX].squareNum;
		int nextSqNum = this.board.grid[toY][toX].squareNum;
		
		//Check if there is a hound sitting at the current spot and
		//if there is anything sitting at the (toX, toY) spot.
		if (!this.board.grid[fromY][fromX].houndOnTop 
				|| this.board.grid[toY][toX].houndOnTop 
				|| this.board.grid[toY][toX].hareOnTop) {
			throw new GameService.IllegalMoveException("There is nothing to move there or "
														+ "something is already sitting at the destination.");
		}
		
		//I now know that there exists a hound to move at the designated spot and that 
		//there is nothing at the spot where it's planning on moving to.
		//The only exception that can be thrown now is if there is no edge connecting the two squares.
		Hound houndToMove = this.houndPlayer.getHoundAtXY(fromX, fromY);
		
		if (this.board.houndGraph[currSqNum][nextSqNum] == 1) {
			//Changes the x and y members of the Hound that's being moved.
			houndToMove.x = toX;
			houndToMove.y = toY;
			
			//Flips the boolean that indicates whether a hound is on top
			//of the square or not.
			this.board.grid[fromY][fromX].houndOnTop = false;
			this.board.grid[toY][toX].houndOnTop = true;
			
			//Since the hound made a valid move, pass the turn to Hare.
			this.isHoundTurn = false;
			this.turn = "HARE";
			this.gameState = "TURN_HARE";
			
			//Gather information to save in the HashMap of states.
			int hound1X = this.houndPlayer.hounds[0].x;
			int hound2X = this.houndPlayer.hounds[1].x;
			int hound3X = this.houndPlayer.hounds[2].x;
			int hound1Y = this.houndPlayer.hounds[0].y;
			int hound2Y = this.houndPlayer.hounds[1].y;
			int hound3Y = this.houndPlayer.hounds[2].y;
			int hareX = this.harePlayer.hare.x;
			int hareY = this.harePlayer.hare.y;
			
			//With the above x, y information, get the square number of the squares.
			//It is cleaner to save the square numbers rather than 3 pairs of x's and y's.
			int hound1SqNum = this.board.grid[hound1Y][hound1X].squareNum;
			int hound2SqNum = this.board.grid[hound2Y][hound2X].squareNum;
			int hound3SqNum = this.board.grid[hound3Y][hound3X].squareNum;
			int hareSqNum = this.board.grid[hareY][hareX].squareNum;
			
			//Hound positions' ArrayList
			ArrayList<Integer> hounds = new ArrayList<>();
			hounds.add(hound1SqNum);
			hounds.add(hound2SqNum);
			hounds.add(hound3SqNum);
			
			//Hare Position ArrayList
			ArrayList<Integer> hare = new ArrayList<>();
			hare.add(hareSqNum);
			
			//Now put the two ArrayLists in an ArrayList of ArrayLists.
			ArrayList<ArrayList<Integer>> state = new ArrayList<>();
			//Sort the numbers in the hound list
			Collections.sort(hounds);
			state.add(hounds);
			state.add(hare);
			
			//See if the state is a repeated state.
			if (this.states.containsKey(state)) {
				//If it is, remove it, increment its value, and put it back.
				Integer num = this.states.get(state);
				num++;
				states.remove(state);
				states.put(state, num);
			} else {
				//Else, newly add the state.
				states.put(state, 1);
			}
		} else {
			throw new GameService.IllegalMoveException("Cannot move hound that way!");
		}
		
	} //end houndTurn()
	
	/**
	 * Runs the hare's turn.
	 * Logic and comments are identical to those of houndTurn().
	 */
	public void hareTurn(int fromX, int fromY, int toX, int toY) throws GameService.IllegalMoveException {

		//Declare and initialize variables to be used.
		int currSqNum = this.board.grid[fromY][fromX].squareNum;
		int nextSqNum = this.board.grid[toY][toX].squareNum;

		//Check if there is a hare to be moved and if nothing is sitting on the (toX, toY) spot.
		if (!this.board.grid[fromY][fromX].hareOnTop 
				|| this.board.grid[toY][toX].houndOnTop 
				|| this.board.grid[toY][toX].hareOnTop) {
			throw new GameService.IllegalMoveException("There is nothing to move there or "
														+ "something is already sitting at the destination.");
		}
		
		if (this.board.hareGraph[currSqNum][nextSqNum] == 1) {

			//Change the information in HarePlayer and Board.
			this.harePlayer.hare.x = toX;
			this.harePlayer.hare.y = toY;
			this.board.grid[fromY][fromX].hareOnTop = false;
			this.board.grid[toY][toX].hareOnTop = true;
			
			//Update the changes in the game.
			this.isHoundTurn = true;
			this.turn = "HOUND";
			this.gameState = "TURN_HOUND";
			
			//Retrieve information to save the screenshot of the game.
			int hound1X = this.houndPlayer.hounds[0].x;
			int hound2X = this.houndPlayer.hounds[1].x;
			int hound3X = this.houndPlayer.hounds[2].x;
			int hound1Y = this.houndPlayer.hounds[0].y;
			int hound2Y = this.houndPlayer.hounds[1].y;
			int hound3Y = this.houndPlayer.hounds[2].y;
			int hareX = this.harePlayer.hare.x;
			int hareY = this.harePlayer.hare.y;
			
			//Get the square numbers based on the attained x's and y's.
			int hound1SqNum = this.board.grid[hound1Y][hound1X].squareNum;
			int hound2SqNum = this.board.grid[hound2Y][hound2X].squareNum;
			int hound3SqNum = this.board.grid[hound3Y][hound3X].squareNum;
			int hareSqNum = this.board.grid[hareY][hareX].squareNum;
			
			//Add the Hounds' square numbers in one list.
			ArrayList<Integer> hounds = new ArrayList<>();
			hounds.add(hound1SqNum);
			hounds.add(hound2SqNum);
			hounds.add(hound3SqNum);
			
			//Add the Hare's square number in another.
			ArrayList<Integer> hare = new ArrayList<>();
			hare.add(hareSqNum);
			
			//Add the two lists in one bigger list.
			ArrayList<ArrayList<Integer>> state = new ArrayList<>();
			Collections.sort(hounds);
			state.add(hounds);
			state.add(hare);
			
			//Add that bigger list to the HashMap of states.
			if (this.states.containsKey(state)) {
				//State already exists.
				Integer num = this.states.get(state);
				num++;
				states.remove(state);
				states.put(state, num);
			} else {
				//The state occurs for the first time.
				states.put(state, 1);
			}
			
		} else {
			throw new GameService.IllegalMoveException("Cannot move hare that way!");
		}
		
	} //end hareTurn()
	
	/**
	 * Checks if the game is over.
	 * 
	 * @return 0 if the game is still on, 1 if the hare won by escaping,
	 * 		   2 if the hare won because the hounds were stalling, or
	 * 		   3 if the hound won by trapping the hare.
	 */
	public int gameOver() {
		if (hareEscaped()) {
			return 1;
		} else if (isStalling()) {
			return 2;
		} else if (hareTrapped()) {
			return 3;
		} else {
			return 0;
		}

	} //end gameOver()
	
	/**
	 * Checks if the hare has escaped the hounds.
	 * Gets called in gameOver().
	 * The hare has escaped if its x coordinate is less than or 
	 * equal to the left-most hound's x coordinate.
	 * 
	 * @return true if hare has escaped, false if not.
	 */
	private boolean hareEscaped() {
		//Find the left-most hound.
		int leftMostHoundXPos = Math.min(this.houndPlayer.hounds[0].x, this.houndPlayer.hounds[1].x);
		leftMostHoundXPos = Math.min(leftMostHoundXPos, this.houndPlayer.hounds[2].x);
		//Figure out the hare's x coordinate.
		int hareXPos = this.harePlayer.hare.x;
		//Compare and return the result.
		return leftMostHoundXPos >= hareXPos;
	} //end hareEscaped()
	
	/**
	 * Accesses the "state saver" HashMap member variable "states"
	 * and checks if any state has repeated 3 times.
	 * Gets called in gameOver().
	 * 
	 * @return true if any state has repeated 3 times, false if not.
	 */
	private boolean isStalling() {
		//Put all the values of the HashMap (i.e. the number of occurrences of
		//each state saved in the HashMap) and see if any of them is equal to 3.
		Collection<Integer> allValues = this.states.values();
		for(Integer i : allValues) {
			if (i == 3) {
				return true;
			}
		}
		return false;
	} //end isStalling()
	
	/**
	 * Check if the hare is trapped.
	 * Gets called in gameOver().
	 * We check for all adjacent Squares of the Square
	 * that the hare is currently sitting on, if there are
	 * any Squares that the hare can escape to.
	 * 
	 * @return true is the hare is trapped, false if not.
	 */
	private boolean hareTrapped() {
		//Figure out on which Square the hare is sitting on.
		int hareXPos = this.harePlayer.hare.x;
		int hareYPos = this.harePlayer.hare.y;
		int hareSqNum = this.board.grid[hareYPos][hareXPos].squareNum;
		
		//Get the one row from the adjacency list for the hareGraph.
		int[] adj = this.board.hareGraph[hareSqNum];
		boolean isTrapped = true;
		
		//Run through that one row and inspect all the adjacent Squares.
		for (int i = 0; i < 15; i++) {
			if (adj[i] == 1) { //this means that adj[i] is an adjacent Square.
				//Get the x, y coordinates of the adjacent Square.
				ArrayList<Integer> pair = this.board.sqFinder.get(i);
				int x = pair.get(0);
				int y = pair.get(1);	
				//See if there is a hound sitting on top of it.
				if (!this.board.grid[x][y].houndOnTop) {
					//If not, the hare can escape to that Square.
					//So it is not trapped.
					isTrapped = false;
				} //end inner if
			} //end outer if
		} //end for
		return isTrapped;
	} //end hareTrapped()
	
	/*****************************************************************/
	/**From here and until the end are a bunch of getters and setters*/
	/************The method names are all self-explanatory************/
	
	public boolean isHoundTurn() {
		return this.isHoundTurn;
	}
	
	public String getGameId() {
		return this.gameId;
	}
	
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
	public String getGameState() {
		return this.gameState;
	}
	
	public String getFirstPlayerType() {
		return this.firstPlayerType;
	}
	
	public String getSecondPlayerType() {
		return this.secondPlayerType;
	}
	
	public void setFirstPlayerType(String type) {
		this.firstPlayerType = type;
	}
	
	public void setSecondPlayerType(String type) {
		this.secondPlayerType = type;
	}
	
	public String getFirstPlayerId() {
		return this.firstPlayerId;
	}
	
	public String getSecondPlayerId() {
		return this.secondPlayerId;
	}
	
	public void setFirstPlayerId(String id) {
		this.firstPlayer.playerId = id;
		this.firstPlayerId = id;
	}
	
	public void setSecondPlayerId(String id) {
		this.secondPlayer.playerId = id;
		this.secondPlayerId = id;
	}
	
	public void setGameState(String state) {
		this.gameState = state;
	}
	
	public int getHound1X() {
		return this.houndPlayer.hounds[0].x;
	}
	
	public int getHound2X() {
		return this.houndPlayer.hounds[1].x;
	}
	
	public int getHound3X() {
		return this.houndPlayer.hounds[2].x;
	}
	
	public int getHound1Y() {
		return this.houndPlayer.hounds[0].y;
	}
	
	public int getHound2Y() {
		return this.houndPlayer.hounds[1].y;
	}
	
	public int getHound3Y() {
		return this.houndPlayer.hounds[2].y;
	}
	
	public int getHareX() {
		return this.harePlayer.hare.x;
	}
	
	public int getHareY() {
		return this.harePlayer.hare.y;
	}
	
	public void secondPlayerHasNotJoined() {
		this.secondPlayerJoined = false;
	}
	
	public void secondPlayerHasJoined() {
		this.secondPlayerJoined = true;
	}
	
	public boolean getSecondPlayerJoined() {
		return this.secondPlayerJoined;
	}
	
	public String getTurn() {
		return this.turn;
	}
	
	public void setTurn(String turn) {
		this.turn = turn;
	}
	
	public void updateStates() {
		if (this.hareEscaped()) {
			this.gameState = "WIN_HARE_BY_ESCAPE";
		} else if (this.isStalling()) {
			this.gameState = "WIN_HARE_BY_STALLING";
		} else if (this.hareTrapped()) {
			this.gameState = "WIN_HOUND";
		}  else if (!this.secondPlayerJoined) {
			this.gameState = "WAITING_FOR_SECOND_PLAYER";
		} else if (this.isHoundTurn) {
			this.gameState =  "TURN_HOUND";
		} else if (!this.isHoundTurn) {
			this.gameState = "TURN_HARE";
		} else {
			this.gameState = "";
		}
	}
	
} //end Game class
