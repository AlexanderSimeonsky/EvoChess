import java.awt.*;

public class Rook extends Piece{
    int points = 5;

    Rook(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {

        if (location.getX() == target.getX() || location.getY() == target.getY()) {
            return true;
        }

        return false;
    }

    @Override
    boolean validCapture(Point target) {
        return validMove(target);
    }
}
