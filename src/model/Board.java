package model;

/**
 * Class for a board, which is a 2D array of Square objects.
 * 
 * @author  Bryle Tan
 */
public class Board {

    /**
     * 2D Square array that represents the board.
     */
    public Square[][] board;

    /**
     * Constructor for a board.
     * 
     * @see Square for square object.
     */
    public Board() {
        board = new Square[8][8];
        
        int file = 0; //column
        int rank = 0; //row

        //initialize squares with no pieces
        board[0][5] = new Square("white");
        board[2][5] = new Square("white");
        board[4][5] = new Square("white");
        board[6][5] = new Square("white");
        board[1][4] = new Square("white");
        board[3][4] = new Square("white");
        board[5][4] = new Square("white");
        board[7][4] = new Square("white");
        board[0][3] = new Square("white");
        board[2][3] = new Square("white");
        board[4][3] = new Square("white");
        board[6][3] = new Square("white");
        board[1][2] = new Square("white");
        board[3][2] = new Square("white");
        board[5][2] = new Square("white");
        board[7][2] = new Square("white");

        board[1][5] = new Square("black");
        board[3][5] = new Square("black");
        board[5][5] = new Square("black");
        board[7][5] = new Square("black");
        board[0][4] = new Square("black");
        board[2][4] = new Square("black");
        board[4][4] = new Square("black");
        board[6][4] = new Square("black");
        board[1][3] = new Square("black");
        board[3][3] = new Square("black");
        board[5][3] = new Square("black");
        board[7][3] = new Square("black");
        board[0][2] = new Square("black");
        board[2][2] = new Square("black");
        board[4][2] = new Square("black");
        board[6][2] = new Square("black");

        //initialize non-pawn white pieces 
        board[0][0] = new Square(new Rook("white"),"black"); 
		board[1][0] = new Square(new Knight("white"),"white");
        board[2][0] = new Square(new Bishop("white"),"black");
        board[3][0] = new Square(new Queen("white"),"white");
        board[4][0] = new Square(new King("white"),"black");
        board[5][0] = new Square(new Bishop("white"),"white");
		board[6][0] = new Square(new Knight("white"),"black");
        board[7][0] = new Square(new Rook("white"),"white");

        //initialize white pawns
        rank = 1;
        for(file = 0; file < 8; file ++) {
            if (file % 2 == 0) {
                board[file][rank] = new Square(new Pawn("white"), "white");
            }
            else {
                board[file][rank] = new Square(new Pawn("white"), "black");
            }
        }

        //initialize non-pawn black pieces 
        board[0][7] = new Square(new Rook("black"),"white");
		board[1][7] = new Square(new Knight("black"),"black");
        board[2][7] = new Square(new Bishop("black"),"white");
        board[3][7] = new Square(new Queen("black"),"black");
        board[4][7] = new Square(new King("black"),"white");
        board[5][7] = new Square(new Bishop("black"),"black");
		board[6][7] = new Square(new Knight("black"),"white");
        board[7][7] = new Square(new Rook("black"),"black");

        //initialize black pawns
        rank = 6;
        for(file = 0; file < 8; file ++) {
            if (file % 2 == 0) {
                board[file][rank] = new Square(new Pawn("black"), "black");
            }
            else {
                board[file][rank] = new Square(new Pawn("black"), "white");
            }
        }

    }

    /**
     * Prints the board on the terminal.
     * 
     * @author          Bryle Tan
     */
    public void printBoard() {
        int file = 0; //column
        int rank = 0; //row

        //Creating the checkerboard pattern on the board
        for(rank = 7; rank >= 0; rank--) {
			for(file = 0; file < 8; file++) {

                //If there is a piece in on the board, print out the piece character
				if(board[file][rank].getPiece() != null) {
					System.out.print(board[file][rank] + " ");
				}

				else {
					if(board[file][rank].isSquareBlack())
						System.out.print("## ");
					else
						System.out.print("   ");
				}
				
			}
			System.out.println(" " + (rank+1));
		}
		System.out.println(" a  b  c  d  e  f  g  h");
		System.out.println();
    }

}
