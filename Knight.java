import java.awt.*;

public class Knight extends Piece{
    int points = 3;

    Knight(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {

        int deltaX = Math.abs(target.x - location.x);
        int deltaY = Math.abs(target.y - location.y);

        if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
            return true;
        }

        return false;
    }

    @Override
    boolean validCapture(Point target) {
        return validMove(target);
    }
}
