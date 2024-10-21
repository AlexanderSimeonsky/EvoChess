import java.awt.*;

public class Knight extends Piece{
    int points = 3;

    Knight(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {
        //System.out.println("test move");
        if (location.equals(target)) {
            //System.out.println("invalid move location same as target");
            return false; // No move if the target is the same as the current location
        }

        int deltaX = Math.abs(target.x - location.x);
        int deltaY = Math.abs(target.y - location.y);

        if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
            //System.out.println("VALID move");
            return true;
        }

        //System.out.println("invalid move");
        return false;
    }

    @Override
    boolean validCapture(Point target) {
        if (location.equals(target)) {
            //System.out.println("invalid move same target is the same as location");
            return false; // No move if the target is the same as the current location
        }
        
        return validMove(target);
    }

    @Override
    boolean hasLegalMoves() {
        Point[] directions = {
            new Point(2, 1),
            new Point(2, -1),
            new Point(-2, 1),
            new Point(-2, -1),
            new Point(1, 2),
            new Point(1, -2),
            new Point(-1, 2),
            new Point(-1, -2) 
        };

        for (Point d : directions) {
            Point currPos = location;

            while (true) {
                Point newPos = new Point(currPos.x + d.x, currPos.y + d.y);
                if (!ChessGame.ChessMouseListener.pointInBoard(newPos)) {
                    //position not in board
                    break;
                }

                //check if position is occupied
                Piece occupyingPiece = ChessGame.board[newPos.x][newPos.y];
                if (occupyingPiece != null) {
                    //check colour of piece
                    if (occupyingPiece.isWhite != isWhite) {
                        if (validCapture(d) && !ChessGame.ChessMouseListener.illegalMove(d, this)) {
                            //can capture so can move
                            return true;
                        }
                    }
                    break;
                } else {
                    if (validMove(d) && !ChessGame.ChessMouseListener.illegalMove(d, this)) {
                        //can move
                        return true;
                    }
                }
            }
        }

        //no moves
        return false;
    }
}
