import java.awt.*;

/**
 * Class to handle the behaviour of bishops.
 */
public class Bishop extends Piece {
    int points = 3;

    Bishop(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {
        if (location.equals(target)) {
            //System.out.println("Invalid move: Target is the same as current location");
            return false; // No move if the target is the same as the current location
        }

        if (Math.abs(target.y - location.y) == Math.abs(target.x - location.x)) {
            if (!pieceIsOnDiagonalLine(target)) {
                return true;
            }
        }

        //System.out.println("Invalid move");
        return false;
    }

    @Override
    boolean validCapture(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }
        
        return validMove(target);
    }

    @Override
    boolean hasLegalMoves() {
        Point[] directions = {
            new Point(1, 1), //down right
            new Point(1, -1), //down left
            new Point(-1, 1), //up right
            new Point(-1, -1) //down left
        };

        for (Point d : directions) {
            Point currPos = location;

            while (true) {
                Point newPos = new Point(currPos.x + d.x, currPos.y + d.y);
                if (!ChessGame.ChessMouseListener.pointInBoard(newPos)) {
                    //position not in board
                    break;
                }

                //check if position is occupied
                Piece occupyingPiece = ChessGame.board[newPos.x][newPos.y];
                if (occupyingPiece != null) {
                    //check colour of piece
                    if (occupyingPiece.isWhite != isWhite) {
                        if (validCapture(d) && !ChessGame.ChessMouseListener.illegalMove(d, this)) {
                            //can capture so can move
                            return true;
                        }
                    }
                    break;
                } else {
                    if (validMove(d) && !ChessGame.ChessMouseListener.illegalMove(d, this)) {
                        //can move
                        return true;
                    }
                }

                currPos = newPos;
            }
        }

        //no moves
        return false;
    }
}
