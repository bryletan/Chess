package model;

/**
 * Parent class for all pieces in the game.
 * 
 * @author Bryle Tan
 */
public class Piece {

    /**
     * boolean to keep track of the piece's color.
     */
    public boolean isWhite;

    /**
     * string that represents the type of the piece.
     */
    public String type;

    /**
     * boolean to keep track of if the piece has moved or not.
     */
    public boolean hasMoved;

    /**
     * boolean to keep track of the piece's ability to be captured in an
     * enpassant move.
     */
    public boolean enpassant;

    /**
     * Constructor for a piece object.
     * 
     * @param color string representing the color of the piece.
     *              "white" if the piece belongs to the white team, 
     *              "black" if it belongs to the black team.
     */
    public Piece(String color) {
        isWhite = color.equals("white");
        type = null;
        hasMoved = false;
        enpassant = false;
    }

    /**
     * getter method for this piece's color.
     * 
     * @return  <code>true</code> if the piece is white,
     *          <code>false</code> if the piece is black.
     */
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * getter method for this piece's hasMoved attribute.
     * 
     * @return  <code>true</code> if the piece has been moved in the chess game,
     *          <code>false</code> if the piece has not moved.
     */
    public boolean hasMoved() {
        return hasMoved;
    }

    /**
     * setter method for this piece's hasMoved attribute. Called when a piece has been moved
     * from it's original position.
     */
    public void moved() {
        hasMoved = true;
    }

    /**
     * getter method for this piece's type.
     * 
     * @return  string representation of the piece's type.
     *          <code>null</code> if the piece does not have a type.
     */
    public String getType() {
        return this.type;
    }

    /**
     * returns the piece's color and its type in a string.
     * 
     * @return string representation of the piece.
     */
    public String toString() {
        if(this.isWhite()) {
            return "white " + type;
        }

        return "black " + type;
    }
}
