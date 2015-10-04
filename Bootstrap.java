package com.oose2015.jchoi100.hareandhounds;

//Assignment 1
//Name: Joon Hyuck Choi
//JHED: jchoi100
//email: jchoi100@jhu.edu
//Phone: 667-239-0288
//Section: 600.421

import com.oose2015.jchoi100.hareandhounds.GameService.GameServiceException;
import static spark.Spark.*;
import java.util.HashMap;

public class Bootstrap {
    public static final String IP_ADDRESS = "localhost";
    public static final int PORT = 8080;

    /**
	 * Main method for this class.
	 * 
	 * @param args arguments for the main method.
     * @throws GameServiceException 
	 */
	public static void main(String[] args) throws GameServiceException {
		ipAddress(IP_ADDRESS);
		port(PORT);
		
		staticFileLocation("/public");
		
		//Make a new map of games and pass it on to GameService.
		HashMap<String, Game> gameMap = new HashMap<>();
		GameService model = new GameService(gameMap);
		
		//Pass on the GameService with the map of games to Controller.
		new GameController(model);
		
	} //end main
    
} //end Bootstrap
