import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Queen class that extends Piece.
 * Contains movement logic for the Queen piece.
 * Also contains inner classes for the evolved forms of the Queen piece.
 */
public class Queen extends Piece {

    /**
     * Constructor for the Queen class.
     * @param isWhite colour of the piece (true if white, false if black)
     * @param location location of the piece on the board
     */
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
            SoundPlayer.playSound("sounds/evolve.wav");
            //create the evolved piece
            EvoQueen evoQueen = new EvoQueen(isWhite, location);
            ChessGame.board[location.x][location.y] = evoQueen;
            String pieceName = "";
            String colour = "";

            //remove it from the list of pieces and add the new one
            if (isWhite) {
                ChessGame.whitePieces.remove(this);
                ChessGame.whitePieces.add(evoQueen);
                colour = "White";
                pieceName = "evo-white-queen";
            } else {
                ChessGame.blackPieces.remove(this);
                ChessGame.blackPieces.add(evoQueen);
                colour = "Black";
                pieceName = "evo-black-queen";
            }

            try {
                JPanel temp = ChessGame.chessBoardPanel;
                JPanel square = (JPanel) temp.getComponent(location.x * 8 + location.y);
                square.removeAll();
                square.putClientProperty("piece", evoQueen);
                BufferedImage img = ImageIO.read(new File("sprites/" + colour + "/" + pieceName + ".png"));
                JLabel tutorialLabel = new JLabel(new ImageIcon(img));
                square.add(tutorialLabel);
                square.revalidate();
                square.repaint();
            } catch (IOException a) {
                a.printStackTrace();
            }

            return evoQueen;
        } 
        return null;
    }

    class EvoQueen extends Queen {

        /**
         * Constructor for the EvoQueen class.
         * @param isWhite colour of the piece (true if white, false if black)
         * @param location location of the piece on the board
         */
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

        @Override
        public Piece pieceEvolves() { 

            if (acquiredPoints >= 6) {
                SoundPlayer.playSound("sounds/evolve.wav");
                //create the evolved piece
                SuperQueen superQueen = new SuperQueen(isWhite, location);
                ChessGame.board[location.x][location.y] = superQueen;
                String colour = "";
                String pieceName = "";

                //remove it from the list of pieces and add the new one
                if (isWhite) {
                    ChessGame.whitePieces.remove(this);
                    ChessGame.whitePieces.add(superQueen);
                    colour = "White";
                    pieceName = "super-white-queen";
                } else {
                    ChessGame.blackPieces.remove(this);
                    ChessGame.blackPieces.add(superQueen);
                    colour = "Black";
                    pieceName = "super-black-queen";
                }

                try {
                    JPanel temp = ChessGame.chessBoardPanel;
                    JPanel square = (JPanel) temp.getComponent(location.x * 8 + location.y);
                    square.removeAll();
                    square.putClientProperty("piece", superQueen);
                    BufferedImage img = ImageIO.read(new File("sprites/" + colour + "/" + pieceName + ".png"));
                    JLabel tutorialLabel = new JLabel(new ImageIcon(img));
                    square.add(tutorialLabel);
                    square.revalidate();
                    square.repaint();
                } catch (IOException a) {
                    a.printStackTrace();
                }
    
                return superQueen;
            } 
            return null;
        }

        class SuperQueen extends EvoQueen {

            /**
             * Constructor for the SuperQueen class.
             * @param isWhite colour of the piece (true if white, false if black)
             * @param location location of the piece on the board
             */
            SuperQueen(boolean isWhite, Point location) {
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
    
                //evo moves like a knight
                int deltaX = Math.abs(target.x - location.x);
                int deltaY = Math.abs(target.y - location.y);
    
                if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
                    return true;
                }

                //super moves anywhere on an empty square
                ArrayList<Piece> pieces = new ArrayList<Piece>();
                for (Piece piece : ChessGame.whitePieces) {
                    pieces.add(piece);
                }
                for (Piece piece : ChessGame.blackPieces) {
                    pieces.add(piece);
                }

                for (Piece piece : pieces) {
                    if (piece.location.equals(target)) {
                        return false;
                    }
                }
    
                return true;
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
                //can't evolve anymore
                return null;
            }
            
        }
    }
}
