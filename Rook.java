import java.awt.*;
import javax.swing.*;

public class Rook extends Piece {
    int points = 5;

    Rook(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {

        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }

        int deltaX = target.x - location.x;
        int deltaY = target.y - location.y;

        
        if (deltaX != 0 && deltaY != 0) {
            return false; // Invalid rook move if not in straight line
        }

        if (deltaX == 0) { //side movement
            int step = (deltaY > 0) ? 1 : -1;
            for (int i = location.y + step; i != target.y; i += step) {
                Piece p = ChessGame.board[location.x][i];
                if (p != null) {
                    return false; // There is a piece in the way
                }
            }
        } else if (deltaY == 0) { //up and down movement
            int step = (deltaX > 0) ? 1 : -1;
            for (int i = location.x + step; i != target.x; i += step) {
                Piece p = ChessGame.board[i][location.y];
                if (p != null) {
                    return false; // There is a piece in the way
                }
            }
        }

        return true; // Move is valid if no obstacles found
    }

    @Override
    boolean validCapture(Point target) {
        Piece p = ChessGame.board[target.x][target.y];

        if (p != null) {
            return validMove(target);
        }
        return false;
    }
}
