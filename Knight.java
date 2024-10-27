import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Knight class that extends Piece.
 */
public class Knight extends Piece {

    /**
     * Constructor for the Knight class.
     * @param isWhite colour of the piece (true if white, false if black)
     * @param location location of the piece on the board
     */
    Knight(boolean isWhite, Point location) {
        super(isWhite, location);
        this.points = 3;
    }

    @Override
    boolean validMove(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }

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
            String colour;
            String pieceName;

            //remove it from the list of pieces and add the new one
            if (isWhite) {
                ChessGame.whitePieces.remove(this);
                ChessGame.whitePieces.add(evoKnight);
                colour = "White";
                pieceName = "evo-white-knight";
            } else {
                ChessGame.blackPieces.remove(this);
                ChessGame.blackPieces.add(evoKnight);
                colour = "Black";
                pieceName = "evo-black-knight";
            }

            try {
                JPanel temp = ChessGame.chessBoardPanel;
                JPanel square = (JPanel) temp.getComponent(location.x * 8 + location.y);
                square.removeAll();
                square.putClientProperty("piece", evoKnight);
                BufferedImage img = ImageIO.read(new File("sprites/" + colour + "/" + pieceName + ".png"));
                JLabel tutorialLabel = new JLabel(new ImageIcon(img));
                square.add(tutorialLabel);
                square.revalidate();
                square.repaint();
            } catch (IOException a) {
                a.printStackTrace();
            }

            return evoKnight;
        } 
        return null;
    }

    class EvoKnight extends Knight {

        /**
         * Constructor for the EvoKnight class.
         * @param isWhite colour of the piece (true if white, false if black)
         * @param location location of the piece on the board
         */
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

        @Override
        public Piece pieceEvolves() {
            if (acquiredPoints >= 4) {
                //create the evolved piece
                SuperKnight superKnight = new SuperKnight(isWhite, location);
                ChessGame.board[location.x][location.y] = superKnight;
                SoundPlayer.playSound("sounds/evolve.wav");

                String colour;
                String pieceName;

                //remove it from the list of pieces and add the new one
                if (isWhite) {
                    ChessGame.whitePieces.remove(this);
                    ChessGame.whitePieces.add(superKnight);
                    colour = "White";
                    pieceName = "super-white-knight";
                } else {
                    ChessGame.blackPieces.remove(this);
                    ChessGame.blackPieces.add(superKnight);
                    colour = "Black";
                    pieceName = "super-black-knight";
                }

                try {
                    JPanel temp = ChessGame.chessBoardPanel;
                    JPanel square = (JPanel) temp.getComponent(location.x * 8 + location.y);
                    square.removeAll();
                    square.putClientProperty("piece", superKnight);
                    BufferedImage img = ImageIO.read(new File("sprites/" + colour + "/" + pieceName + ".png"));
                    JLabel tutorialLabel = new JLabel(new ImageIcon(img));
                    square.add(tutorialLabel);
                    square.revalidate();
                    square.repaint();
                } catch (IOException a) {
                    a.printStackTrace();
                }

                return superKnight;
            } 
            return null;
        }

        class SuperKnight extends EvoKnight {

            /**
             * Constructor for the SuperKnight class.
             * @param isWhite colour of the piece (true if white, false if black)
             * @param location location of the piece on the board
             */
            SuperKnight(boolean isWhite, Point location) {
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
    
                //evo moves 3x2
                if ((deltaX == 3 && deltaY == 2) || (deltaX == 2 && deltaY == 3)) {
                    return true;
                }

                //super moves 4x3
                if ((deltaX == 4 && deltaY == 3) || (deltaX == 3 && deltaY == 4)) {
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
                //can't evolve anymore
                return null;
            }
        }
    }
}
