import java.awt.*;

public class Bishop extends Piece {
    int points = 3;

    Bishop(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {
        if (location.equals(target)) {
            System.out.println("Invalid move: Target is the same as current location");
            return false; // No move if the target is the same as the current location
        }

        int deltaX = target.x - location.x;
        int deltaY = target.y - location.y;

        // Check if the move is along a diagonal
        if (Math.abs(deltaX) != Math.abs(deltaY)) {
            System.out.println("Invalid move: Not a diagonal move");
            return false;
        }

        // Calculate the direction of movement
        int xStep = Integer.signum(deltaX);
        int yStep = Integer.signum(deltaY);

        // Check all squares between the current location and the target
        int x = location.x + xStep;
        int y = location.y + yStep;
        while (x != target.x && y != target.y) {
            Piece p = ChessGame.board[x][y];
            if (p != null) {
                System.out.println("Invalid move: Piece blocking the path at (" + x + ", " + y + ")");
                return false; // There is a piece in the way
            }
            x += xStep;
            y += yStep;
        }

        // If we reach here, it's a valid move
        System.out.println("Valid move from (" + location.x + ", " + location.y + ") to (" + target.x + ", " + target.y + ")");
        return true;
    }

    @Override
    boolean validCapture(Point target) {
        // Bishop can only capture on valid moves, so re-use validMove logic
        return validMove(target);
    }
}
