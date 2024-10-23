import java.awt.*;

/**
 * Piece super class from which all piece types derive. 
 */
public class Piece {  

    boolean isWhite;
    Point location;
    
    /**. */
    Piece(boolean isWhite, Point location) {
        this.isWhite = isWhite;
        this.location = location;
    }


    boolean validMove(Point target) {
        return true;
    }

    boolean validCapture(Point target) {
        return true;
    }

    /**
     * Method to determine of a piece is blocking a move on
     * a straight line.
     * @param target the square that the piece wants to move to
     * @return returns if the move is valid
     */
    boolean pieceIsOnStraightLine(Point target) {
        //when piece is moving left
        for (int col = location.y - 1; col > target.y; col--) {
            for (Piece[] pieces : ChessGame.board) {
                for (Piece piece : pieces) {
                    if (piece != null) {
                        if (piece.location.y == col && piece.location.x == target.x) {
                            //System.out.println("Invalid move piece in the way");
                            //System.out.println(piece.location + " " + piece);
                            return true;
                        }
                    }
                }
            }
        }

        //when piece is moving right
        for (int col = location.y + 1; col < target.y; col++) {
            for (Piece[] pieces : ChessGame.board) {
                for (Piece piece : pieces) {
                    if (piece != null) {
                        if (piece.location.y == col && piece.location.x == target.x) {
                            //System.out.println("Invalid move piece in the way");
                            //System.out.println(piece.location + " " + piece);
                            return true;
                        }
                    }
                }
            }
        }

        //when piece is moving up
        for (int row = location.x - 1; row > target.x; row--) {
            for (Piece[] pieces : ChessGame.board) {
                for (Piece piece : pieces) {
                    if (piece != null) {
                        if (piece.location.y == target.y && piece.location.x == row) {
                            //System.out.println("Invalid move piece in the way");
                            //System.out.println(piece.location + " " + piece);
                            return true;
                        }
                    }
                }
            }
        }

        //when piece is moving down
        for (int row = location.x + 1; row < target.x; row++) {
            for (Piece[] pieces : ChessGame.board) {
                for (Piece piece : pieces) {
                    if (piece != null) {
                        if (piece.location.y == target.y && piece.location.x == row) {
                            //System.out.println("Invalid move piece in the way");
                            //System.out.println(piece.location + " " + piece);
                            return true;
                        }
                    }
                }
            }
        }

        //System.out.println("No piece in the way valid move");
        return false;
    }

    /**
     * Method to determine of a piece is blocking a move on
     * a diagonal line.
     * @param target the square that the piece wants to move to
     * @return returns if the move is valid
     */
    boolean pieceIsOnDiagonalLine(Point target) {
        if (target.x < location.x) {
            //up left
            for (int col = location.y - 1; col > target.y; col--) {
                int diff = Math.abs(col - location.y);
                for (Piece[] pieces : ChessGame.board) {
                    for (Piece piece : pieces) {
                        if (piece != null) {
                            if (piece.location.y == col && piece.location.x == location.x - diff) {
                                //System.out.println("Invalid move piece in the way");
                                System.out.println(piece.location + " " + piece);
                                return true;
                            }
                        }
                    }
                }
            }

            //up right
            for (int col = location.y + 1; col < target.y; col++) {
                int diff = Math.abs(col - location.y);
                for (Piece[] pieces : ChessGame.board) {
                    for (Piece piece : pieces) {
                        if (piece != null) {
                            if (piece.location.y == col && piece.location.x == location.x - diff) {
                                //System.out.println("Invalid move piece in the way");
                                //System.out.println(piece.location + " " + piece);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        if (target.x > location.x) {
            //down left
            for (int col = location.y - 1; col > target.y; col--) {
                int diff = Math.abs(col - location.y);
                for (Piece[] pieces : ChessGame.board) {
                    for (Piece piece : pieces) {
                        if (piece != null) {
                            if (piece.location.y == col && piece.location.x == location.x + diff) {
                                //System.out.println("Invalid move piece in the way");
                                //System.out.println(piece.location + " " + piece);
                                return true;
                            }
                        }
                    }
                }
            }

            //down right
            for (int col = location.y + 1; col < target.y; col++) {
                int diff = Math.abs(col - location.y);
                for (Piece[] pieces : ChessGame.board) {
                    for (Piece piece : pieces) {
                        if (piece != null) {
                            if (piece.location.y == col && piece.location.x == location.x + diff) {
                                //System.out.println("Invalid move piece in the way");
                                //System.out.println(piece.location + " " + piece);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        //System.out.println("No piece in the way valid move");
        return false;
    }

    boolean hasLegalMoves() {
        return true;
    }

    /**
    * Method to check if the move is illegal.
    * @param target needed to simulate the move
    *  @return returns if the move is illegal
    */
    public boolean illegalMove(Point target) {
        // Store the current locations of the pieces
        Piece targetPiece = ChessGame.board[target.x][target.y]; // Target piece
        Point originalLocation = new Point(location); // Starting position
    
        // Fetch the location of the king
        King king = getKing();
        if (king == null) {
            return true; // Illegal move by default if king is null
        }
    
        // Simulate the move
        ChessGame.board[location.x][location.y] = null; // Remove piece from original location
        ChessGame.board[target.x][target.y] = this; // Place piece at target location
        location = target; // Update piece's location
    
        // Check if the king is in check
        boolean isInCheck = king.isInCheck();
    
        // Revert the move
        ChessGame.board[target.x][target.y] = targetPiece; // Restore target piece
        ChessGame.board[originalLocation.x][originalLocation.y] = this; // Restore original piece
        location = originalLocation; // Restore original location
    
        // Return true if the king is in check (indicating an illegal move)
        return isInCheck;
    }
    
    public King getKing() {
        for (Piece piece : ChessGame.isWhiteTurn ? ChessGame.whitePieces : ChessGame.blackPieces) {
            if (piece instanceof King) {
                return (King) piece;
            }
        }

        return null;
    }
    

    void move(Point target) {
        location = target;
    }

}
