package chess;
import java.util.HashSet;
import java.util.Map;
import model.*;

/**
 * This class handles move objects. Move objects are created when a user enters in a move,
 * and consist of the original position of the piece, and the ending position where the piece
 * is being moved to.
 * 
 * @author Maanas Pimplikar
 * @author Bryle Tan
 */
public class Move {
    /**
     * a Map object that converts a file to their corresponding row.
     */
    public static Map<String, Integer> fileToRow = Map.of(
        "a", 0,
        "b", 1,
        "c", 2,
        "d", 3,
        "e", 4,
        "f", 5,
        "g", 6,
        "h", 7
    );

    /**
     * a Map object that converts a row to their corresponding file.
     */
    public static Map<Integer, String> rowToFile = Map.of(
        0, "a",
        1, "b",
        2, "c",
        3, "d",
        4, "e",
        5, "f",
        6, "g",
        7, "h"
    );

    /**
     * string that represents the piece's original position.
     * Given in filerank notation.
     */
    private String originalPosition;

    /**
     * string that represents the piece's ending/goal position
     * Given in filerank notation.
     */
    private String endingPosition;

    /**
     * Board object to keep track of the current state of the game.
     */
    private Board board;

    /**
     * boolean to keep track of the team that attempted this turn.
     */
    private boolean whiteTurn;


    /**
     * Class constructor for a move object.
     * 
     * @param originalPosition  The string representing the position of the piece to be moved, in filerank notation.
     * @param endingPosition    The string representing the piece is moving to, in filerank notation.
     * @param board             The board object representing the chess board.
     * @param whiteTurn         A boolean, <code>true</code> if it's white team's turn, <code>false</code> if it's black team's turn.
     * 
     * @author                  Maanas Pimplikar
     */
    public Move(String originalPosition, String endingPosition, Board board, boolean whiteTurn) {
        this.originalPosition = originalPosition;
        this.endingPosition = endingPosition;
        this.board = board;
        this.whiteTurn = whiteTurn;
    }

    // getter methods

    /**
     * Getter method to get the starting position of the piece, in filerank notation.
     * 
     * @return  this move's original position.
     */
    public String getOriginalPosition() { return this.originalPosition; }

    /**
     * Getter method to get the position resulting from the move, in filerank notation.
     * 
     * @return  this move's ending position.
     */
    public String getEndingPosition() { return this.endingPosition; }

    /**
     * Getter method to get the board.
     * 
     * @return  board object representing the board.
     */
    public Board getBoard() { return this.board; }

    /**
     * Getter method to see which team is making this move.
     * 
     * @return  boolean representing this move's team. <code>True</code> if the white team made this move,
     *          <code>false</code> if the black team made this move.
     */
    public boolean isWhiteTurn() { return this.whiteTurn; }

    // setter methods
    /**
     * Sets the turn to the opposite team's turn.
     * If the white team made this move, then sets the move to the black team.
     * <p>
     * Used the most in <code>validateMove</code>.
     */
    public void setWhiteTurn() { this.whiteTurn = !this.whiteTurn; }

    /*
     * Utility methods
     */

    /**
     * Gets all possible moves that can be made for the current team by calling <code>getPossibleMoves</code>.
     * Gets all impossible moves, which are moves that put, or keep the king in check by calling <code>getImpossibleMoves</code>.
     * 
     * @return  string "invalid" if this object's move is an impossible move.
     *          string "check" if this object's move put the other team's king in check.
     *          string "checkmate" if this object's move put the other team in checkmate.
     *          string "valid" if neither check or checkmate, and the move is a valid move.
     * 
     * @author Maanas Pimplikar
     */
    public String validateMove() {
        HashSet<String> possibleMoves = getPossibleMoves(this.board);

        // after getting possible moves, make the move in an auxillary board, and then check the other team's moves
        // if the king's position is one of the ending squares in the other team's set, then that move is no longer valid
        
        HashSet<String> impossibleMoves = getImpossibleMoves(possibleMoves, this.board);

        // set difference to remove all impossible moves
        possibleMoves.removeAll(impossibleMoves);

        // The current move is valid if the set of possible moves contains this instance of move
        if(possibleMoves.contains(this.toString())) {
            
            // the move is correct, make the move
            int pieceRow = Move.fileToRow.get(this.originalPosition.substring(0, 1));
            int pieceCol = Integer.parseInt(this.originalPosition.substring(1)) - 1;

            int pieceEndingRow = Move.fileToRow.get(this.endingPosition.substring(0, 1));
            int pieceEndingCol = Integer.parseInt(this.endingPosition.substring(1)) - 1;

            Piece movingPiece = this.board.board[pieceRow][pieceCol].getPiece();
            this.board.board[pieceRow][pieceCol].setPiece(null);
            
            // CAN BE NULL
            Piece capturedPiece = this.board.board[pieceEndingRow][pieceEndingCol].getPiece();
            this.board.board[pieceEndingRow][pieceEndingCol].setPiece(movingPiece);

            // CHECKING IF IT RESULTS IN A CHECKMATE
            this.setWhiteTurn();
            HashSet<String> nextPossibleMoves = this.getPossibleMoves(this.board);
            HashSet<String> nextImpossibleMoves = this.getImpossibleMoves(nextPossibleMoves, this.board);
            nextPossibleMoves.removeAll(nextImpossibleMoves);

            if(nextPossibleMoves.size() == 0) {
                this.board.board[pieceRow][pieceCol].setPiece(movingPiece);
                this.board.board[pieceEndingRow][pieceEndingCol].setPiece(capturedPiece);
                return "checkmate";
            }
            this.setWhiteTurn();

            // CHECK IF YOU CAN NOW ATTACK THE OPPOSITE KING
            String otherKingPosition = this.getKingPosition(!this.isWhiteTurn());
            nextPossibleMoves = this.getPossibleMoves(this.board);

            for(String nextMove : nextPossibleMoves) {
                String ending = nextMove.substring(6);
                if(ending.equals(otherKingPosition)) {
                    // MEANS THEY'RE IN CHECK
                    this.board.board[pieceRow][pieceCol].setPiece(movingPiece);
                    this.board.board[pieceEndingRow][pieceEndingCol].setPiece(capturedPiece);
                    return "check";
                }
            }

            this.board.board[pieceRow][pieceCol].setPiece(movingPiece);
            this.board.board[pieceEndingRow][pieceEndingCol].setPiece(capturedPiece);

            return "valid";
        }

        // System.out.println("invalid move");
        return "invalid";
    }

