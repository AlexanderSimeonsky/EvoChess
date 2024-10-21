import java.awt.*;
import javax.swing.*;

public class Rook extends Piece {
    int points = 5;
    boolean hasMoved = false;

    Rook(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {

        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }

        if (target.x == location.x || target.y == location.y) {
            //System.out.println("Check if there is blocking piece");
            if (!pieceIsOnStraightLine(target)) {
                hasMoved = true;
                return true;
            }
        }

        //System.out.println("Invalid move");
        return false;
    }

    @Override
    boolean validCapture(Point target) {
        if (location.equals(target)) {
            //System.out.println("invalid move");
            return false; // No move if the target is the same as the current location
        }
        
        return validMove(target);
    }
}
