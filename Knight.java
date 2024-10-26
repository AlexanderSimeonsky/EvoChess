import java.awt.*;

public class Knight extends Piece{

    Knight(boolean isWhite, Point location) {
        super(isWhite, location);
        this.points = 3;
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
            }
        }

        //no moves
        return false;
    }

    @Override
    public Piece pieceEvolves() {
        if (acquiredPoints >= 2) {
            //create the evolved piece
            EvoKnight evoKnight = new EvoKnight(isWhite, location);
            ChessGame.board[location.x][location.y] = evoKnight;

            //remove it from the list of pieces and add the new one
            if (isWhite) {
                ChessGame.whitePieces.remove(this);
                ChessGame.whitePieces.add(evoKnight);
                return evoKnight;
            } else {
                ChessGame.blackPieces.remove(this);
                ChessGame.blackPieces.add(evoKnight);
                return evoKnight;
            }
        } 
        return null;
    }

    class EvoKnight extends Knight {
        EvoKnight(boolean isWhite, Point location) {
            super(isWhite, location);
            this.points = 3;
        }

        @Override
        public boolean validMove(Point target) {
            if (location.equals(target)) {
                return false; // No move if the target is the same as the current location
            }

            int deltaX = Math.abs(target.x - location.x);
            int deltaY = Math.abs(target.y - location.y);

            //normal knight move
            if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
                return true;
            }

            //new moves 3x2
            if ((deltaX == 3 && deltaY == 2) || (deltaX == 2 && deltaY == 3)) {
                return true;
            }

            return false;
        }

        @Override
        public boolean validCapture(Point target) {
            if (location.equals(target)) {
                return false; // No move if the target is the same as the current location
            }
            
            return validMove(target);
        }
    }
}