    /*
     * generating moves for all the pieces
     */

    /**
     * Goes through all the possible moves and checks if they put the team's king in check after being made.
     * If the king is at check after the move is made, then it's added to a set of all other impossible moves.
     * 
     * @param possibleMoves     <code>HashSet</code> of strings containing all possible moves.
     * @param board             board object representing the board.
     * @return                  <code>HashSet</code> of strings containing all impossible moves.
     * 
     * @author                  Maanas Pimplikar
     */
    private HashSet<String> getImpossibleMoves(HashSet<String> possibleMoves, Board board) {
        HashSet<String> impossibleMoves = new HashSet<String>();

        for(String move : possibleMoves) {
            int pieceRow = Move.fileToRow.get(move.substring(0, 1));
            int pieceCol = Integer.parseInt(move.substring(1,2)) - 1;

            int pieceEndingRow = Move.fileToRow.get(move.substring(6, 7));
            int pieceEndingCol = Integer.parseInt(move.substring(7)) - 1;

            Piece movingPiece = this.board.board[pieceRow][pieceCol].getPiece();
            this.board.board[pieceRow][pieceCol].setPiece(null);

            // CAN BE NULL
            Piece capturedPiece = this.board.board[pieceEndingRow][pieceEndingCol].getPiece();
            this.board.board[pieceEndingRow][pieceEndingCol].setPiece(movingPiece);
            
            String kingPosition = getKingPosition(this.isWhiteTurn());
            
            // temporarily change the turn to the next person's
            this.setWhiteTurn();
            HashSet<String> nextPossibleMoves = getPossibleMoves(this.board);

            // string = "** to **"
            for(String nextMove : nextPossibleMoves) {
                String tempEnding = nextMove.substring(6);
                if(tempEnding.equals(kingPosition)) {
                    impossibleMoves.add(move);
                }
            }

            // revert back the changes
            this.board.board[pieceRow][pieceCol].setPiece(movingPiece);
            this.board.board[pieceEndingRow][pieceEndingCol].setPiece(capturedPiece);
            this.setWhiteTurn();
        }

        return impossibleMoves;
    }

    /**
     * Goes through all squares of the board, and gets the set of moves for each piece
     * that has the same color as this move's team. Calls methods <code>pawnMoves</code>,
     * <code>rookMoves</code>, <code>bishopMoves</code>, <code>knightMoves</code>, 
     * <code>queenMoves</code>, <code>kingMoves</code>.
     * <p>
     * 
     * The methods called make sure that the piece color matches this move's color.
     * 
     * @param board     board object representing the board.
     * @return          <code>HashSet</code> of string containing all possible moves.
     * 
     * @author          Maanas Pimplikar
     */
    private HashSet<String> getPossibleMoves(Board board) {
        HashSet<String> possibleMoves = new HashSet<String>();

        // go through all positions of the board
        for(int file = 0; file < 8; file++) {
            for(int rank = 0; rank < 8; rank++) {
                // check which type of piece is at the square
                if(board.board[file][rank].getPiece() != null) {
                    String type = board.board[file][rank].getType();
                    if(type.equals("P")) {
                        pawnMoves(file, rank, possibleMoves);
                    } else if(type.equals("R")) {
                        rookMoves(file, rank, possibleMoves);
                    } else if(type.equals("B")) {
                        bishopMoves(file, rank, possibleMoves);
                    } else if(type.equals("N")) {
                        knightMoves(file, rank, possibleMoves);
                    } else if(type.equals("Q")) {
                        queenMoves(file, rank, possibleMoves);
                    } else if(type.equals("K")) {
                        kingMoves(file, rank, possibleMoves);
                    }
                }
            }
        }

        return possibleMoves;
    }

