import javax.swing.*;
import java.awt.*;

public class Temp {
    
    String[][] gridArray;

    void initializeChessGridArray() {
        gridArray = new String[][] {
            {"r", "n", "b", "q", "k", "b", "n", "r"},
            {"p", "p", "p", "p", "p", "p", "p", "p"},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"p", "p", "p", "p", "p", "p", "p", "p"},
            {"r", "n", "b", "q", "k", "b", "n", "r"}
        };
    }

    public boolean move(int rowOrigin, int colOrigin, int rowTarget, int colTarget, boolean isWhite) {
        

        if (isWhite) {
            if (rowOrigin == 6) {
                return rowTarget <= rowOrigin + 2 && rowTarget > rowOrigin;
            } else {
                return rowTarget <= rowOrigin + 1 && rowTarget > rowOrigin;
            }
        } else {
            if (rowOrigin == 1) {
                return rowTarget <= rowOrigin + 2 && rowTarget > rowOrigin;
            } else {
                return rowTarget <= rowOrigin + 1 && rowTarget > rowOrigin;
            }
        }
    }
}
