import java.awt.*;

/**
 * Piece super class from which all piece types derive. 
 */
public class Piece {  

    boolean isWhite;
    Point location;
    int points;
    int acquiredPoints;
    
    /**. */
    Piece(boolean isWhite, Point location) {
        this.isWhite = isWhite;
        this.location = location;
        points = 0;
        acquiredPoints = 0;
    }


    boolean validMove(Point target) {
        return false;
    }

    boolean validCapture(Point target) {
        return false;
    }

    Piece pieceEvolves() {
        return null;
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
        Piece targetP = ChessGame.board[target.x][target.y]; // Target piece
        Point spLocation = location; // Starting position
    
        // Fetch the location of the king
        King king = null;
        if (this instanceof King) {
            // If the piece being moved is the king, it's the king itself
            king = (King) this;
        } else {
            // Otherwise, get the king based on the turn
            king = getKing();
        }
    
        // Null check: If king is null, it's an invalid board state, return true as an illegal move
        if (king == null) {
            return true; // Illegal move by default
        }
        
        // Move the piece temporarily
        ChessGame.board[target.x][target.y] = null;
        ChessGame.board[target.x][target.y] = this;
        ChessGame.board[location.x][location.y] = null;
        location = target;
    
        // Check if any piece can attack the king
        boolean check = king.isInCheck(targetP);
        
        // Revert the move
        ChessGame.board[target.x][target.y] = targetP;
        ChessGame.board[spLocation.x][spLocation.y] = this;
        location = spLocation;
    
        // Return true if the king is in check (indicating an illegal move)
        System.out.println("returning " + check);
        return check;
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