    /**
     * Gets the position of the king for the given team.
     * 
     * @param white     boolean representing the color of the king.
     *                  <code>True</code> if the king is from the white team, <code>false</code> if black team's.
     * @return          string representing the location of the king, in filerank notation.
     * 
     * @author          Bryle Tan
     */
    private String getKingPosition(boolean white) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Square cur = this.board.board[i][j];
                if(cur.getPiece() != null && cur.getPiece().isWhite() == white && cur.getPiece().getType().equals("king")) {
                    return Move.rowToFile.get(i) + (j + 1);
                }
            }
        }

        return "";
    }

    /**
     * Checks the next possible square for the pawn located at <code>row</code>, <code>col</code>.
     * If the pawn is in the starting position, also checks for a double move, and takes care of captures and empassant as well.
     * 
     * @param row       row corresponding to the file of the piece.
     *                  The file to row conversions can be seen in the map <code>fileToRow</code>
     * @param col       col corresponding to the rank of the piece.
     * @param moves     String hashset of all possible moves, to which the method will add the possible pawn moves.
     * 
     * @author          Maanas Pimplikar
     */
    private void pawnMoves(int row, int col, HashSet<String> moves) {
        String position = Move.rowToFile.get(row) + (col + 1); // +1 since rows are 1-indexed
        boolean pieceIsWhite = this.board.board[row][col].getPiece().isWhite();
        // if it's white's turn to move
        if(this.whiteTurn && pieceIsWhite) {
            Square advanceOne = this.board.board[row][col+1];

            // null square means empty square
            // check for if the pawn can advance once
            if(col + 1 < 8 && advanceOne.getPiece() == null) {
                //left check
                String ending = Move.rowToFile.get(row) + (col + 1 + 1); // +1 since rows are 1-indexed
                moves.add(position + " to " + ending);

                // check for if the pawn can advance twice
                // only possible if the pawn is at the starting position
                // since the check is inside the previous if-statement, no need to check for blockage
                if(col + 2 < 8) {
                    Square advanceTwo = this.board.board[row][col+2];
                    if(col == 1 && advanceTwo.getPiece() == null) {
                        ending = Move.rowToFile.get(row) + (col + 2 + 1); // +1 since rows are 1-indexed
                        moves.add(position + " to " + ending);
                    }
                }
            }

            // checking for captures
            // a white pawn capture if theres a piece at row-1, col+1 or row+1, col+1
            if(row-1 >= 0 && col + 1 < 8) {
                Square upLeft = this.board.board[row-1][col+1];

                /*EMPASSANT CODE*/
                if(col == 4) {
                    Square left = this.board.board[row-1][col];
            
                    if(left.getPiece() != null && !left.getPiece().isWhite()) {
                        if(left.getPiece().enpassant == true) {
                            String ending = Move.rowToFile.get(row-1) + (col + 1 + 1);
                            moves.add(position + " to " + ending);
                        }
                    }
                }
                // if the piece is a black piece
                if(upLeft.getPiece() != null && !upLeft.getPiece().isWhite()) {
                    String ending = Move.rowToFile.get(row-1) + (col + 1 + 1);
                    moves.add(position + " to " + ending);
                }
            }

            if(row + 1 < 8 && col + 1 < 8) {
                Square upRight = this.board.board[row+1][col+1];

                /*EMPASSANT CODE*/
                if(col == 4) {
                    Square right = this.board.board[row+1][col];
            
                    if(right.getPiece() != null && !right.getPiece().isWhite()) {
                        if(right.getPiece().enpassant == true) {
                            String ending = Move.rowToFile.get(row+1) + (col + 1 + 1);
                            moves.add(position + " to " + ending);
                        }
                    }
                }
                if(upRight.getPiece() != null && !upRight.getPiece().isWhite()) {
                    String ending = Move.rowToFile.get(row+1) + (col + 1 + 1);
                    moves.add(position + " to " + ending);
                }
            }
        }
        // if it's black's turn 
        else if(!this.whiteTurn && !pieceIsWhite) {
            Square advanceOne = this.board.board[row][col-1];

            // null square means empty square
            // check for if the pawn can advance once
            if(col - 1 >= 0 && advanceOne.getPiece() == null) {
                String ending = Move.rowToFile.get(row) + (col - 1 + 1); // +1 since rows are 1-indexed
                moves.add(position + " to " + ending);

                // check for if the pawn can advance twice
                // only possible if the pawn is at the starting position
                // since the check is inside the previous if-statement, no need to check for blockage
                
                if(col - 2 >= 0) {
                    Square advanceTwo = this.board.board[row][col-2];
                    if(col == 6 && advanceTwo.getPiece() == null) {
                        ending = Move.rowToFile.get(row) + (col - 2 + 1); // +1 since rows are 1-indexed
                        moves.add(position + " to " + ending);
                    }
                }
            }

            // checking for captures
            // a black pawn capture if theres a piece at row-1, col-1 or row+1, col-1
            if(row-1 >= 0 && col - 1 >= 0) {
                Square downLeft = this.board.board[row-1][col-1];
                
                /*EMPASSANT CODE*/
                if(col == 3) {
                    Square left = this.board.board[row-1][col];
            
                    if(left.getPiece() != null && left.getPiece().isWhite()) {
                        if(left.getPiece().enpassant == true) {
                            String ending = Move.rowToFile.get(row-1) + (col - 1 + 1);
                            moves.add(position + " to " + ending);
                        }
                    }
                }
                
                // if the piece is a black piece
                if(downLeft.getPiece() != null && downLeft.getPiece().isWhite()) {
                    String ending = Move.rowToFile.get(row-1) + (col - 1 + 1);
                    moves.add(position + " to " + ending);
                }
            }

            if(row + 1 < 8 && col - 1 >= 0) {
                Square downRight = this.board.board[row+1][col-1];

                /*EMPASSANT CODE*/
                if(col == 3) {
                    Square right = this.board.board[row+1][col];

                    if(right.getPiece() != null && right.getPiece().isWhite()) {
                        if(right.getPiece().enpassant == true) {
                            String ending = Move.rowToFile.get(row+1) + (col - 1 + 1);
                            moves.add(position + " to " + ending);
                        }
                    }
                }
                
                if(downRight.getPiece() != null && downRight.getPiece().isWhite()) {
                    String ending = Move.rowToFile.get(row+1) + (col - 1 + 1);
                    moves.add(position + " to " + ending);
                }
            }
        }
    }

    /**
     * checks all horizontal and vertical moves for the rook located at <code>row</code>, <code>col</code>
     * 
     * @param row       row corressponding to the file of the piece
     * @param col       col corresponding to the rank of the piece
     * @param moves     String hashset of all possible moves.
     * 
     * @author          Maanas Pimplikar
     */
    private void rookMoves(int row, int col, HashSet<String> moves) {
        // if the rook at row, col is not the correct team's then stop the method
        // if its white's turn, black rooks cannot move, and vice versa
        boolean pieceIsWhite = this.board.board[row][col].getPiece().isWhite();
        if((pieceIsWhite && !this.whiteTurn) || (!pieceIsWhite && this.whiteTurn)) {
            return;
        }

        String position = Move.rowToFile.get(row) + (col + 1);

        // rooks can move all four directions
        // go thru all directions until they are either off the grid or occupied

        // going left
        for(int i = row - 1; i >= 0; i--) {
            Square cur = this.board.board[i][col];

            // if the square is occupied, then break the loop as rook cant move past the piece
            if(cur.getPiece() != null) {
                // if it's the other team's, then it can be captured
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.isWhiteTurn()) || !pieceIsWhite && this.isWhiteTurn()) {
                    String ending = Move.rowToFile.get(i) + (col + 1);
                    moves.add(position + " to " + ending);
                }
                break;
            }

            String ending = Move.rowToFile.get(i) + (col + 1);
            moves.add(position + " to " + ending);
        }

        // going right
        for(int i = row + 1; i < 8; i++) {
            Square cur = this.board.board[i][col];

            // if piece is on the sqaure, break
            // add to moves if it can be captured
            if(cur.getPiece() != null) {
                // if it's the other team's, then it can be captured
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.isWhiteTurn()) || !pieceIsWhite && this.isWhiteTurn()) {
                    String ending = Move.rowToFile.get(i) + (col + 1);
                    moves.add(position + " to " + ending);
                }
                break;
            }

            String ending = Move.rowToFile.get(i) + (col + 1);
            moves.add(position + " to " + ending);
        }

        // going up
        for(int i = col + 1; i < 8; i++) {
            Square cur = this.board.board[row][i];

            // if the square is occupied, then break the loop as rook cant move past the piece
            // if piece is on the sqaure, break
            // add to moves if it can be captured
            if(cur.getPiece() != null) {
                // if it's the other team's, then it can be captured
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.isWhiteTurn()) || !pieceIsWhite && this.isWhiteTurn()) {
                    String ending = Move.rowToFile.get(row) + (i + 1);
                    moves.add(position + " to " + ending);
                }
                break;
            }

            String ending = Move.rowToFile.get(row) + (i + 1);
            moves.add(position + " to " + ending);
        }

        for(int i = col - 1; i >= 0; i--) {
            Square cur = this.board.board[row][i];

            // if piece is on the sqaure, break
            // add to moves if it can be captured
            if(cur.getPiece() != null) {
                // if it's the other team's, then it can be captured
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.isWhiteTurn()) || !pieceIsWhite && this.isWhiteTurn()) {
                    String ending = Move.rowToFile.get(row) + (i + 1);
                    moves.add(position + " to " + ending);
                }
                break;
            }

            String ending = Move.rowToFile.get(row) + (i + 1);
            moves.add(position + " to " + ending);
        }
    }

    /**
     * checks all diagonal moves for the bishop located at <code>row</code>, <code>col</code>
     * 
     * @param row       row corressponding to the file of the piece
     * @param col       col corresponding to the rank of the piece
     * @param moves     String hashset of all possible moves.
     * 
     * @author          Maanas Pimplikar
     */
    private void bishopMoves(int row, int col, HashSet<String> moves) {

        // making sure the piece is only moved if it's the color's turn
        boolean pieceIsWhite = this.board.board[row][col].getPiece().isWhite();
        if((pieceIsWhite && !this.whiteTurn) || (!pieceIsWhite && this.whiteTurn)) {
            return;
        }

        String position = Move.rowToFile.get(row) + (col + 1);

        // rooks can move diagonally to the up-left, up-right, down-left and down-right

        // checking the up left diagonal
        for(int i = row-1, j = col+1; i >= 0 && j < 8; i--, j++) {
            Square cur = this.board.board[i][j];
            
            // if piece is on the sqaure, break
            // add to moves if it can be captured
            if(cur.getPiece() != null) {
                // if it's the other team's, then it can be captured
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.isWhiteTurn()) || !pieceIsWhite && this.isWhiteTurn()) {
                    String ending = Move.rowToFile.get(i) + (j + 1);
                    moves.add(position + " to " + ending);
                }
                break;
            }

            String ending = Move.rowToFile.get(i) + (j + 1);
            moves.add(position + " to " + ending);
        }

        // up right diagonal
        for(int i = row+1, j = col+1; i < 8 && j < 8; i++, j++) {
            Square cur = this.board.board[i][j];
            
            // if piece is on the sqaure, break
            // add to moves if it can be captured
            if(cur.getPiece() != null) {
                // if it's the other team's, then it can be captured
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.isWhiteTurn()) || !pieceIsWhite && this.isWhiteTurn()) {
                    String ending = Move.rowToFile.get(i) + (j + 1);
                    moves.add(position + " to " + ending);
                }
                break;
            }

            String ending = Move.rowToFile.get(i) + (j + 1);
            moves.add(position + " to " + ending);
        }

        // bottom left diagonal
        for(int i = row-1, j = col-1; i >= 0 && j >= 0; i--, j--) {
            Square cur = this.board.board[i][j];
            
            // if piece is on the sqaure, break
            // add to moves if it can be captured
            if(cur.getPiece() != null) {
                // if it's the other team's, then it can be captured
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.isWhiteTurn()) || !pieceIsWhite && this.isWhiteTurn()) {
                    String ending = Move.rowToFile.get(i) + (j + 1);
                    moves.add(position + " to " + ending);
                }
                break;
            }

            String ending = Move.rowToFile.get(i) + (j + 1);
            moves.add(position + " to " + ending);
        }

        // bottom right diagonal
        for(int i = row+1, j = col-1; i < 8 && j >= 0; i++, j--) {
            Square cur = this.board.board[i][j];
            
            // if piece is on the sqaure, break
            // add to moves if it can be captured
            if(cur.getPiece() != null) {
                // if it's the other team's, then it can be captured
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.isWhiteTurn()) || !pieceIsWhite && this.isWhiteTurn()) {
                    String ending = Move.rowToFile.get(i) + (j + 1);
                    moves.add(position + " to " + ending);
                }
                break;
            }

            String ending = Move.rowToFile.get(i) + (j + 1);
            moves.add(position + " to " + ending);
        }
    }

    /**
     * checks all moves for the knight located at <code>row</code>, <code>col</code>
     * 
     * @param row       row corressponding to the file of the piece
     * @param col       col corresponding to the rank of the piece
     * @param moves     String hashset of all possible moves.
     * 
     * @author          Bryle Tan
     */
    private void knightMoves(int row, int col, HashSet<String> moves) {
        String position = Move.rowToFile.get(row) + (col + 1);

        boolean pieceIsWhite = this.board.board[row][col].getPiece().isWhite();
        if((pieceIsWhite && !this.whiteTurn) || (!pieceIsWhite && this.whiteTurn)) {
            return;
        }
            
        // up two and right one
        if(col + 2 < 8 && row + 1 < 8) {
            if(this.board.board[row + 1][col + 2].getPiece() != null) {
                pieceIsWhite = this.board.board[row + 1][col + 2].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row + 1) + (col + 2 + 1));
                }
            }

            if(this.board.board[row + 1][col + 2].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row + 1) + (col + 2 + 1));
            }
        }

        // up one and right two
        if(col + 1 < 8 && row + 2 < 8) {
            if(this.board.board[row + 2][col + 1].getPiece() != null) {
                pieceIsWhite = this.board.board[row + 2][col + 1].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row + 2) + (col + 1 + 1));
                }
            }
            if(this.board.board[row + 2][col + 1].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row + 2) + (col + 1 + 1));
            }
        }

        // down one and right two
        if(col - 1 >= 0 && row + 2 < 8) {
            if(this.board.board[row + 2][col - 1].getPiece() != null) {
                pieceIsWhite = this.board.board[row + 2][col - 1].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row + 2) + (col - 1 + 1));
                }
            }
            if(this.board.board[row + 2][col - 1].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row + 2) + (col - 1 + 1));
            }
        }

        // down two and right one
        if(col - 2 >= 0 && row + 1 < 8) {
            if(this.board.board[row + 1][col - 2].getPiece() != null) {
                pieceIsWhite = this.board.board[row + 1][col - 2].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row + 1) + (col - 2 + 1));
                }
            }
            if(this.board.board[row + 1][col - 2].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row + 1) + (col - 2 + 1));
            }
        }

        // down two and left one
        if(col - 2 >= 0 && row - 1 >= 0) {
            if(this.board.board[row - 1][col - 2].getPiece() != null) {
                pieceIsWhite = this.board.board[row - 1][col - 2].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row - 1) + (col - 2 + 1));
                }
            }
            if(this.board.board[row - 1][col - 2].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row - 1) + (col - 2 + 1));
            }
        }

        // down one and left two
        if(col - 1 >= 0 && row - 2 >= 0) {
            if(this.board.board[row - 2][col - 1].getPiece() != null) {
                pieceIsWhite = this.board.board[row - 2][col - 1].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row - 2) + (col - 1 + 1));
                }
            }
            if(this.board.board[row - 2][col - 1].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row - 2) + (col - 1 + 1));
            }
        }

        // up one and left two
        if(col + 1 < 8 && row - 2 >= 0) {
            if(this.board.board[row - 2][col + 1].getPiece() != null) {
                pieceIsWhite = this.board.board[row - 2][col + 1].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row - 2) + (col + 1 + 1));
                }
            }
            if(this.board.board[row - 2][col + 1].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row - 2) + (col + 1 + 1));
            }
        }

        // up two and left one
        if(col + 2 < 8 && row - 1 >= 0) {
            if(this.board.board[row - 1][col + 2].getPiece() != null) {
                pieceIsWhite = this.board.board[row - 1][col + 2].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row - 1) + (col + 2 + 1));
                }
            }
            if(this.board.board[row - 1][col + 2].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row - 1) + (col + 2 + 1));
            }
        }
    } 

    /**
     * checks all horizontal, vertical and diagonal moves for the queen located at <code>row</code>, <code>col</code>
     * 
     * @param row       row corressponding to the file of the piece
     * @param col       col corresponding to the rank of the piece
     * @param moves     String hashset of all possible moves.
     * 
     * @author          Maanas Pimplikar
     */
    private void queenMoves(int row, int col, HashSet<String> moves) {
        // queen can move all directtions (diagonal, horizontal and vertical)

        // check if it's the right team's piece
        boolean pieceIsWhite = this.board.board[row][col].getPiece().isWhite();
        if((pieceIsWhite && !this.whiteTurn) || (!pieceIsWhite && this.whiteTurn)) {
            return;
        }

        String position = Move.rowToFile.get(row) + (col + 1);

        // VERTICAL MOVES
        // going up
        for(int i = col + 1; i < 8; i++) {
            Square cur = this.board.board[row][i];

            if(cur.getPiece() != null) {
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.whiteTurn) || (!pieceIsWhite && this.whiteTurn)) {
                    String ending = Move.rowToFile.get(row) + (i + 1);
                    moves.add(position + " to " + ending);
                }

                break;
            }

            String ending = Move.rowToFile.get(row) + (i + 1);
            moves.add(position + " to " + ending);
        }

        // going down
        for(int i = col - 1; i >= 0; i--) {
            Square cur = this.board.board[row][i];

            if(cur.getPiece() != null) {
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.whiteTurn) || (!pieceIsWhite && this.whiteTurn)) {
                    String ending = Move.rowToFile.get(row) + (i + 1);
                    moves.add(position + " to " + ending);
                }

                break;
            }

            String ending = Move.rowToFile.get(row) + (i + 1);
            moves.add(position + " to " + ending);
        }

        // HORIZONTAL MOVES
        // going left
        for(int i = row - 1; i >= 0; i--) {
            Square cur = this.board.board[i][col];

            if(cur.getPiece() != null) {
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.whiteTurn) || (!pieceIsWhite && this.whiteTurn)) {
                    String ending = Move.rowToFile.get(i) + (col + 1);
                    moves.add(position + " to " + ending);
                }

                break;
            }

            String ending = Move.rowToFile.get(i) + (col + 1);
            moves.add(position + " to " + ending);
        }

        // going right
        for(int i = row + 1; i < 8; i++) {
            Square cur = this.board.board[i][col];

            if(cur.getPiece() != null) {
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.whiteTurn) || (!pieceIsWhite && this.whiteTurn)) {
                    String ending = Move.rowToFile.get(i) + (col + 1);
                    moves.add(position + " to " + ending);
                }

                break;
            }

            String ending = Move.rowToFile.get(i) + (col + 1);
            moves.add(position + " to " + ending);
        }

        // DIAGONAL MOVES
        // going up-left
        for(int i = row - 1, j = col + 1; i >= 0 && j < 8; i--, j++) {
            Square cur = this.board.board[i][j];

            if(cur.getPiece() != null) {
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.whiteTurn) || (!pieceIsWhite && this.whiteTurn)) {
                    String ending = Move.rowToFile.get(i) + (j + 1);
                    moves.add(position + " to " + ending);
                }

                break;
            }

            String ending = Move.rowToFile.get(i) + (j + 1);
            moves.add(position + " to " + ending);
        }

        // going up-right
        for(int i = row + 1, j = col + 1; i < 8 && j < 8; i++, j++) {
            Square cur = this.board.board[i][j];

            if(cur.getPiece() != null) {
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.whiteTurn) || (!pieceIsWhite && this.whiteTurn)) {
                    String ending = Move.rowToFile.get(i) + (j + 1);
                    moves.add(position + " to " + ending);
                }

                break;
            }

            String ending = Move.rowToFile.get(i) + (j + 1);
            moves.add(position + " to " + ending);
        }

        // going down-left
        for(int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            Square cur = this.board.board[i][j];

            if(cur.getPiece() != null) {
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.whiteTurn) || (!pieceIsWhite && this.whiteTurn)) {
                    String ending = Move.rowToFile.get(i) + (j + 1);
                    moves.add(position + " to " + ending);
                }

                break;
            }

            String ending = Move.rowToFile.get(i) + (j + 1);
            moves.add(position + " to " + ending);
        }
        
        // going down-right
        for(int i = row + 1, j = col - 1; i < 8 && j >= 0; i++, j--) {
            Square cur = this.board.board[i][j];

            if(cur.getPiece() != null) {
                pieceIsWhite = cur.getPiece().isWhite();

                if((pieceIsWhite && !this.whiteTurn) || (!pieceIsWhite && this.whiteTurn)) {
                    String ending = Move.rowToFile.get(i) + (j + 1);
                    moves.add(position + " to " + ending);
                }

                break;
            }

            String ending = Move.rowToFile.get(i) + (j + 1);
            moves.add(position + " to " + ending);
        }
    }

    /**
     * checks all moves for the king located at <code>row</code>, <code>col</code>.
     * These moves also include castling moves.
     * 
     * @param row       row corressponding to the file of the piece
     * @param col       col corresponding to the rank of the piece
     * @param moves     String hashset of all possible moves.
     * 
     * @author          Bryle Tan
     */
    private void kingMoves(int row, int col, HashSet<String> moves) {
        // check if it's the right team's piece
        boolean pieceIsWhite = this.board.board[row][col].getPiece().isWhite();
        if((pieceIsWhite && !this.whiteTurn) || (!pieceIsWhite && this.whiteTurn)) {
            return;
        }

        String position = Move.rowToFile.get(row) + (col + 1);

        //regular king movements (3x3)
        //down
        if(col - 1 >= 0) {
            if(this.board.board[row][col - 1].getPiece() != null) {
                pieceIsWhite = this.board.board[row][col - 1].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row) + (col - 1 + 1));
                }
            }
    
            if(this.board.board[row][col - 1].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row) + (col - 1 + 1));
            }
        }
        //up
        if(col + 1 < 8) {
            if(this.board.board[row][col + 1].getPiece() != null) {
                pieceIsWhite = this.board.board[row][col + 1].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row) + (col + 1 + 1));
                }
            }
    
            if(this.board.board[row][col + 1].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row) + (col + 1 + 1));
            }
        }
        //right 
        if(row + 1 < 8) {
            if(this.board.board[row + 1][col].getPiece() != null) {
                pieceIsWhite = this.board.board[row + 1][col].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row + 1) + (col + 1));
                }
            }
    
            if(this.board.board[row + 1][col].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row + 1) + (col + 1));
            }
        }
        //left 
        if(row - 1 >= 0) {
            if(this.board.board[row - 1][col].getPiece() != null) {
                pieceIsWhite = this.board.board[row - 1][col].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row - 1) + (col + 1));
                }
            }
    
            if(this.board.board[row - 1][col].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row - 1) + (col + 1));
            }
        }
        //down left
        if(row - 1 >= 0 && col - 1 >= 0) {
            if(this.board.board[row - 1][col - 1].getPiece() != null) {
                pieceIsWhite = this.board.board[row - 1][col - 1].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row - 1) + (col - 1 + 1));
                }
            }
    
            if(this.board.board[row - 1][col - 1].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row - 1) + (col - 1 + 1));
            }
        }
        //up left 
        if(row - 1 >= 0 && col + 1 < 8) {
            if(this.board.board[row - 1][col + 1].getPiece() != null) {
                pieceIsWhite = this.board.board[row - 1][col + 1].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row - 1) + (col + 1 + 1));
                }
            }
    
            if(this.board.board[row - 1][col + 1].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row - 1) + (col + 1 + 1));
            }
        }
        //down right
        if(row + 1 < 8 && col - 1 >= 0) {
            if(this.board.board[row + 1][col - 1].getPiece() != null) {
                pieceIsWhite = this.board.board[row + 1][col - 1].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row + 1) + (col - 1 + 1));
                }
            }
    
            if(this.board.board[row + 1][col - 1].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row + 1) + (col - 1 + 1));
            }
        }
        //up right
        if(row + 1 < 8 && col + 1 < 8) {
            if(this.board.board[row + 1][col + 1].getPiece() != null) {
                pieceIsWhite = this.board.board[row + 1][col + 1].getPiece().isWhite();
                if(pieceIsWhite && !this.isWhiteTurn() || !pieceIsWhite && this.isWhiteTurn()) {
                    moves.add(position + " to " + Move.rowToFile.get(row + 1) + (col + 1 + 1));
                }
            }
    
            if(this.board.board[row + 1][col + 1].getPiece() == null) {
                moves.add(position + " to " + Move.rowToFile.get(row + 1) + (col + 1 + 1));
            }
        }

        //castling logic
        Piece king = this.board.board[row][col].getPiece();
        
        //white king
        Square rightWhiteRook = this.board.board[7][0];
        Square leftWhiteRook = this.board.board[0][0];

        //black king
        Square rightBlackRook = this.board.board[7][7];
        Square leftBlackRook = this.board.board[0][7];

        //if the bottom right corner is a white rook and has not moved, and there are no pieces in between them
        if(!Chess.inCheck && !king.hasMoved && rightWhiteRook.getPiece() != null && rightWhiteRook.getPiece().isWhite && rightWhiteRook.getPiece().type.equals("rook") && !rightWhiteRook.getPiece().hasMoved && this.board.board[5][0].getPiece() == null && this.board.board[6][0].getPiece() == null) {
            
            String ending = Move.rowToFile.get(row + 2) + (col + 1);
            moves.add(position + " to " + ending);
        }
        //top right corner
        if(!Chess.inCheck && !king.hasMoved && rightBlackRook.getPiece() != null && !rightBlackRook.getPiece().isWhite && rightBlackRook.getPiece().type.equals("rook") && !rightBlackRook.getPiece().hasMoved && this.board.board[5][7].getPiece() == null && this.board.board[6][7].getPiece() == null) {
            String ending = Move.rowToFile.get(row + 2) + (col + 1);
            moves.add(position + " to " + ending);
        }
        //bottom left corner
        if(!Chess.inCheck && !king.hasMoved && leftWhiteRook.getPiece() != null && leftWhiteRook.getPiece().isWhite && leftWhiteRook.getPiece().type.equals("rook") && !leftWhiteRook.getPiece().hasMoved && this.board.board[1][0].getPiece() == null && this.board.board[2][0].getPiece() == null && this.board.board[3][0].getPiece() == null) {
            String ending = Move.rowToFile.get(row - 2) + (col + 1);
            moves.add(position + " to " + ending);
        }
        //top left corner
        if(!Chess.inCheck && !king.hasMoved && leftBlackRook.getPiece() != null && !leftBlackRook.getPiece().isWhite && leftBlackRook.getPiece().type.equals("rook") && !leftBlackRook.getPiece().hasMoved && this.board.board[1][7].getPiece() == null && this.board.board[2][7].getPiece() == null && this.board.board[3][7].getPiece() == null) {
        String ending = Move.rowToFile.get(row - 2) + (col + 1);
            moves.add(position + " to " + ending);
        }
    }

    /**
     * @return string representation of the move object.
     *         Formatted as "<code>originalPosition</code> to <code>endingPosition</code>"
     */
    public String toString() {
        return this.originalPosition + " to " + this.endingPosition;
    }
}
