package simacogo;

import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

//********************************//
//          Time per Move	  //
//  		(3 turn avg)	  // 
//	9-ply:    < 2 sec         // 
//  	10-ply:     6 sec         // 
//  	11-ply:    15 sec         // 
//  	12-ply:    44 sec         //  
//  	13-ply:   175 sec         //
//  	14-ply:   505 sec         //
//********************************//


public class playSimacogo {
	
	
	// ** Generate Moves ** 	
	public static LinkedList <Simacogo2> getMoves (Simacogo2 gameState, boolean aiPlayer){					// Creates a Linked List of all legal moves from this state	
		byte[][] board = gameState.CURRSTATE;										// Stores current token positions
		byte gameScore = gameState.SCORE;										// Stores current games score
		LinkedList <Simacogo2> moves = new LinkedList<Simacogo2>();							// Stores legal moves from current state
		byte token = 9;
		if (aiPlayer){
			token = 1;												// AI Player drops 1s
		} else {
			token = 0;												// Human Player drops 0s
		}
		
		for (short column = 0; column < 9; column++){									// Check each column for the lowest empty space
			byte[][] modified = new byte[9][9];
			
			for (short i = 0; i < 9; i++){										// Creates a copy of 2D array so we don't modify our original n^2 ouch!
				for (short j = 0; j < 9; j++){									// Without a deep copy all moves equal the last modification
					modified[i][j] = board[i][j];
				}
			}
			
			if (board[8][column] != 9){										// Avoid searching full columns  
				continue;																					
			}
			if (board[0][column] == 9){										// Check the 0th row next 
				modified[0][column] = token;
				Simacogo2 move = new Simacogo2(modified, gameScore, (byte) 0, (byte) 0);			// Place token
				moves.add(move); 
				continue;
			}
			if (board[4][column] == 9){										// Open space is between 1-4 search index 1-4 
				for (short row = 1; row < 5; row++){
					if (board[row][column] == 9){								// Generate move in first empty space
						modified[row][column] = token;
						Simacogo2 move = new Simacogo2(modified, gameScore, (byte) row, (byte) column); 						   
						moves.add(move);
						break;
					}
				}
			}
			else{													// Open space is between 5-8 search index 5-8 
				for (short row = 5; row < 9; row++){													
					if (board[row][column] == 9){								// Generate move in first empty space
						modified[row][column] = token;
						Simacogo2 move = new Simacogo2(modified, gameScore, (byte) row, (byte) column);							   
						moves.add(move);
						break;
					}
				}
			}
		}
		return moves;
	}

	
	
