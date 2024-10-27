import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Bishop class that extends Piece.
 * Contains movement logic for the Bishop piece.
 * Also contains inner classes for the evolved forms of the Bishop piece.
 */
public class Bishop extends Piece {

    /**
     * Constructor for the bishop.
     * @param isWhite colour of the piece (true if white, false if black)
     * @param location location of the piece on the board
     */
    Bishop(boolean isWhite, Point location) {
        super(isWhite, location);
        this.points = 3;
    }

    @Override
    boolean validMove(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }

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
            new Point(1, 1), //down right
            new Point(1, -1), //down left
            new Point(-1, 1), //up right
            new Point(-1, -1) //down left
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

            SoundPlayer.playSound("sounds/evolve.wav");
            //create the evolved piece
            EvoBishop evoBishop = new EvoBishop(isWhite, location);
            ChessGame.board[location.x][location.y] = evoBishop;

            //remove it from the list of pieces and add the new one
            if (isWhite) {
                ChessGame.whitePieces.remove(this);
                ChessGame.whitePieces.add(evoBishop);
                return evoBishop;
            } else {
                ChessGame.blackPieces.remove(this);
                ChessGame.blackPieces.add(evoBishop);
                return evoBishop;
            }
        } 
        return null;
    }

    class EvoBishop extends Bishop {

        /**
         * Constructor for the evolved bishop.
         * @param isWhite colour of the piece (true if white, false if black)
         * @param location location of the piece on the board
         */
        EvoBishop(boolean isWhite, Point location) {
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

            //normal bishop movement
            if (deltaY == deltaX) {
                if (!pieceIsOnDiagonalLine(target)) {
                    return true;
                }
            }

            //move like a king
            if ((deltaX <= 1 && deltaY <= 1 && (deltaX + deltaY) > 0)) {        
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

        @Override
        public Piece pieceEvolves() {
            SoundPlayer.playSound("sounds/evolve.wav");
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