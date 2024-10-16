import java.awt.*;

public class Pawn {
    final int points = 1;
    Point location;
    boolean isWhite;

    Pawn(boolean isWhite, Point location) {
        this.isWhite = isWhite;
        this.location = location;
    }

    public boolean validMove(Point target) {

        if (isWhite) {
            if (location.getX() == 6) {
                return target.getX() <= location.getX() + 2 && target.getX() > location.getX();
            } else {
                return target.getX() <= location.getX() + 1 && target.getX() > location.getX();
            }
        } else {
            if (location.getX() == 1) {
                return target.getX() <= location.getX() + 2 && target.getX() > location.getX();
            } else {
                return target.getX() <= location.getX() + 1 && target.getX() > location.getX();
            }
        }
    }

    public boolean validCapture(Point target) {

        if (isWhite) {
            if (location.getX() + 1 == target.getY()) {
                return location.getY() + 1 == target.getY() || location.getY() - 1 == target.getY();
            }
        } else {
            if (location.getX() - 1 == target.getX()) {
                return location.getY() + 1 == target.getY() || location.getY() - 1 == target.getY();
            }
        }

        return false;
    }

    public void move(Point target) {
        location = target;
    }
}
