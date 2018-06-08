package simacogo;

public class Simacogo2{
	byte [][] CURRSTATE;												// Area of Play
	byte PARENTSCORE;												// Parents Score
	byte SCORE;													// Stores the evaluated score. AI wants positive values and human wants negative values
	byte ROW;													// Stores row of move taken to get to this game state
	byte COL;													// Stores column of move taken to get to this game state



	
	Simacogo2(byte gameState [][], byte parentscore, byte moveRow, byte moveCol){					// Constructor
		this.CURRSTATE 		= gameState;
		this.ROW		= moveRow;
		this.COL		= moveCol;
		this.PARENTSCORE	= parentscore;
		this.SCORE		= updateScore();
		
	}
		

	
	// ** Print out Current State of Board ** 
	
	void printBoard(){
		System.out.println(" 0  1  2  3  4  5  6  7  8 ");
		for (int row = 8; row >= 0 ; row--){
			for (int column = 0; column < 9; column++){
				System.out.print('|');
				if(CURRSTATE[row][column] == 9){
					System.out.print('_');
				} else {
					System.out.print(CURRSTATE[row][column]);
				}
				System.out.print('|');
			}
			System.out.print(" ");
			System.out.println(row);
		}
		System.out.println();
	}
	
	
	
	// ** Methods for Scoring **  super ugly but fast -- if I get the time I will clean this up  
	
	byte updateScore (){												// Combines points earned by this moves with the score of the game
		return (byte) (PARENTSCORE + getMoveScore());
	}
	
