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
    

    void move(Point target) {
        location = target;
    }

}
