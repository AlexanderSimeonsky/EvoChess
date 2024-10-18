import java.awt.*;

public class Knight extends Piece{
    int points = 3;

    Knight(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {
        System.out.println("test move");
        if (location.equals(target)) {
            System.out.println("invalid move location same as target");
            return false; // No move if the target is the same as the current location
        }

        int deltaX = Math.abs(target.x - location.x);
        int deltaY = Math.abs(target.y - location.y);

        if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
            System.out.println("VALID move");
            return true;
        }

        System.out.println("invalid move");
        return false;
    }

    @Override
    boolean validCapture(Point target) {
        if (location.equals(target)) {
            System.out.println("invalid move same target is the same as location");
            return false; // No move if the target is the same as the current location
        }
        
        return validMove(target);
    }
}
