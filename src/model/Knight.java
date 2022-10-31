package model;

/**
 * Class for a knight piece, which is a subclass of Piece.
 * 
 * @author  Bryle Tan
 */
public class Knight extends Piece {

    /**
     * Constructor for a knight object with a color.
     * 
     * @param color string representing the color of the knight.
     * 
     * @see Piece for color format.
     */
    public Knight(String color) {
        super(color);
        type = "knight";
    }
}
