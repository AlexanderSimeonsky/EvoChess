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

        //Vertical and Horizontal movement
        if (target.x == location.x || target.y == location.y) {
            //System.out.println("Check if there is blocking piece");
            if (!pieceIsOnStraightLine(target)) {
                return true;
            }
        }

        //Diagonal movement
        if (Math.abs(target.y - location.y) == Math.abs(target.x - location.x)) {
            if (!pieceIsOnDiagonalLine(target)) {
                return true;
            }
        }
        //System.out.println("Invalid move");
        return false;
    }

    @Override
    boolean validCapture(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }
        
        //System.out.println("check valid move move");
        return validMove(target);
    }
}