	byte getMoveScore (){
		byte mScore = 0;											// Adjacent moves gain 2 points, diagonal moves gain 1 point
		int token = CURRSTATE[ROW][COL];									// Current players token
		
		
		if (ROW > 0 && ROW < 8 && COL > 0 && COL < 8){								// Move is not an edge tile so we can look at all adjacent spaces
			if (CURRSTATE[ROW -1][COL]	 	== token){ mScore = (byte) (mScore + 2); }				
			if (CURRSTATE[ROW][COL -1] 		== token){ mScore = (byte) (mScore + 2); }			
			if (CURRSTATE[ROW][COL +1] 		== token){ mScore = (byte) (mScore + 2); }			
			if (CURRSTATE[ROW -1][COL -1] 		== token){ mScore = (byte) (mScore + 1); }			
			if (CURRSTATE[ROW +1][COL +1] 		== token){ mScore = (byte) (mScore + 1); }			
			if (CURRSTATE[ROW -1][COL +1] 		== token){ mScore = (byte) (mScore + 1); }			
			if (CURRSTATE[ROW +1][COL -1] 		== token){ mScore = (byte) (mScore + 1); }	
			if (token == 1){ return mScore;}       else{ return (byte) (0 - mScore); }
			
		}
		else if (ROW == 0 && COL != 0 && COL != 8){								// Move is the bottom row but not in the corners
			if (CURRSTATE[ROW][COL -1] 		== token){ mScore = (byte) (mScore + 2); }			
			if (CURRSTATE[ROW][COL +1] 		== token){ mScore = (byte) (mScore + 2); }			
			if (CURRSTATE[ROW +1][COL +1] 		== token){ mScore = (byte) (mScore + 1); }			
			if (CURRSTATE[ROW +1][COL -1] 		== token){ mScore = (byte) (mScore + 1); }		
			if (token == 1){ return mScore;}       else{ return (byte) (0 - mScore); }
		}
		else if (ROW == 0 && COL == 0){										// Move is bottom left corner		
			if (CURRSTATE[ROW][COL +1] 		== token){ mScore = (byte) (mScore + 2); }			
			if (CURRSTATE[ROW +1][COL +1] 		== token){ mScore = (byte) (mScore + 1); }			
			if (token == 1){ return mScore;}       else{ return (byte) (0 - mScore); }
		}
		else if (ROW == 0 && COL == 8){										// Move is bottom right corner
			if (CURRSTATE[ROW][COL -1] 		== token){ mScore = (byte) (mScore + 2); }					
			if (CURRSTATE[ROW +1][COL -1] 		== token){ mScore = (byte) (mScore + 1); }			
			if (token == 1){ return mScore;}       else{ return (byte) (0 - mScore); }
		}
		else if (ROW == 8 && COL != 0 && COL != 8){								// Move is in top row but not in a corner
			if (CURRSTATE[ROW -1][COL] 		== token){ mScore = (byte) (mScore + 2); }			
			if (CURRSTATE[ROW][COL -1] 		== token){ mScore = (byte) (mScore + 2); }			
			if (CURRSTATE[ROW][COL +1] 		== token){ mScore = (byte) (mScore + 2); }			
			if (CURRSTATE[ROW -1][COL -1] 		== token){ mScore = (byte) (mScore + 1); }			
			if (CURRSTATE[ROW -1][COL +1] 		== token){ mScore = (byte) (mScore + 1); }			
			if (token == 1){ return mScore;}       else{ return (byte) (0 - mScore); }
		}
		else if (ROW == 8 && COL == 0){										// Move is in top left corner
			if (CURRSTATE[ROW][COL +1] 		== token){ mScore = (byte) (mScore + 2); }			
			if (CURRSTATE[ROW -1][COL]	 	== token){ mScore = (byte) (mScore + 2); }			 
			if (CURRSTATE[ROW -1][COL +1] 		== token){ mScore = (byte) (mScore + 1); }			
			if (token == 1){ return mScore;}       else{ return (byte) (0 - mScore); }
			}
		else if (ROW == 8 && COL == 8){										// Move is in top right corner
			if (CURRSTATE[ROW][COL -1] 		== token){ mScore = (byte) (mScore + 2); }			
			if (CURRSTATE[ROW -1][COL] 		== token){ mScore = (byte) (mScore + 2); }			 
			if (CURRSTATE[ROW -1][COL -1] 		== token){ mScore = (byte) (mScore + 1); }	
			if (token == 1){ return mScore;}       else{ return (byte) (0 - mScore); }
		}
		else if (ROW != 0 && ROW != 8 && COL == 0){								// Move is on left edge but not in a corner
			if (CURRSTATE[ROW -1][COL] 		== token){ mScore = (byte) (mScore + 2); }
			if (CURRSTATE[ROW][COL +1] 		== token){ mScore = (byte) (mScore + 2); }
			if (CURRSTATE[ROW +1][COL +1] 		== token){ mScore = (byte) (mScore + 1); }
			if (CURRSTATE[ROW -1][COL +1] 		== token){ mScore = (byte) (mScore + 1); }
			if (token == 1){ return mScore;}       else{ return (byte) (0 - mScore); }
		} else {												// Move is on the right edge but not in a corner
			if (CURRSTATE[ROW -1][COL] 		== token){ mScore = (byte) (mScore + 2); }
			if (CURRSTATE[ROW][COL -1] 		== token){ mScore = (byte) (mScore + 2); }
			if (CURRSTATE[ROW +1][COL -1] 		== token){ mScore = (byte) (mScore + 1); }
			if (CURRSTATE[ROW -1][COL -1] 		== token){ mScore = (byte) (mScore + 1); }
			if (token == 1){ return mScore;}       else{ return (byte) (0 - mScore); }
		}
	}
	
