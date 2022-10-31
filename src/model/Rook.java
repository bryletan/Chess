package model;

/**
 * Class for a rook piece, which is a subclass of a Piece object.
 * 
 * @author  Bryle Tan
 */
public class Rook extends Piece {

    /**
     * Constructor for a rook object with a color.
     * 
     * @param color string representing the color of the rook.
     * 
     * @see Piece for color format.
     */
    public Rook(String color) {
        super(color);
        type = "rook";
    }
}
