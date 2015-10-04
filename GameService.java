package com.oose2015.jchoi100.hareandhounds;

//Assignment 1
//Name: Joon Hyuck Choi
//JHED: jchoi100
//email: jchoi100@jhu.edu
//Phone: 667-239-0288
//Section: 600.421

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class GameService {

	/** Contains all the games created in a HashMap. */
	private HashMap<String, Game> gameMap;
	
	/** The current number of games. */
	private Integer numGames = 0;
	
	/**
	 * Constructor for the GameService class.
	 * 
	 * @param gameMap the map of games.
	 */
	public GameService(HashMap<String, Game> gameMap) {
		this.gameMap = gameMap;
	} //end constructor
	
	/**
	 * Creates a new game.
	 * 
	 * @param body the contents sent from the user's front end. 
	 * 			   It contains the starting player's piece type.
	 * @return a GameStartInfo object with the required information in it.
	 */
	public GameStartInfo createNewGame(String body) {
		
		GameStartInfo gameStart = new Gson().fromJson(body, GameStartInfo.class);
		
		//Set up the game id.
		this.numGames++;
		String gameId = "g" + this.numGames.toString();
		
		//Set things up in gameStart which will be returned.
		String pieceType = gameStart.pieceType;
		gameStart.gameId = gameId;
		gameStart.playerId = "p1";
		
		//Set things in the game object as well.
		Game game = new Game(pieceType);
		game.setGameId(gameId);
		game.setFirstPlayerId("p1");
		game.setGameState("WAITING_FOR_SECOND_PLAYER");
		game.secondPlayerHasNotJoined();
	
		//Put the game object in the HashMap.
		this.gameMap.put(gameId, game);
		
		return gameStart;
		
	} //end createNewGame()
	
	/**
	 * Lets the second user join the game.
	 * 
	 * @param gameId the game id of the game to be joined.
	 * @return a GameJoinInfo object that contains the necessary information.
	 * @throws GameServiceException if the game cannot be found.
	 * @throws SecondPlayerJoinedException if a second player has already joined.
	 */
	public GameJoinInfo joinGame(String gameId) throws GameServiceException, SecondPlayerJoinedException {
		
		if (this.gameMap.containsKey(gameId)) {
			//Retrieve the game.
			Game game = this.gameMap.get(gameId);
			String firstPlayerType = game.getFirstPlayerType();
			String secondPlayerType = firstPlayerType.equals("HOUND")? "HARE" : "HOUND";
			
			//Check if a second player already exists.
			if (game.getSecondPlayerJoined()) {
				throw new SecondPlayerJoinedException("A second player has already joined!");
			}
			
			//Set the second player's attributes.
			game.setSecondPlayerType(secondPlayerType);
			game.setSecondPlayerId("p2");
			
			//Update fields in the game.
			game.setGameState("TURN_HOUND");
			game.setTurn("HOUND");
			game.secondPlayerHasJoined();
			
			//Make a GameJoinInfo object to be returned.
			GameJoinInfo gameJoin = new GameJoinInfo();
			gameJoin.gameId = game.getGameId();
			gameJoin.playerId = "p2";
			gameJoin.pieceType = secondPlayerType;
			
			return gameJoin;
			
		} else {
			throw new GameServiceException("No game with that id!");
		}
		
	} //end joinGame()
	
	/**
	 * Let the users play one turn of the game.
	 * @param gameId the id of the game to be played.
	 * @param body user's input content with the piece type, toX, toY, fromX, and fromY.
	 * @return the id of the user that just made the move.
	 * @throws GameServiceException if the game cannot be found.
	 * @throws JsonSyntaxException if there is something wrong with the json syntax.
	 * @throws InvalidPlayerIdException if the player id is invalid.
	 * @throws IncorrectTurnException if the turn is not correct.
	 * @throws IllegalMoveException if the move is not legal.
	 */
	public String playGame(String gameId, String body) throws GameServiceException, JsonSyntaxException,
															  InvalidPlayerIdException, IncorrectTurnException, 
															  IllegalMoveException {	
		//Check if the game exists.
		if (!this.gameMap.containsKey(gameId)) {
			throw new GameServiceException("There is no game with that id!");
		}
		Game game = this.gameMap.get(gameId);
		String pieceType = game.getTurn();
		
		//Get information about a user's request.
		MoveRequest requestedMove = new Gson().fromJson(body, MoveRequest.class);
		String playerId = requestedMove.playerId;
		int fromX = requestedMove.fromX;
		int fromY = requestedMove.fromY;
		int toX = requestedMove.toX;
		int toY = requestedMove.toY;
	
		//Check if player id is valid.
		if (!playerId.equals("p1") && !playerId.equals("p2")) {
			throw new InvalidPlayerIdException("No player with that id!");
		}
		
		//Check if the game has not ended yet.
		if (game.gameOver() == 1 || game.gameOver() == 2 || game.gameOver() == 3) {
			throw new IllegalMoveException("The game has already ended!");
		}
		
		//Check if it's the right turn.
		boolean requestIsFirstPlayer = playerId.equals("p1")? true : false;
		if (requestIsFirstPlayer) {
			if (!game.getFirstPlayerType().equals(game.getTurn())) {
				throw new IncorrectTurnException("Incorrect turn!");
			}
		} else {
			if (!game.getSecondPlayerType().equals(game.getTurn())) {
				throw new IncorrectTurnException("Incorrect turn!");
			}
		}
		
		//Make move and throw an exception if the move is illegal.
		if (pieceType.equals("HOUND")) {
			game.houndTurn(fromX, fromY, toX, toY);
		} else {
			game.hareTurn(fromX, fromY, toX, toY);
		}
		
		//Updates the staes.
		game.updateStates();
		
		return playerId;
		
	} //end playGame()
	
	/**
	 * Allows the program to see the current state of the Board.
	 * 
	 * @param gameId the id of the game to be inspected.
	 * @return an ArrayList of PieceStatus objects. Each Piece Status object 
	 * 		   contains a piece type, x, and y coordinates saved in it.
	 * @throws GameServiceException if the game cannot be found.
	 */
	public ArrayList<PieceStatus> getBoardStatus(String gameId) throws GameServiceException {

		//Check if the game exists.
		if (!this.gameMap.containsKey(gameId)) {
			throw new GameServiceException("There is no game with that id!");
		}
		Game game = this.gameMap.get(gameId);
		
		//Get information from the game.
		Integer hound1X = game.getHound1X();
		Integer hound2X = game.getHound2X();
		Integer hound3X = game.getHound3X();
		Integer hound1Y = game.getHound1Y();
		Integer hound2Y = game.getHound2Y();
		Integer hound3Y = game.getHound3Y();
		Integer hareX = game.getHareX();
		Integer hareY = game.getHareY();
		
		//Make PieceStatus objects to be returned.
		PieceStatus hound1 = new PieceStatus();
		hound1.x = hound1X;
		hound1.y = hound1Y;
		hound1.pieceType = "HOUND";
		
		PieceStatus hound2 = new PieceStatus();
		hound2.x = hound2X;
		hound2.y = hound2Y;
		hound2.pieceType = "HOUND";
		
		PieceStatus hound3 = new PieceStatus();
		hound3.x = hound3X;
		hound3.y = hound3Y;
		hound3.pieceType = "HOUND";
		
		PieceStatus hare = new PieceStatus();
		hare.x = hareX;
		hare.y = hareY;
		hare.pieceType = "HARE";
		
		//Add the PieceStatus objects to the ArrayList.
		ArrayList<PieceStatus> list = new ArrayList<>();
		list.add(hound1);
		list.add(hound2);
		list.add(hound3);
		list.add(hare);
		
		return list;
	} //end getBoardStatus()
	
	/**
	 * Find a game with the given game id.
	 * 
	 * @param gameId the id of the game to be found.
	 * @return the Game we're looking for.
	 * @throws GameServiceException if the game does not exist.
	 */
	public Game find(String gameId) throws GameServiceException {
		if (this.gameMap.containsKey(gameId)) {
			return this.gameMap.get(gameId);
		} else {
			throw new GameServiceException("No game with that id!");
		}
	} //end find()
	
    @SuppressWarnings("serial")
	public static class GameServiceException extends Exception {
        public GameServiceException(String message) {
            super(message);
        }
    }
    
    @SuppressWarnings("serial")
	public static class SecondPlayerJoinedException extends Exception {
        public SecondPlayerJoinedException(String message) {
            super(message);
        }
    }
    
    @SuppressWarnings("serial")
	public static class InvalidPlayerIdException extends Exception {
        public InvalidPlayerIdException(String message) {
            super(message);
        }
    }
	
    @SuppressWarnings("serial")
	public static class IncorrectTurnException extends Exception {
        public IncorrectTurnException(String message) {
            super(message);
        }
    }
    
    @SuppressWarnings("serial")
	public static class IllegalMoveException extends Exception {
        public IllegalMoveException(String message) {
            super(message);
        }
    }
}
