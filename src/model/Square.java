package model;

/**
 * Square object for each square on the board.
 * 
 * @author  Bryle Tan
 */
public class Square {

    /**
     * boolean to keep track of the color of the square.
     * <code>true</code> if the square is black.
     * <code>false</code> if the square is white.
     */
    public boolean isBlack;

    /**
     * Piece object representing the piece that is situated on the square.
     * <code>null</code> if there is no piece on this square.
     */
    public Piece currPiece;
    
    /**
     * Constructor for a square object without a piece.
     * 
     * @param color string representation of the color of the square.
     *              "black" if the square is a black square,
     *              "white" if the square is a white square.
     */
    public Square(String color) {
        currPiece = null;
        isBlack = color.equals("black");
    }

    /**
     * Constructor for a square object with a piece on it.
     * 
     * @param piece Piece object that is on the square
     * @param color string representation of the color of the square.
     */
    public Square(Piece piece, String color) {
        currPiece = piece;
        isBlack = color.equals("black");
    }

    /**
     * setter method for the piece on the square.
     * 
     * @param piece new piece object for the square.
     *              <code>null</code> if there is no piece on the square.
     */
    public void setPiece(Piece piece) {
        currPiece = piece;
    }

    /**
     * getter method for the current piece on the square.
     * 
     * @return  Piece object that is currently on the square.
     */
    public Piece getPiece() {
        return currPiece;
    }
    
    /**
     * getter method for the current piece's type.
     * 
     * @return  string representation of the piece's type,
     *          "" if there is no piece on the square.
     */
    public String getType() {
        if(currPiece.type == "rook") {
            return "R";
        }
        else if(currPiece.type == "pawn") {
            return "P";
        }
        else if(currPiece.type == "knight") {
            return "N";
        }
        else if(currPiece.type == "bishop") {
            return "B";
        }
        else if(currPiece.type == "king") {
            return "K";
        }
        else if(currPiece.type == "queen") {
            return "Q";
        }
        else {
            return "";
        }
    }

    /**
     * getter method for the square's piece.
     * 
     * @return  "w" if the square is white,
     *          "b" if the square is black.
     */
    public String getColor() {
        if(currPiece.isWhite) {
            return "w";
        }
        else {
            return "b";
        }
    }

    /**
     * getter method for the square's color.
     * 
     * @return  <code>true</code> if the square is a black square,
     *          <code>false</code> if the square is a white square.
     */
    public boolean isSquareBlack() {
        return isBlack;
    }

    /**
     * @return  string representation of the square object.
     *          returns the color and the type of the piece.
     */
    public String toString() {
        return getColor() + getType();
    }
    
}
