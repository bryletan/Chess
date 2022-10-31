package model;

/**
 * Class for a queen piece, which is a subclass of a Piece object.
 * 
 * @author  Bryle Tan
 */
public class Queen extends Piece {

    /**
     * Constructor for a queen object with a color.
     * 
     * @param color string representing the color of the queen.
     * 
     * @see Piece for color format.
     */
    public Queen(String color) {
        super(color);
        type = "queen";
    }
}
