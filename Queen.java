import java.awt.*;
import javax.swing.JPanel;

public class Queen extends Piece {
    int points = 9;

    Queen(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }

        //Vertical and Horizontal movement
        if (target.x == location.x || target.y == location.y) {
            //System.out.println("Check if there is blocking piece");
            if (!pieceIsOnStraightLine(target)) {
                return true;
            }
        }

        //Diagonal movement
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
        
        //System.out.println("check valid move move");
        return validMove(target);
    }

    @Override
    boolean hasLegalMoves() {
        Point[] directions = {
            //diagonal moves
            new Point(1, 1), //down right
            new Point(1, -1), //down left
            new Point(-1, 1), //up right
            new Point(-1, -1), //down left
            //straight line moves
            new Point(1, 0), //down
            new Point(-1, 0), //up
            new Point(0, 1), //right
            new Point(0, -1) //left
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
                        if (validCapture(d) && !illegalMove(d)) {
                            //can capture so can move
                            return true;
                        }
                    }
                    break;
                } else {
                    if (validMove(d) && !illegalMove(d)) {
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
