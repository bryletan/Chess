package model;

/**
 * Class for a bishop piece, which is a subclass of a Piece object.
 * 
 * @author  Bryle Tan
 */
public class Bishop extends Piece {

    /**
     * Constructor for a bishop object with a color.
     * 
     * @param color string representing the color of the bishop.
     * 
     * @see Piece for color format.
     */
    public Bishop(String color) {
        super(color);
        type = "bishop";
    }
}
