package model;

/**
 * Class for a pawn piece, which is a subclass of a Piece object.
 * 
 * @author  Bryle Tan
 */
public class Pawn extends Piece {

    /**
     * Constructor for a pawn object with a color.
     * 
     * @param color string representing the color of the pawn.
     * 
     * @see Piece for color format.
     */
    public Pawn(String color) {
        super(color);
        type = "pawn";
    }

    /**
     * Changes type of pawn to a queen, knight, bishop, or rook
     * 
     * @param input             the raw input string provided by the user within chess.java
     * @param board             the game board created within chess.java
     * @param isWhite           boolean that indicates if the pawn piece is white
     * @param file              file location of pawn
     * @param rank              rank location of pawn
     * 
     * @author                  Bryle Tan
     */
    public static void promote(String input, Square[][] board, boolean isWhite, int file, int rank) {
        String[] args = input.split(" ");

        String color = "";
        if(isWhite) {
            color = "white";
        } else {
            color = "black";
        }

        if(args.length == 3) {
            if(args[2].equals("Q")) {
                board[file][rank].setPiece(new Queen(color));
                return;
            }
            else if(args[2].equals("N")) {
                board[file][rank].setPiece(new Knight(color));
                return;
            }
            else if(args[2].equals("B")) {
                board[file][rank].setPiece(new Bishop(color));
                return;
            }
            else {
                board[file][rank].setPiece(new Rook(color));
                return;
            }
        }

        //if no 3rd argument is given, assume that the pawn is a queen
        board[file][rank].setPiece(new Queen(color));
    }
}
