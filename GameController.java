package com.oose2015.jchoi100.hareandhounds;

//Assignment 1
//Name: Joon Hyuck Choi
//JHED: jchoi100
//email: jchoi100@jhu.edu
//Phone: 667-239-0288
//Section: 600.421

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collections;

import static spark.Spark.*;

//Code adapted from TodoController.java

/**
 * The GameController class gets called in the Bootstrap and
 * ultimately controls the Game.
 * It has methods to start/join/play a game and describe the
 * board and the game state.
 * 
 * @author Joon Hyuck Choi
 *
 */
public class GameController {

	private static final String API_CONTEXT = "/hareandhounds/api/";
	
	private final GameService gameService;
    
	/**
	 * Constructor for the class.
	 * 
	 * @param gameService a GameService object.
	 */
	public GameController(GameService gameService) {
		this.gameService = gameService;
		setupEndPoints();
	} //end constructor
	
	/**
	 * Lets users start a game (post), join a game (put), play a game (post),
	 * check the board status (get), and check the game state (get).
	 */
	private void setupEndPoints() {
		
		//Start a game
		post(API_CONTEXT + "/games", "application/json", (request, response) -> {
			//Makes a GameStartInfo object with just the information we need to return as Json.
			GameStartInfo gameStart = (GameStartInfo) gameService.createNewGame(request.body());
			response.status(201);
			return gameStart;
		}, new JsonTransformer());
		
		//Join a game
		put(API_CONTEXT + "/games/:gameId", "application/json", (request, response) -> {
			try {
				//Makes a GameJoinInfo object with just the information we need in Json.
				//Contents are the same as GameStartInfo, but I made a separate class for this
				//to make the names of the classes more self-explanatory and specific.
				GameJoinInfo gameJoin = (GameJoinInfo) gameService.joinGame(request.params(":gameId"));
				response.status(200);
				return gameJoin;
			} catch (GameService.GameServiceException ex) {
				//If a game with the :gameId could not be found.
				response.status(404);
				return Collections.EMPTY_MAP;
			} catch (GameService.SecondPlayerJoinedException ex) {
				//If a second player had already joined.
				response.status(410);
				return Collections.EMPTY_MAP;
			}
		}, new JsonTransformer());
		
		//Make a move
		post(API_CONTEXT + "/games/:gameId/turns", "application/json", (request, response) -> {
			try {
				//Gets the playerId of the player that just made a move.
				String playerId = gameService.playGame(request.params(":gameId"), request.body());
				response.status(200);
				//Stores the returned playerId into a Move object and return it.
				Move move = new Move();
				move.playerId = playerId;
				return move;
			} catch(GameService.GameServiceException ex) {
				response.status(404);
				FailureReason failure = new FailureReason();
				failure.reason = "INVALID_GAME_ID";
				return failure;
			} catch(GameService.InvalidPlayerIdException ex) {
				response.status(404);
				FailureReason failure = new FailureReason();
				failure.reason = "INVALID_PLAYER_ID";
				return failure;
			} catch(GameService.IncorrectTurnException ex) {
				response.status(422);
				FailureReason failure = new FailureReason();
				failure.reason = "INCORRECT_TURN";
				return failure;
			} catch(GameService.IllegalMoveException ex) {
				//If the move is illegal during a game or
				//if any player tries to move after a game is over.
				response.status(422);
				FailureReason failure = new FailureReason();
				failure.reason = "ILLEGAL_MOVE";
				return failure;
			}
			
		}, new JsonTransformer());
		
		//Board State description
		get(API_CONTEXT + "/games/:gameId/board", "application/json", (request, response) -> {
			try {
				return gameService.getBoardStatus(request.params(":gameId"));
			} catch (GameService.GameServiceException ex) {
				response.status(404);
				return Collections.EMPTY_MAP;
			}
		}, new JsonTransformer());

		//Game State description
		get(API_CONTEXT +"/games/:gameId/state", "application/json", (request, response) -> {
			try {
				//Retrieves the game object with the specified :gameId and returns its state.
				Game game = gameService.find(request.params(":gameId"));
				response.status(200);
				State state = new State();
				state.state = game.getGameState();
				return state;
			} catch (GameService.GameServiceException ex) {
                response.status(404);
                return Collections.EMPTY_MAP;
			}
		}, new JsonTransformer());
		
	} //end setEndPoints()
	
} //end GameController class
