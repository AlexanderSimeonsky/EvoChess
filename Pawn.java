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
        
        double deltaX = Math.signum(target.x - location.x);
        double deltaY = Math.signum(target.y - location.y);

        int x = (int) (location.y + deltaX);
        int y = (int) (location.y + deltaY);
        
        if (isWhite) {
            if (location.x == 6) {
                Piece p = ChessGame.board[target.x][target.y];

                return target.x >= location.x - 2 && target.x < location.x && p == null && target.y == location.y;
            } else {
                return target.x >= location.x - 1 && target.x < location.x && target.y == location.y;
            }
        } else {
            if (location.x == 1) {
                Piece p = ChessGame.board[target.x][target.y];

                return target.x <= location.x + 2 && target.x > location.x && p == null && target.y == location.y;
            } else {
                return target.x <= location.x + 1 && target.x > location.x && target.y == location.y;
            }
        }
    }

    @Override
    public boolean validCapture(Point target) {
        if (isWhite) {
            if (location.x - 1 == target.x) {
                return location.y + 1 == target.y || location.y - 1 == target.y;
            }
        } else {
            if (location.x + 1 == target.getX()) {
                return location.y + 1 == target.y || location.y - 1 == target.y;
            }
        }

        return false;
    }

    public void move(Point target) {
        location = target;
    }

    //TODO promotion
    //TODO EN Passant
}
