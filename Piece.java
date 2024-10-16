import java.awt.*;

/**. */
public class Piece {
    boolean isWhite;
    int pointValue;
    Dimension position;
    String type;
    
    
    /**. */
    Piece(boolean isWhite, int pointValue, Dimension position, String type) {
        this.isWhite = isWhite;
        this.pointValue = pointValue;
        this.position = position;
        this.type = type;
    }

    void move() {
        
    }

}
