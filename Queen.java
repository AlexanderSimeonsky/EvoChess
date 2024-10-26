import java.awt.*;

public class Queen extends Piece {

    Queen(boolean isWhite, Point location) {
        super(isWhite, location);
        this.points = 9;
    }

    @Override
    boolean validMove(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }

        //Vertical and Horizontal movement
        if (target.x == location.x || target.y == location.y) {
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
        return false;
    }

    @Override
    boolean validCapture(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }
        
        return validMove(target);
    }

    @Override
    boolean hasLegalMoves() {
        Point[] directions = {
            //diagonal moves
            new Point(1, 1), //down right
            new Point(1, -1), //down left
            new Point(-1, 1), //up right
            new Point(-1, -1), //down left
            //straight line moves
            new Point(1, 0), //down
            new Point(-1, 0), //up
            new Point(0, 1), //right
            new Point(0, -1) //left
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
                        if (validCapture(d) && !illegalMove(d)) {
                            //can capture so can move
                            return true;
                        }
                    }
                    break;
                } else {
                    if (validMove(d) && !illegalMove(d)) {
                        //can move
                        return true;
                    }
                }

                currPos = newPos;
            }
        }

        //no moves
        return false;
    }

    @Override
    public Piece pieceEvolves() {
        if (acquiredPoints >= 4) {
            //create the evolved piece
            EvoQueen evoQueen = new EvoQueen(isWhite, location);
            ChessGame.board[location.x][location.y] = evoQueen;

            //remove it from the list of pieces and add the new one
            if (isWhite) {
                ChessGame.whitePieces.remove(this);
                ChessGame.whitePieces.add(evoQueen);
                return evoQueen;
            } else {
                ChessGame.blackPieces.remove(this);
                ChessGame.blackPieces.add(evoQueen);
                return evoQueen;
            }
        } 
        return null;
    }

    class EvoQueen extends Queen {
        EvoQueen(boolean isWhite, Point location) {
            super(isWhite, location);
            this.points = 9;
        }

        @Override
        boolean validMove(Point target) {
            if (location.equals(target)) {
                return false; // No move if the target is the same as the current location
            }

            //Vertical and Horizontal movement
            if (target.x == location.x || target.y == location.y) {
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

            //new moves like a knight
            int deltaX = Math.abs(target.x - location.x);
            int deltaY = Math.abs(target.y - location.y);

            if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
                return true;
            }

            return false;
        }

        @Override
        boolean validCapture(Point target) {
            if (location.equals(target)) {
                return false; // No move if the target is the same as the current location
            }
        
            return validMove(target);
        }
    }
}
