import java.awt.*;
import javax.swing.*;

public class Rook extends Piece {
    boolean hasMoved = false;

    Rook(boolean isWhite, Point location) {
        super(isWhite, location);
        this.points = 5;
    }

    @Override
    boolean validMove(Point target) {

        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }

        if (target.x == location.x || target.y == location.y) {
            if (!pieceIsOnStraightLine(target)) {
                hasMoved = true;
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
        if (acquiredPoints >= 2) {
            //create the evolved piece
            EvoRook evoRook = new EvoRook(isWhite, location);
            ChessGame.board[location.x][location.y] = evoRook;

            //remove it from the list of pieces and add the new one
            if (isWhite) {
                ChessGame.whitePieces.remove(this);
                ChessGame.whitePieces.add(evoRook);
                return evoRook;
            } else {
                ChessGame.blackPieces.remove(this);
                ChessGame.blackPieces.add(evoRook);
                return evoRook;
            }
        } 
        return null;
    }

    class EvoRook extends Rook {
        EvoRook(boolean isWhite, Point location) {
            super(isWhite, location);
            this.points = 5;
            this.acquiredPoints = acquiredPoints;
        }

        @Override
        boolean validMove(Point target) {
            if (location.equals(target)) {
                return false; // No move if the target is the same as the current location
            }

            //normal rook moves
            if (target.x == location.x || target.y == location.y) {
                if (!pieceIsOnStraightLine(target)) {
                    hasMoved = true;
                    return true;
                }
            }

            //new moves 1 square diagonally
            int deltaX = Math.abs(target.x - location.x);
            int deltaY = Math.abs(target.y - location.y);

            if (deltaX == 1 && deltaY == 1) {
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

        @Override
        public Piece pieceEvolves() {
            if (acquiredPoints >= 4) {
                //create the evolved piece
                Queen queen = new Queen(isWhite, location);
                ChessGame.board[location.x][location.y] = queen;
                String pieceName = "";
                String colour = "";

                //remove it from the list of pieces and add the new one
                if (isWhite) {
                    ChessGame.whitePieces.remove(this);
                    ChessGame.whitePieces.add(queen);
                    colour = "White";
                    pieceName = "white-queen";
                } else {
                    ChessGame.blackPieces.remove(this);
                    ChessGame.blackPieces.add(queen);
                    colour = "Black";
                    pieceName = "black-queen";
                }

                JPanel temp = ChessGame.chessBoardPanel;
                JPanel square = (JPanel) temp.getComponent(location.x * 8 + location.y);
                square.removeAll();
                square.putClientProperty("piece", queen);
                ImageIcon icon = new ImageIcon("sprites/" + colour + "/" + pieceName + ".png");
                Image img = icon.getImage();
                icon = new ImageIcon(img.getScaledInstance(105, 105, Image.SCALE_SMOOTH));

                JLabel pieceLabel = new JLabel(icon);
                square.add(pieceLabel);
        
                // Revalidate and repaint the square
                square.revalidate();
                square.repaint();

                return queen;
            } 
            return null;
        }
    }
}
