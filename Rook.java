import java.awt.*;
import javax.swing.*;

public class Rook extends Piece {
    int points = 5;
    boolean hasMoved = false;

    Rook(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {

        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }

        if (target.x == location.x || target.y == location.y) {
            //System.out.println("Check if there is blocking piece");
            if (!pieceIsOnStraightLine(target)) {
                hasMoved = true;
                return true;
            }
        }

        //System.out.println("Invalid move");
        return false;
    }

    @Override
    boolean validCapture(Point target) {
        if (location.equals(target)) {
            //System.out.println("invalid move");
            return false; // No move if the target is the same as the current location
        }
        
        return validMove(target);
    }

    @Override
    boolean hasLegalMoves() {
        Point[] directions = {
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
