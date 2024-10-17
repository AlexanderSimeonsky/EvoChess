import java.awt.*;

public class Knight extends Piece{
    int points = 3;

    Knight(boolean isWhite, Point location) {
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
