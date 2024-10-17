import java.awt.*;

/**. */
public class Piece {  

    boolean isWhite;
    Point location;
    
    /**. */
    Piece(boolean isWhite, Point location) {
        this.isWhite = isWhite;
        this.location = location;
    }


    boolean validMove(Point target) {
        return true;
    }

    boolean validCapture(Point target) {
        return true;
    }

    void move(Point target) {
        location = target;
    }

}
