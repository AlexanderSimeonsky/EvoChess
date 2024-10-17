import java.awt.*;
import javax.swing.JPanel;

public class Queen extends Piece {
    int points = 9;

    Queen(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {

        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }

        int deltaX = target.x - location.x;
        int deltaY = target.y - location.y;

        // Check if the move is in a valid direction: horizontal, vertical, or diagonal
        if (deltaX != 0 && deltaY != 0 && Math.abs(deltaX) != Math.abs(deltaY)) {
            return false; // Not a valid queen move
        }

        int xStep = Integer.signum(deltaX); // +1, -1 or 0
        int yStep = Integer.signum(deltaY); // +1, -1 or 0

        int x = location.x + xStep;
        int y = location.y + yStep;

        // Traverse along the path from current location to the target, checking for obstructions
        while (x != target.x || y != target.y) {
            Piece p = ChessGame.board[x][y];
            if (p != null) {
                return false; // There's a piece in the way
            }
            x += xStep;
            y += yStep;
        }

        return true; // Valid move, no obstructions
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
