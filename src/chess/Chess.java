package chess;

import model.*;

import java.util.Scanner;

/**
 * This class contains the main method, and is the class that runs the chess game.
 * 
 * @author Bryle Tan
 * @author Maanas Pimplikar
 */
public class Chess {

    /**
     * Board object to keep track of the current state of the game.
     */
    public static Board game;

    /**
     * Scanner object used to take user input.
     */
    public static Scanner scan = new Scanner(System.in);

    /**
     * string to store the current user's raw input.
     */
    public static String rawInput;

    /**
     * boolean to keep track of the check status for either kings.
     */
    public static boolean inCheck = false;

    /**
     * boolean to keep track of the team's turns. Initialized as <code>true</code>
     * since the white team always starts the game.
     */
    private static boolean isWhiteTurn = true;

    /**
     * Main method, calls the start method.
     * 
     * @param args  Standard Input
     * @author      Bryle Tan
     */
    public static void main(String[] args) {
        start();
    }

    /**
     * Starts the chess game. Prints the board, takes in user input, and moves the pieces.
     * 
     * @author Bryle Tan
     * @author Maanas Pimplikar
     */
    public static void start() {
        // keep scanning input until the game ends with checkmate, one player resigns, or the game draws
        game = new Board();

        
        /*
         * The game needs to be running until it ends due to one of the following:
         * 1. a player loses to a checkmate
         * 2. a player resigns
         * 3. a player draws
         * 
         * If either of the three conditions are met, set running = false and print the goodbye message
         * (goodbye message is either one player winning or the game drawing)
         */
        boolean running = true;
        while(running) {
            game.printBoard();
            
            if(inCheck) {
                System.out.println("Check");
            }
            boolean valid = false;
            while(!valid) {
                if(isWhiteTurn) {
                    System.out.print("White's move: ");
                }
                else {
                    System.out.print("Black's move: ");
                }

                String rawInput = scan.nextLine();
                String[] nextInput = rawInput.split(" ");
                
                if(rawInput.equals("")) {
                    System.out.println("illegal move, try again");
                    continue;
                }

                if(nextInput.length == 1) {
                    if(rawInput.equals("resign")) {
                        running = false;
                        if(isWhiteTurn) {
                            System.out.print("Black wins");
                        }
                        else {
                            System.out.print("White wins");
                        }
                        break;
                    }
                    if(!rawInput.equals("resign")) {
                        System.out.println("illegal move, try again");
                        continue;
                    }
                }

                if(nextInput.length == 3 && nextInput[2].equals("draw?")) {
                    System.out.print("draw?: ");
                    String response = scan.nextLine();

                    if(response.equals("draw")) {
                        running = false;
                        break;
                    }
                }

                // if more information given, then
                    // its either a promotion or draw

                // positions of the piece
                String starting = nextInput[0];
                String ending = nextInput[1];


                Move move = new Move(starting, ending, game, isWhiteTurn);

                String validation = move.validateMove();
    
                if(validation.equals("invalid")) {
                    System.out.println("illegal move, try again");
                } else if(validation.equals("checkmate")) {
                    Chess.makeMove(rawInput, starting, ending);
                    System.out.println();
                    game.printBoard();
    
                    System.out.println("Checkmate");
                    running = false;
                    if(isWhiteTurn) {
                        System.out.print("White wins");
                    }
                    else {
                        System.out.print("Black wins");
                    }
                    break;
                } else {
                    if(validation.equals("check")) {
                        inCheck = true;
                    } else {
                        inCheck = false;
                    }
                        
                    valid = true;
                    Chess.makeMove(rawInput, starting, ending);
                }
            }

            // change the turn to be the other color's
            isWhiteTurn = !isWhiteTurn;
            System.out.println();
        }
    }

    /**
     * Moves the piece located at <code>startingPosition</code> to <code>endingPosition</code>.
     * 
     * @param raw               the raw input string of the user, read in the main method.
     * @param startingPosition  string representating location of the piece to be moved, given in filerank notation.
     *                          For example, c6.
     * @param endingPosition    string representing location of where the piece is moving to, given in filerank notation.
     * 
     * @author                  Maanas Pimplikar
     */
    public static void makeMove(String raw, String startingPosition, String endingPosition) {
        int startingRow = Move.fileToRow.get(startingPosition.substring(0, 1));
        int startingCol = Integer.parseInt(startingPosition.substring(1)) - 1;

        int endingRow = Move.fileToRow.get(endingPosition.substring(0, 1));
        int endingCol = Integer.parseInt(endingPosition.substring(1)) - 1;

        Piece movingPiece = game.board[startingRow][startingCol].getPiece();
                    
        /*EMPASSANT CODE*/
        if(movingPiece.type.equals("pawn")) {
            if(Math.abs(endingCol - startingCol) == 2) {
                movingPiece.enpassant = true;
            }
        }
        if(movingPiece.type.equals("pawn") && Math.abs(endingRow - startingRow) == 1 && Math.abs(endingCol - startingCol) == 1) {
            if(isWhiteTurn) {
                Square under = game.board[endingRow][endingCol-1];
                if(under.getPiece() != null && !under.getPiece().isWhite && under.getPiece().enpassant == true) {
                    game.board[endingRow][endingCol-1].setPiece(null);
                }
            }
            else {
                Square over = game.board[endingRow][endingCol+1];
                if(over.getPiece() != null && over.getPiece().isWhite && over.getPiece().enpassant == true) {
                    game.board[endingRow][endingCol+1].setPiece(null);
                }
            }
        }
        
        //castling kingside
        if(movingPiece.type.equals("king") && endingRow - startingRow == 2) {
            //if the bottom right piece is a white rook and has not moved
            if(isWhiteTurn && movingPiece.isWhite) {
                game.board[7][0].setPiece(null);
                game.board[5][0].setPiece(new Rook("white"));
            }
            else {
                game.board[7][7].setPiece(null);
                game.board[5][7].setPiece(new Rook("black"));
            }
        }
        
        //castling queenside
        if(movingPiece.type.equals("king") && endingRow - startingRow == -2) {
            if(isWhiteTurn && movingPiece.isWhite) {
                game.board[0][0].setPiece(null);
                game.board[3][0].setPiece(new Rook("white"));
            }
            else {
                game.board[0][7].setPiece(null);
                game.board[3][7].setPiece(new Rook("black"));
            }
        }

        game.board[endingRow][endingCol].setPiece(movingPiece);
        game.board[startingRow][startingCol].setPiece(null);
        movingPiece.hasMoved = true;

        // handling promotion
        if((endingCol == 7 || endingCol == 0) && movingPiece.type.equals("pawn")) {
            Pawn.promote(raw, game.board, movingPiece.isWhite(), endingRow, endingCol);
        }
    }
}