import javax.swing.*;
import java.awt.*;

public class Pawn extends Piece {
    int points = 1;

    Pawn(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    public boolean validMove(Point target) {
        double deltaX = Math.signum(target.getX() - location.getX());
        double deltaY = Math.signum(target.getY() - location.getY());

        int x = (int) (location.getX() + deltaX);
        int y = (int) (location.getY() + deltaY);
        
        if (isWhite) {
            if (location.getX() == 6) {
                JPanel c = (JPanel) ChessGame.chessBoardPanel.getComponent(x * 8 + y);
                Piece p = (Piece) c.getClientProperty("piece");

                return target.getX() >= location.getX() - 2 && target.getX() < location.getX() && p == null;
            } else {
                return target.getX() >= location.getX() - 1 && target.getX() < location.getX();
            }
        } else {
            if (location.getX() == 1) {
                JPanel c = (JPanel) ChessGame.chessBoardPanel.getComponent(x * 8 + y);
                Piece p = (Piece) c.getClientProperty("piece");

                return target.getX() <= location.getX() + 2 && target.getX() > location.getX() && p == null;
            } else {
                return target.getX() <= location.getX() + 1 && target.getX() > location.getX();
            }
        }
    }

    @Override
    public boolean validCapture(Point target) {

        if (isWhite) {
            if (location.getX() - 1 == target.getX()) {
                return location.getY() + 1 == target.getY() || location.getY() - 1 == target.getY();
            }
        } else {
            if (location.getX() + 1 == target.getX()) {
                return location.getY() + 1 == target.getY() || location.getY() - 1 == target.getY();
            }
        }

        return false;
    }

    public void move(Point target) {
        location = target;
    }

    //TODO promotion
    //TODO EN Passant
}
