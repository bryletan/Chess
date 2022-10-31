package model;

/**
 * Class for a king piece, which is a subclass of Piece.
 * 
 * @author  Bryle Tan
 */
public class King extends Piece {

    /**
     * Constructor for a king piece with a color.
     * 
     * @param color string representing the color of the king.
     * 
     * @see Piece for color format.
     */
    public King(String color) {
        super(color);
        type = "king";
    }
}
