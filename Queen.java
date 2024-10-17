import java.awt.*;

public class Queen extends Piece{
    int points = 9;

    Queen(boolean isWhite, Point location) {
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
