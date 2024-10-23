import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Pawn class to handle the behaviour of pawns.
 */
public class Pawn extends Piece {
    int points = 1;
    int turnDoubleMoveHappened = 0;

    Pawn(boolean isWhite, Point location) {
        super(isWhite, location);
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
            //System.out.println("Enpassant successful");
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
                //System.out.println("white can only enpassant on row index 3");
                return false;
            }

            //check for piece one row below target
            //System.out.println("//check for piece one row below target");
            if (ChessGame.board[target.x - 1][target.y] != null) {
                if (ChessGame.board[target.x - 1][target.y] instanceof Pawn) {
                    //if piece is pawn check if it moved two squares at once and last turn
                    //System.out.println("//if pawn check if it moved two squares last turn");
                    Pawn p = (Pawn) ChessGame.board[target.x - 1][target.y];
                    if (p.turnDoubleMoveHappened + 1 == ChessGame.turnCounter) {
                        //if all conditions are valid capture the pawn
                        //System.out.println("//if all conditions are valid capture the pawn");
                        JPanel temp = ChessGame.chessBoardPanel;
                        JPanel sq = (JPanel) temp.getComponent(8 * (target.x + 1) + target.y);
                        JPanel prevSq = (JPanel) temp.getComponent(target.x * 8 + target.y);

                        //remove the captured piece
                        //System.out.println("//remove the captured piece");
                        sq.removeAll();
                        sq.putClientProperty("piece", null);
                        ChessGame.board[target.x - 1][target.y] = null;

                        // Revalidate and repaint the chessboard
                        //System.out.println("// Revalidate and repaint the chessboard");
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
            //System.out.println("");
            if (location.x != 4) {
                //System.out.println("black can only enpassant on row index 4");
                return false;
            }

            //check for piece one row above target
            //System.out.println("//check for piece one row above target");
            if (ChessGame.board[target.x + 1][target.y] != null) {
                if (ChessGame.board[target.x + 1][target.y] instanceof Pawn) {
                    //if piece is pawn check if it moved two squares at once and last turn
                    //System.out.println("//if pawn check if it moved two squares last turn");
                    Pawn p = (Pawn) ChessGame.board[target.x + 1][target.y];
                    if (p.turnDoubleMoveHappened + 1 == ChessGame.turnCounter) {
                        //if all conditions are valid capture the pawn
                        //System.out.println("//if all conditions are valid capture the pawn");
                        JPanel temp = ChessGame.chessBoardPanel;
                        JPanel sq = (JPanel) temp.getComponent(8 * (target.x - 1) + target.y);
                        JPanel prevSq = (JPanel) temp.getComponent(target.x * 8 + target.y);

                        //remove the captured piece
                        //System.out.println("//remove the captured piece");
                        sq.removeAll();
                        sq.putClientProperty("piece", null);
                        ChessGame.board[target.x + 1][target.y] = null;

                        // Revalidate and repaint the chessboard
                        //System.out.println("// Revalidate and repaint the chessboard");
                        prevSq.revalidate();
                        sq.revalidate();
                        ChessGame.chessBoardPanel.revalidate();
                        prevSq.repaint();
                        sq.repaint();

                        return true;
                    } else {
                        //System.out.println("turndoublemoved is different");
                    }
                } else {
                    //System.out.println("not a pawn");
                }
            } else {
                //System.out.println("square is null " + ChessGame.board[target.x + 1][target.y]);
            }
        }

        //System.out.println("invalid enpassant");
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
}
