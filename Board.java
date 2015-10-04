package com.oose2015.jchoi100.hareandhounds;
import java.util.HashMap;
import java.util.ArrayList;

//Assignment 1
//Name: Joon Hyuck Choi
//JHED: jchoi100
//email: jchoi100@jhu.edu
//Phone: 667-239-0288
//Section: 600.421

/**
 * This class is a representation of a board for the game.
 * 
 * @author Joon Hyuck Choi
 *
 */
public class Board {

	/** The board is represented by a 2D array of Square objects. */
	public Square[][] grid;
	
	/** Adjacency matrix representation of the board for the hare player. */
	public int[][] hareGraph;
	
	/** Adjacency matrix representation of the board for the hound player. */
	public int[][] houndGraph;
	
	/** This HashMap makes it easier to find the x, y coordinates
	 * of a square just given its unique square number as the key. 
	 * The unique numbering of the squares is documented in the Game class. */
	public HashMap<Integer, ArrayList<Integer>> sqFinder;
	
	/**
	 * Constructor for the Board class.
	 * 
	 * @param finder the HashMap in which we'll store each square's unique
	 * 		  		 square number as the key and the x, y coordinates as the value.
	 */
	public Board(HashMap<Integer, ArrayList<Integer>> sqFinder) {
		
		//Set up the grid.
		//Again, number conventions all following the x, y from course web site.
		//On the grid[][], it is actually backwards. i.e. [y][x].
		this.grid = new Square[3][5];
		
		//This exists for us to easily look up the coordinates with just the node number.
		this.sqFinder = sqFinder;
		
		//These are the empty nodes in the graph
		//Nothing can go on top of these squares
		//We first make an instance of a new Square,
		//create an ArrayList of Integers to represent the
		//coordinates, and add each square's unique
		//square number and the ArrayList to the HashMap.
		//ref.    y  x                         x  y
		this.grid[0][0] = new Square(0, false, 0, 0);
		ArrayList<Integer> list = new ArrayList<>();
		list.add(0);
		list.add(0);
		this.sqFinder.put(0, list);
		this.grid[2][0] = new Square(10, false, 0, 2);
		list = new ArrayList<>();
		list.add(2);
		list.add(0);
		this.sqFinder.put(10, list);
		this.grid[0][4] = new Square(4, false, 4, 0);
		list = new ArrayList<>();
		list.add(0);
		list.add(4);
		this.sqFinder.put(4, list);
		this.grid[2][4] = new Square(14, false, 4, 2);
		list = new ArrayList<>();
		list.add(2);
		list.add(4);
		this.sqFinder.put(14, list);
		
		//These are the actual nodes in the graph.
		//Hounds and the Hare can actually go on them.
		this.grid[1][0] = new Square(5, true, 0, 1);
		list = new ArrayList<>();
		list.add(1);
		list.add(0);
		this.sqFinder.put(5, list);
		this.grid[0][1] = new Square(1, true, 1, 0);
		list = new ArrayList<>();
		list.add(0);
		list.add(1);
		this.sqFinder.put(1, list);
		this.grid[1][1] = new Square(6, true, 1, 1);
		list = new ArrayList<>();
		list.add(1);
		list.add(1);
		this.sqFinder.put(6, list);
		this.grid[2][1] = new Square(11, true, 1, 2);
		list = new ArrayList<>();
		list.add(2);
		list.add(1);
		this.sqFinder.put(11, list);
		this.grid[0][2] = new Square(2, true, 2, 0);
		list = new ArrayList<>();
		list.add(0);
		list.add(2);
		this.sqFinder.put(2, list);
		this.grid[1][2] = new Square(7, true, 2, 1);
		list = new ArrayList<>();
		list.add(1);
		list.add(2);
		this.sqFinder.put(7, list);
		this.grid[2][2] = new Square(12, true, 2, 2);
		list = new ArrayList<>();
		list.add(2);
		list.add(2);
		this.sqFinder.put(12, list);
		this.grid[0][3] = new Square(3, true, 3, 0);
		list = new ArrayList<>();
		list.add(0);
		list.add(3);
		this.sqFinder.put(3, list);
		this.grid[1][3] = new Square(8, true, 3, 1);
		list = new ArrayList<>();
		list.add(1);
		list.add(3);
		this.sqFinder.put(8, list);
		this.grid[2][3] = new Square(13, true, 3, 2);
		list = new ArrayList<>();
		list.add(2);
		list.add(3);
		this.sqFinder.put(13, list);
		this.grid[1][4] = new Square(9, true, 4, 1);
		list = new ArrayList<>();
		list.add(1);
		list.add(4);
		this.sqFinder.put(9, list);

		//Set up hareGraph and houndGraph: adjacency matrix method.
		this.hareGraph = new int[15][15];
		this.houndGraph = new int[15][15];
		
		//hareGraph[a][b] represents a path from square a to square b.
		//Fill in hareGraph's adjacency matrix.
		this.hareGraph[1][2] = 1;
		this.hareGraph[1][5] = 1;
		this.hareGraph[1][6] = 1;
		this.hareGraph[1][7] = 1;
		this.hareGraph[2][1] = 1;
		this.hareGraph[2][3] = 1;
		this.hareGraph[2][7] = 1;
		this.hareGraph[3][2] = 1;
		this.hareGraph[3][7] = 1;
		this.hareGraph[3][8] = 1;
		this.hareGraph[3][9] = 1;
		this.hareGraph[5][1] = 1;
		this.hareGraph[5][6] = 1;
		this.hareGraph[5][11] = 1;
		this.hareGraph[6][1] = 1;
		this.hareGraph[6][5] = 1;
		this.hareGraph[6][7] = 1;
		this.hareGraph[6][11] = 1;
		this.hareGraph[7][1] = 1;
		this.hareGraph[7][2] = 1;
		this.hareGraph[7][3] = 1;
		this.hareGraph[7][6] = 1;
		this.hareGraph[7][8] = 1;
		this.hareGraph[7][11] = 1;
		this.hareGraph[7][12] = 1;
		this.hareGraph[7][13] = 1;
		this.hareGraph[8][3] = 1;
		this.hareGraph[8][7] = 1;
		this.hareGraph[8][9] = 1;
		this.hareGraph[8][13] = 1;
		this.hareGraph[9][3] = 1;
		this.hareGraph[9][8] = 1;
		this.hareGraph[9][13] = 1;
		this.hareGraph[11][5] = 1;
		this.hareGraph[11][6] = 1;
		this.hareGraph[11][7] = 1;
		this.hareGraph[11][12] = 1;
		this.hareGraph[12][7] = 1;
		this.hareGraph[12][11] = 1;
		this.hareGraph[12][13] = 1;
		this.hareGraph[13][7] = 1;
		this.hareGraph[13][8] = 1;
		this.hareGraph[13][9] = 1;
		this.hareGraph[13][12] = 1;
		
		//Fill in houndGraph's adjacency matrix.
		this.houndGraph[1][2] = 1;
		this.houndGraph[1][6] = 1;
		this.houndGraph[1][7] = 1;
		this.houndGraph[2][3] = 1;
		this.houndGraph[2][7] = 1;
		this.houndGraph[3][8] = 1;
		this.houndGraph[3][9] = 1;
		this.houndGraph[5][1] = 1;
		this.houndGraph[5][6] = 1;
		this.houndGraph[5][11] = 1;
		this.houndGraph[6][1] = 1;
		this.houndGraph[6][7] = 1;
		this.houndGraph[6][11] = 1;
		this.houndGraph[7][2] = 1;
		this.houndGraph[7][3] = 1;
		this.houndGraph[7][8] = 1;
		this.houndGraph[7][12] = 1;
		this.houndGraph[7][13] = 1;
		this.houndGraph[8][3] = 1;
		this.houndGraph[8][9] = 1;
		this.houndGraph[8][13] = 1;
		this.houndGraph[11][6] = 1;
		this.houndGraph[11][12] = 1;
		this.houndGraph[12][7] = 1;
		this.houndGraph[12][13] = 1;
		this.houndGraph[13][8] = 1;
		this.houndGraph[13][9] = 1;		
	} //end constructor
	
} //end class Board
