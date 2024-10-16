import java.awt.*;

public class Bishop extends Piece{
    int points = 3;

    Bishop(boolean isWhite, Point location) {
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
