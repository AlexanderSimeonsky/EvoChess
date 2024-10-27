import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;


/**
 * Pawn class that extends Piece.
 * Contains movement logic for the Pawn piece.
 * Also contains inner classes for the evolved forms of the Pawn piece.
 */
public class Pawn extends Piece {
    int turnDoubleMoveHappened = 0;

    /**
     * Constructor for the Pawn class.
     * @param isWhite colour of the piece (true if white, false if black)
     * @param location location of the piece on the board
     */
    Pawn(boolean isWhite, Point location) {
        super(isWhite, location);
        this.points = 1;
    }

    @Override
    public boolean validMove(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }

        if (isWhite && target.x == 0) {
            promotion();
        } else if (!isWhite && target.x == 7) {
            promotion();
        }

        if (enPassant(target)) {
            return true;
        }
        
        int deltaX = target.x - location.x;
        int deltaY = Math.abs(target.y - location.y);

        if (deltaY != 0) {
            return false;
        }
        
        if (isWhite) {
            if (location.x == 6 && deltaX == -2) {
                if (ChessGame.board[location.x - 1][location.y] == null) {
                    turnDoubleMoveHappened = ChessGame.turnCounter;
                    return true;
                }
            }

            return deltaX == -1;
        } else {
            if (location.x == 1 && deltaX == 2) {
                if (ChessGame.board[location.x + 1][location.y] == null) {
                    turnDoubleMoveHappened = ChessGame.turnCounter;
                    return true;
                }
            }

            return deltaX == 1;
        }
    }

    @Override
    public boolean validCapture(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }
        
        int deltaX = target.x - location.x;
        int deltaY = Math.abs(target.y - location.y);

        if (isWhite) {
            if (deltaX == -1 && deltaY == 1) {
                if (target.x == 0) {
                    promotion();
                }
                return true;
            }
        } else {
            if (deltaX == 1 && deltaY == 1) {
                if (target.x == 7) {
                    promotion();
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Method to handle special en passant move case.
     * @param target square where the piece wants to move
     * @return return boolean value wheter the move is valid
     */
    public boolean enPassant(Point target) {
        if (isWhite) {
            //if white en passant can only happen at row index 3
            if (location.x != 3) {
                return false;
            }

            //check for piece one row below target
            if (ChessGame.board[target.x - 1][target.y] != null) {
                if (ChessGame.board[target.x - 1][target.y] instanceof Pawn) {
                    //if piece is pawn check if it moved two squares at once and last turn
                    Pawn p = (Pawn) ChessGame.board[target.x - 1][target.y];
                    if (p.turnDoubleMoveHappened + 1 == ChessGame.turnCounter) {
                        //if all conditions are valid capture the pawn
                        JPanel temp = ChessGame.chessBoardPanel;
                        JPanel sq = (JPanel) temp.getComponent(8 * (target.x + 1) + target.y);
                        JPanel prevSq = (JPanel) temp.getComponent(target.x * 8 + target.y);

                        //remove the captured piece
                        sq.removeAll();
                        sq.putClientProperty("piece", null);
                        ChessGame.board[target.x - 1][target.y] = null;

                        // Revalidate and repaint the chessboard
                        prevSq.revalidate();
                        sq.revalidate();
                        ChessGame.chessBoardPanel.revalidate();
                        prevSq.repaint();
                        sq.repaint();

                        return true;
                    }
                }
            }

        } else {
            //if black en passant can only happen at row index 4
            if (location.x != 4) {
                return false;
            }

            //check for piece one row above target
            if (ChessGame.board[target.x + 1][target.y] != null) {
                if (ChessGame.board[target.x + 1][target.y] instanceof Pawn) {
                    //if piece is pawn check if it moved two squares at once and last turn
                    Pawn p = (Pawn) ChessGame.board[target.x + 1][target.y];
                    if (p.turnDoubleMoveHappened + 1 == ChessGame.turnCounter) {
                        //if all conditions are valid capture the pawn
                        JPanel temp = ChessGame.chessBoardPanel;
                        JPanel sq = (JPanel) temp.getComponent(8 * (target.x - 1) + target.y);
                        JPanel prevSq = (JPanel) temp.getComponent(target.x * 8 + target.y);

                        //remove the captured piece
                        sq.removeAll();
                        sq.putClientProperty("piece", null);
                        ChessGame.board[target.x + 1][target.y] = null;

                        // Revalidate and repaint the chessboard
                        prevSq.revalidate();
                        sq.revalidate();
                        ChessGame.chessBoardPanel.revalidate();
                        prevSq.repaint();
                        sq.repaint();

                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Method to handle pawn promotion.
     */
    public void promotion() {
        // Create the promotion panel
        String colour = "";
        JPanel promotionPanel = new JPanel();
        promotionPanel.setLayout(new GridLayout(4, 1));
        promotionPanel.setBounds(ChessGame.frame.getWidth() - 150, 150, 100, 400);
        promotionPanel.setBackground(new Color(149, 69, 53));
    
        // Define promotion options
        String[] pieceNames;
    
        // Determine piece names based on the color of the pawn
        if (isWhite) {
            pieceNames = new String[]{"white-bishop", "white-knight", "white-rook", "white-queen"};
            colour = "White";
        } else {
            pieceNames = new String[]{"black-bishop", "black-knight", "black-rook", "black-queen"};
            colour = "Black";
        }
    
        // Create buttons for each promotion option
        for (int i = 0; i < 4; i++) {
            int index = i;
            JButton button = new JButton();
            button.setBackground(new Color(200, 200, 200));
            button.setForeground(Color.BLACK);
            ImageIcon icon = new ImageIcon("sprites/" + colour + "/" + pieceNames[i] + ".png");
            Image img = icon.getImage();
            icon = new ImageIcon(img.getScaledInstance(105, 105, Image.SCALE_SMOOTH));
            button.setIcon(icon);
            button.setPreferredSize(new Dimension(90, 90));
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    promotePawn(pieceNames[index]);
                    // Close the promotion panel after selection
                    promotionPanel.setVisible(false);
                    ChessGame.frame.remove(promotionPanel);
                    ChessGame.frame.revalidate();
                    ChessGame.frame.repaint();
                }
            });
            promotionPanel.add(button);
        }
    
        // Add the promotion panel to the game frame
        ChessGame.frame.add(promotionPanel);
        ChessGame.frame.setVisible(true); // Ensure the frame is visible
    }

    @Override
    boolean hasLegalMoves() {
        Point moveD = new Point(isWhite ? -1 : 1, 0);
        Point[] captureD = {
            new Point(isWhite ? -1 : 1, -1), //diagonal left
            new Point(isWhite ? -1 : 1, 1), //diagonal right
        };

        //move check
        //check for piece
        Point forwardMove = new Point(location.x + moveD.x, location.y);
        if (ChessGame.board[forwardMove.x][forwardMove.y] == null) {
            if (validMove(forwardMove) && !illegalMove(forwardMove)) {
                //can move
                return true;
            }
        }

        //capture check
        for (Point d : captureD) {
            Point newPos = new Point(location.x + d.x, location.y + d.y);
            Piece occupyingPiece = ChessGame.board[newPos.x][newPos.y];
            if (occupyingPiece != null && occupyingPiece.isWhite != isWhite) {
                if (validCapture(newPos) && !illegalMove(newPos)) {
                    //can capture so can move
                    return true;
                }
            }
            
        }

        //no moves
        return false;
    }
    
    /**
     * Helper method to promote pawns.
     * @param pieceName the piece that the pawn is promoting to.
     */
    public void promotePawn(String pieceName) {
        String colour = "";
        if (isWhite) {
            colour = "White";
        } else {
            colour = "Black";
        }

        Piece newPiece;
        if (pieceName.equals("white-bishop")) {
            newPiece = new Bishop(true, location);
        } else if (pieceName.equals("white-knight")) {
            newPiece = new Knight(true, location);
        } else if (pieceName.equals("white-rook")) {
            newPiece = new Rook(true, location);
        } else {
            newPiece = new Queen(true, location);
        }
    
        // Update the chess board and remove the pawn
        ChessGame.board[location.x][location.y] = newPiece;
    
        // Update the board to reflect the new piece
        JPanel temp = ChessGame.chessBoardPanel;
        JPanel square = (JPanel) temp.getComponent(location.x * 8 + location.y);
        square.removeAll();
        square.putClientProperty("piece", newPiece);
        ImageIcon icon = new ImageIcon("sprites/" + colour + "/" + pieceName + ".png");
        Image img = icon.getImage();
        icon = new ImageIcon(img.getScaledInstance(105, 105, Image.SCALE_SMOOTH));

        JLabel pieceLabel = new JLabel(icon);
        square.add(pieceLabel);
        
        // Revalidate and repaint the square
        square.revalidate();
        square.repaint();
        newPiece.location = location;
    }

    @Override
    public Piece pieceEvolves() {
        //check if evolution criteria is met
        if (acquiredPoints >= 1) {
            SoundPlayer.playSound("sounds/evolve.wav");
            //create the evolved piece
            EvoPawn evoPawn = new EvoPawn(isWhite, location);
            ChessGame.board[location.x][location.y] = evoPawn;

            String colour;
            String pieceName;

            //remove it from the list of pieces and add the new one
            if (isWhite) {
                ChessGame.whitePieces.remove(this);
                ChessGame.whitePieces.add(evoPawn);
                colour = "White";
                pieceName = "evo-white-pawn";
            } else {
                ChessGame.blackPieces.remove(this);
                ChessGame.blackPieces.add(evoPawn);
                colour = "Black";
                pieceName = "evo-black-pawn";
            }

            try {
                JPanel temp = ChessGame.chessBoardPanel;
                JPanel square = (JPanel) temp.getComponent(location.x * 8 + location.y);
                square.removeAll();
                square.putClientProperty("piece", evoPawn);
                BufferedImage img = ImageIO.read(new File("sprites/" + colour + "/" + pieceName + ".png"));
                JLabel tutorialLabel = new JLabel(new ImageIcon(img));
                square.add(tutorialLabel);
                square.revalidate();
                square.repaint();
            } catch (IOException a) {
                a.printStackTrace();
            }

            return evoPawn;
        } 
        return null;
    }

    class EvoPawn extends Pawn {

        /**
         * Constructor for the EvoPawn class.
         * @param isWhite colour of the piece (true if white, false if black)
         * @param location location of the piece on the board
         */
        EvoPawn(boolean isWhite, Point location) {
            super(isWhite, location);
            this.points = 1;
        }

        @Override
        public boolean validMove(Point target) {
            if (location.equals(target)) {
                return false; // No move if the target is the same as the current location
            }

            if (isWhite && target.x == 0) {
                promotion();
            } else if (!isWhite && target.x == 7) {
                promotion();
            }
        
            int deltaX = Math.abs(target.x - location.x);
            int deltaY = Math.abs(target.y - location.y);

            if (deltaY != 0) {
                return false; 
            }
        
            //evo pawn move forward and backward
            //double move is impossible
            return deltaX == 1;
        }

        @Override
        public boolean validCapture(Point target) {
            if (location.equals(target)) {
                return false; // No move if the target is the same as the current location
            }
        
            int deltaX = target.x - location.x;
            int deltaY = Math.abs(target.y - location.y);

            if (isWhite) {
                if (deltaX == -1 && deltaY == 1) {
                    if (target.x == 0) {
                        promotion();
                    }
                    return true;
                }
            } else {
                if (deltaX == 1 && deltaY == 1) {
                    if (target.x == 7) {
                        promotion();
                    }
                    return true;
                }
            }

            return false;
        }

        @Override
        public Piece pieceEvolves() {
            SoundPlayer.playSound("sounds/evolve.wav");
            //check if evolution criteria is met
            if (acquiredPoints >= 1) {
                //create the evolved piece
                SuperPawn superPawn = new SuperPawn(isWhite, location);
                ChessGame.board[location.x][location.y] = superPawn;

                String colour;
                String pieceName;

                //remove it from the list of pieces and add the new one
                if (isWhite) {
                    ChessGame.whitePieces.remove(this);
                    ChessGame.whitePieces.add(superPawn);
                    colour = "White";
                    pieceName = "super-white-pawn";
                } else {
                    ChessGame.blackPieces.remove(this);
                    ChessGame.blackPieces.add(superPawn);
                    colour = "Black";
                    pieceName = "super-black-pawn";
                }   

                try {
                    JPanel temp = ChessGame.chessBoardPanel;
                    JPanel square = (JPanel) temp.getComponent(location.x * 8 + location.y);
                    square.removeAll();
                    square.putClientProperty("piece", superPawn);
                    BufferedImage img = ImageIO.read(new File("sprites/" + colour + "/" + pieceName + ".png"));
                    JLabel tutorialLabel = new JLabel(new ImageIcon(img));
                    square.add(tutorialLabel);
                    square.revalidate();
                    square.repaint();
                } catch (IOException a) {
                    a.printStackTrace();
                }

                return superPawn;
            } 
            return null;
        }

        class SuperPawn extends EvoPawn {

            /**
             * Constructor for the SuperPawn class.
             * @param isWhite colour of the piece (true if white, false if black)
             * @param location location of the piece on the board
             */
            SuperPawn(boolean isWhite, Point location) {
                super(isWhite, location);
                this.points = 1;
            }
    
            @Override
            public boolean validMove(Point target) {
                if (location.equals(target)) {
                    return false; // No move if the target is the same as the current location
                }
    
                if (isWhite && target.x == 0) {
                    promotion();
                } else if (!isWhite && target.x == 7) {
                    promotion();
                }
            
                int deltaX = Math.abs(target.x - location.x);
                int deltaY = Math.abs(target.y - location.y);
    
                //double move impossible
                //can move one square up, down, left, right
                return (deltaX == 1 || deltaY == 1) && (deltaX + deltaY < 2);
            }
    
            @Override
            public boolean validCapture(Point target) {
                if (location.equals(target)) {
                    return false; // No move if the target is the same as the current location
                }
            
                int deltaX = target.x - location.x;
                int deltaY = Math.abs(target.y - location.y);
    
                if (isWhite) {
                    if (deltaX == -1 && deltaY == 1) {
                        if (target.x == 0) {
                            promotion();
                        }
                        return true;
                    }
                } else {
                    if (deltaX == 1 && deltaY == 1) {
                        if (target.x == 7) {
                            promotion();
                        }
                        return true;
                    }
                }
    
                return false;
            }

            @Override
            public Piece pieceEvolves() {
                //can't evolve anymore
                return null;
            }
        }
    }
}
