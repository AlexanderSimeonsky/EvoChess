import java.awt.*;

public class King extends Piece{
    int points = 0;

    King(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {
        return true;
    }

    @Override
    boolean validCapture(Point target) {
        return true;
    }
}