	// ** Search for Best Move**
	public static Simacogo2 selectAiMove (Simacogo2 gameState, byte depth, byte endGame){					// Calculates best move for the AI player, considering a number of turns = depth
		if (depth + endGame >=  81){											// Cap search depth if it would go beyond the 81st move
			depth = (byte) (81 - endGame);
		}
		
		short bestValue = -32000;
		Simacogo2 bestMove = null;
		LinkedList <Simacogo2> moves = getMoves(gameState, true);

		
		while (moves.size() > 0){
			Simacogo2 move = moves.pop();
			short moveValue = alphaBeta(move, (byte) (depth -1), 
						Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
			bestValue = (short) Math.max(moveValue, bestValue);
			if (bestValue == moveValue){
				bestMove = move;
			}
		}
		return bestMove;
	}
	
	
	
	public static short alphaBeta(Simacogo2 gameState, byte depth, double alpha, double beta, boolean aiPlayer){							
								  
		if (depth <= 0){
			return gameState.SCORE;
			
		} else {
			LinkedList <Simacogo2> moves = getMoves(gameState, aiPlayer);

			
			if (aiPlayer == true){
				while (moves.size() > 0){
					Simacogo2 move = moves.pop();
					alpha = Math.max(alpha, alphaBeta(move, (byte) (depth -1), alpha, beta, false));  	// Choose the best option the minimizing player would allow
							                          					
					if (beta <= alpha){
						break;
					}
				}
				return (short) alpha;
			} else {
				while (moves.size() > 0){
					Simacogo2 move = moves.pop();
					beta = Math.min(beta, alphaBeta(move, (byte) (depth -1), alpha, beta, true));		// Choose the best option the maximizing player would allow
													
					if (beta <= alpha){
						break;
					}
				}
				return (short) beta;
			}
		}
	}
	
	
	
	// ** Human Turn Handlers**
	
	// ** Determines if the human players move is legally allowed
	public static boolean moveIsLegal (Simacogo2 gameState, byte row, byte column){
		if (row > 8 || column > 8 || row < 0 || column < 0){								// Move is off the board, this move isn't legal
			return false;
		}
		if (gameState.CURRSTATE[row][column] != 9) {									// A token is here, this move isn't legal
			return false;
		} 
		if (row != 0 && gameState.CURRSTATE[row-1][column] == 9){  							// The space below is empty, this move isn't legal
			return false;
		}
		return true;
	}
	
	
	
	// ** Makes legal move or returns old move **
	public static Simacogo2 takeTurn (Simacogo2 gameState, byte row, byte column){
		if (moveIsLegal(gameState, row, column)){									// If the move is legal, make the move
			gameState.CURRSTATE[row][column] = 0; 
		}
		Simacogo2 move = new Simacogo2 (gameState.CURRSTATE, gameState.SCORE, row, column);				// Create and return new game state
		return move;
	}
	
	
	
	// ** Creates starting game board **
	static byte[][] setup(){
		byte [][] state = new byte[9][9];
		for (short i = 0; i < 9; i++){
			for (short j = 0; j < 9; j++){
				state[i][j] = 9;
			}
		}
		return state;
	}
	
	
	
	
	
	public static void main(String[] args) {
		// ** Initialize Game **
		Scanner scanner = new Scanner (System.in);
		byte endGame = 0;
		byte[][] state = setup();
		Simacogo2 start = new Simacogo2(state, (byte) 0, (byte) 0, (byte) 0);						
		start.SCORE = 0;												// Removes incorrect points generated during creation
		Simacogo2 currentGame = start;
		boolean aiPlayer = false;
		boolean humanTurnOver = false;
	
		
		
		// ** Set difficulty of AI **
		System.out.println("How many steps ahead should the computer look? ");
		byte depth = scanner.nextByte();
	
	
	
		//** Main Game Loop **
		System.out.println("---------------------------------------------------");
		while (endGame < 81){
			System.out.println("---------------------------------------------------");
			System.out.println("Move #: " + endGame);
			System.out.println("The current score is: " + currentGame.SCORE);	
			currentGame.printBoard();										// Display current board
			
			// ** Humans Turn **
			if (aiPlayer == false){											// Humans Turn
				System.out.println("Please enter the column of your move: ");
				byte column = scanner.nextByte();
				System.out.println("Please enter the row of your move: ");
				byte row = scanner.nextByte();
			

				while (humanTurnOver == false){													
					if (moveIsLegal(currentGame, row, column)){						// If the move is legal then make it so...Picard Style		
						Simacogo2 move = takeTurn(currentGame, row, column);				// Enacts the players choice of move
						currentGame = move;
						humanTurnOver = true;								// Human player is done
						aiPlayer = true;								// It's now the AI's turn
						System.out.println("---------------------------------------------------");
					} else {										// Human player gave an illegal move make them pick again
						System.out.println("That move is unavailable please choose again.");
						System.out.println("Please enter the column of your move: ");
						column = scanner.nextByte();
						System.out.println("Please enter the row of your move: ");
						row = scanner.nextByte();
					}
				}
			// ** AIs Turn **
			} else {												// AIs Turn
				long startTime = System.currentTimeMillis();							// Starting timer
				if ((endGame + depth) >= 81){									// Prevents AI from looking for moves that can't exist
					depth = (byte) (81 - endGame);
				}
				Simacogo2 aiMove = selectAiMove(currentGame, depth, endGame);					// Get AI's move
				currentGame = aiMove;										// Make AI's move
				aiPlayer = false;										// aiPlayer's turn is over
				humanTurnOver = false;										// Prepare for human turn
				long elapsedTime = (new Date()).getTime() - startTime;						// Calculate time of search
				System.out.println("AI Time to select move: " + elapsedTime / 1000 + " sec");			// Print out the time taken
				System.out.println("---------------------------------------------------");
			}					
			endGame++;
		}
	
		
		
		// ** Win Lose or Draw **	
		currentGame.printBoard();
		if (currentGame.SCORE < 0){											// Tell the player whether they win,lose, or tie 
			System.out.println("You WIN!");
		} else if (currentGame.SCORE == 0){
			System.out.println("You TIED!");
		} else {
			System.out.println("The mighty computer has defeated you mwahahaha!");
		}
		scanner.close();
	}
}
