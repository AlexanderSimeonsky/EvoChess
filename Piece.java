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


    public boolean validMove(Point location) {
        return true;
    }

    public boolean validCapture(Point location) {
        return true;
    }

    public void move(Point target) {
        location = target;
    }

}
