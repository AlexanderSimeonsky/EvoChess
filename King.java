import java.awt.*;

public class King extends Piece{
    int points = 0;
    boolean didCastle = true;

    King(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }

        int deltaX = Math.abs(target.x - location.x);
        int deltaY = Math.abs(target.y - location.y);

        // Check if the move is one square away in any direction
        return (deltaX <= 1 && deltaY <= 1 && (deltaX + deltaY) > 0);
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
