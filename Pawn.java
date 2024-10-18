import javax.swing.*;
import java.awt.*;

public class Pawn extends Piece {
    int points = 1;

    Pawn(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    public boolean validMove(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }
        
        int deltaX = target.x - location.x;
        int deltaY = Math.abs(target.y - location.y);

        if (deltaY != 0) {
            return false;
        }
        
        if (isWhite) {
            if (location.x == 6 && deltaX == -2) {
                return ChessGame.board[location.x - 1][location.y] == null;
            }

            return deltaX == -1;
        } else {
            if (location.x == 1 && deltaX == 2) {
                return ChessGame.board[location.x + 1][location.y] == null;
            }

            return deltaX == 1;
        }
    }

    @Override
    public boolean validCapture(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }
        
        int deltaX = target.x - location.x;
        int deltaY = Math.abs(target.y - location.y);

        if (isWhite) {
            if (deltaX == -1 && deltaY == 1) {
                return true;
            }
        } else {
            if (deltaX == 1 && deltaY == 1) {
                return true;
            }
        }

        return false;
    }

    //TODO promotion
    //TODO EN Passant
}
