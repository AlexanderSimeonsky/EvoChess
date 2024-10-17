import java.awt.*;

import javax.swing.JPanel;

public class Bishop extends Piece{
    int points = 3;

    Bishop(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }

        int deltaX = target.x - location.x;
        int deltaY = target.y - location.y;

        //check if on the same diagonal
        if (Math.abs(deltaX) != Math.abs(deltaY)) {
            return false;
        }

        int xStep = (deltaX > 0) ? 1 : -1;
        int yStep = (deltaY > 0) ? 1 : -1;

        int x = location.x + xStep;
        int y = location.y + yStep;

        while (x != target.x && y != target.y) {
            Piece p = ChessGame.board[x][y];
            if (p != null) {
                return false; // There is a piece in the way
            }
            x += xStep;
            y += yStep;
        }

        return true;
    }

    @Override
    boolean validCapture(Point target) {
        Piece p = ChessGame.board[target.x][target.y];

        if (p != null) {
            return validMove(target);
        }
        return false;
    }
}
