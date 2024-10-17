import java.awt.*;
import javax.swing.*;

public class Rook extends Piece {
    int points = 5;

    Rook(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {
        var board = ChessGame.chessBoardPanel;

        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }

        int deltaX = target.x - location.x;
        int deltaY = target.y - location.y;

        // Rook moves either vertically or horizontally
        if (deltaX != 0 && deltaY != 0) {
            return false; // Invalid rook move if not in straight line
        }

        // Vertical movement
        if (deltaX == 0) {
            int step = (deltaY > 0) ? 1 : -1;
            for (int i = location.y + step; i != target.y; i += step) {
                JPanel temp = (JPanel) board.getComponent((int) (location.getX() * 8 + i));
                if ((Piece) temp.getClientProperty("piece") != null) {
                    return false; // There is a piece in the way
                }
            }
        }
        // Horizontal movement
        else if (deltaY == 0) {
            int step = (deltaX > 0) ? 1 : -1;
            for (int i = location.x + step; i != target.x; i += step) {
                JPanel temp = (JPanel) board.getComponent((int) (i * 8 + location.getY()));
                if ((Piece) temp.getClientProperty("piece") != null) {
                    return false; // There is a piece in the way
                }
            }
        }

        return true; // Move is valid if no obstacles found
    }

    @Override
    boolean validCapture(Point target) {
        var board = ChessGame.chessBoardPanel;
        JPanel temp = (JPanel) board.getComponent((int) (target.getX() * 8 + target.getY()));

        Piece targetPiece = (Piece) temp.getClientProperty("piece");
        if (targetPiece != null && targetPiece.isWhite != this.isWhite) {
            return validMove(target);
        }
        return false;
    }
}