	// ** Prettier version...but it doesn't work at this time...and I ran out of time to debug **
//	byte getMoveScore (){
//		byte mScore = 0;											// Adjacent moves gain 2 points, diagonal moves gain 1 point
//		int token = CURRSTATE[ROW][COL];									// Current players token
//		
//		// ** Move is in the interior **
//		if (ROW > 0 && ROW < 8 && COL > 0 && COL < 8){												
//			for (int i = ROW - 1; i < ROW + 2; i++){
//				for (int j = COL - 1; j < COL + 2; j++){
//					if (checkRedundant(i, j)) { continue; }
//					mScore = (byte) (mScore + pointsEarned(i, j, token));
//				}
//			}
//			return newScore(token, mScore);
//		}
//		
//		// ** Move is in bottom row but not in the corners
//		else if (ROW == 0 && COL != 0 && COL != 8){
//			for (int i = ROW; i < ROW + 2; i++){
//				for (int j = COL - 1; j < COL + 2; j++){
//					if (checkRedundant(i, j)) { continue; }
//					mScore = (byte) (mScore + pointsEarned(i, j, token));
//				}
//			}
//			return newScore(token, mScore); 
//		}
//
//		// ** Move is in bottom left corner **
//		else if (ROW == 0 && COL == 0){
//			for (int i = ROW; i < ROW + 2; i++){
//				if (checkRedundant(i, COL + 1)) { continue; }
//				mScore = (byte) (mScore + pointsEarned(i, COL + 1, token));
//			}
//			return newScore(token, mScore); 
//		}
//
//		// ** Move is in bottom right corner **
//		else if (ROW == 0 && COL == 8){
//			for (int i = ROW; i < ROW + 2; i++){
//				if (checkRedundant(i, COL - 1)) { continue; }
//				mScore = (byte) (mScore + pointsEarned(i, COL - 1, token));
//			}
//			return newScore(token, mScore); 
//		}
//
//		// ** Move is in top row but not a corner **
//		else if (ROW == 8 && COL != 0 && COL != 8){
//			for (int i = ROW - 1; i < ROW + 1; i++){
//				for (int j = COL - 1; j < COL + 2; j++){
//					if (checkRedundant(i, j)) { continue; }
//					mScore = (byte) (mScore + pointsEarned(i, j, token));
//				}
//			}
//			return newScore(token, mScore); 
//		}
//
//		// ** Move is in top left corner **
//		else if (ROW == 8 && COL == 0){
//			for (int i = ROW - 1; i < ROW + 1; i++){
//				for (int j = COL; j < COL + 2; j++){
//					if (checkRedundant(i, j)) { continue; }
//					mScore = (byte) (mScore + pointsEarned(i, j, token));
//				}
//			}
//			return newScore(token, mScore); 
//		}
//
//		// ** Move is in top right corner **
//		else if (ROW == 8 && COL == 8){
//			for (int i = ROW - 1; i < ROW + 1; i++){
//				for(int j = COL - 1; j < COL + 1; j++){
//					if (checkRedundant(i, j)) { continue; }
//					mScore = (byte) (mScore + pointsEarned(i, j, token));
//				}
//			}
//			return newScore(token, mScore);
//		}
//
//		// ** Move is on left edge but not in a corner **
//		else if (ROW != 0 && ROW != 8 && COL == 0){
//			for (int i = ROW - 1; i < ROW + 2; i++){
//				for (int j = COL; j < COL + 2; j++){
//					if (checkRedundant(i, j)) { continue; }
//					mScore += pointsEarned(i, j, token);
//				}
//			}
//			return newScore(token, mScore); 
//		}
//
//		// ** Move is on the right edge but not a corner
//		else {
//			for (int i = ROW - 1; i < ROW + 2; i++){
//				for (int j = COL - 1; j < COL +1; j++){
//					if (checkRedundant(i, j)) { continue; }
//					mScore = (byte) (mScore + pointsEarned(i, j, token));	
//				}
//			}
//			return newScore(token, mScore); 
//		}
//	}
//
//	
//	// ** Returns true if the tile shouldn't be scored **
//	boolean checkRedundant(int i, int j){
//		return (ROW == i && COL == j) || (ROW - 1 == i && COL == j);
//	}
//	
//	// ** Returns the total new score as positive for the AI or negative for the human **
//	byte newScore (int token, byte mScore){
//		if (token == 1){ 
//			return mScore; 
//		} else { 
//			return (byte) (0 - mScore); 
//		} 
//	}
//	
//	// ** Returns points earned by this move **
//	byte pointsEarned(int i, int j, int token){
//		byte points = 0;
//		if (CURRSTATE[i][j] == token){
//			if (Math.abs(i - ROW) == 1 && Math.abs(j - COL) == 1){
//				points = (byte) (points + 1);
//			} else {
//				points = (byte) (points + 2);
//			}
//		}
//		return points;
//	}
}
