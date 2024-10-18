import java.awt.*;

public class Bishop extends Piece {
    int points = 3;

    Bishop(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {
        if (location.equals(target)) {
            System.out.println("Invalid move: Target is the same as current location");
            return false; // No move if the target is the same as the current location
        }

        if (Math.abs(target.y - location.y) == Math.abs(target.x - location.x)) {
            if (!PieceIsOnDiagonalLine(target)) {
                return true;
            }
        }

        System.out.println("Invalid move");
        return false;
    }

    @Override
    boolean validCapture(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }
        
        return validMove(target);
    }
}
