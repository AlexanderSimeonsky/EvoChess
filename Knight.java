import java.awt.*;

public class Knight extends Piece{
    int points = 3;

    Knight(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }

        int deltaX = Math.abs(target.x - location.x);
        int deltaY = Math.abs(target.y - location.y);

        if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
            return true;
        }

        return false;
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
