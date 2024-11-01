import java.awt.*;
import javax.swing.*;

/**
 * King class to handle the behaviour of kings.
 */
public class King extends Piece {
    int points = 0;
    boolean hasMoved = false;

    King(boolean isWhite, Point location) {
        super(isWhite, location);
    }

    @Override
    boolean validMove(Point target) {
        if (location.equals(target)) {
            return false; // No move if the target is the same as the current location
        }

        if (!hasMoved && Math.abs(target.y - location.y) == 2) {
            if (castle(target)) {
                return true;
            }
        }

        int deltaX = Math.abs(target.x - location.x);
        int deltaY = Math.abs(target.y - location.y);

        // Check if the move is one square away in any direction
        if ((deltaX <= 1 && deltaY <= 1 && (deltaX + deltaY) > 0)) {
            hasMoved = true;

            //update king location
            if (isWhite) {
                ChessGame.whiteKingLocation = this.location;
            } else {
                ChessGame.blackKingLocation = this.location;
            }
            
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

    /**
     * Method to handle the special move of castling.
     * @param target the squre the piece wants to move to
     * @return returns if the move is valid
     */
    boolean castle(Point target) {
        Piece p;
        boolean rookIsLeftOfKing;

        if (target.y < location.y) {
            //castle left
            if (isWhite) {
                p = ChessGame.board[7][0];
            } else {
                p = ChessGame.board[0][0];
            }
            rookIsLeftOfKing = true;
        } else {
            //caslte right
            if (isWhite) {
                p = ChessGame.board[7][7];
            } else {
                p = ChessGame.board[0][7];
            }
            rookIsLeftOfKing = false;
        }

        if (p instanceof Rook) {
            Rook r = (Rook) p;
            if (!r.hasMoved) {
                JPanel temp = ChessGame.chessBoardPanel;
                JPanel rOrigin;
                JPanel rTarget;

                //if piece at corner is rook and hasnt moved then check if piece is blocking
                if (!pieceIsOnStraightLine(target)) {
                    //if no errors check if rook is left of king
                    if (rookIsLeftOfKing) {
                        //move three squares right
                        rOrigin = (JPanel) temp.getComponent(r.location.x * 8 + r.location.y);
                        rTarget = (JPanel) temp.getComponent(r.location.x * 8 + r.location.y + 3);

                        //move rook to new square
                        rTarget.putClientProperty("piece", r);
                        rTarget.add(rOrigin.getComponent(0));
                        ChessGame.board[r.location.x][r.location.y + 3] = r;
                        r.move(new Point(r.location.x, r.location.y + 3));

                        //remove it from previous square
                        rOrigin.removeAll();
                        rOrigin.putClientProperty("piece", null);
                        ChessGame.board[r.location.x][r.location.y] = null;

                        //update king location
                        if (isWhite) {
                            ChessGame.whiteKingLocation = this.location;
                        } else {
                            ChessGame.blackKingLocation = this.location;
                        }

                        //revalidate and repaint
                        rOrigin.revalidate();
                        rTarget.revalidate();
                        ChessGame.chessBoardPanel.revalidate();
                        rOrigin.repaint();
                        rTarget.repaint();
                    } else {
                        //move two squares left
                        rOrigin = (JPanel) temp.getComponent(r.location.x * 8 + r.location.y);
                        rTarget = (JPanel) temp.getComponent(r.location.x * 8 + r.location.y - 2);

                        //move rook to new square
                        rTarget.putClientProperty("piece", r);
                        rTarget.add(rOrigin.getComponent(0));
                        ChessGame.board[r.location.x][r.location.y - 2] = r;
                        r.move(new Point(r.location.x, r.location.y - 2));

                        //remove it from previous square
                        rOrigin.removeAll();
                        rOrigin.putClientProperty("piece", null);
                        ChessGame.board[r.location.x][r.location.y] = null;

                        //update king location
                        if (isWhite) {
                            ChessGame.whiteKingLocation = this.location;
                        } else {
                            ChessGame.blackKingLocation = this.location;
                        }

                        //revalidate and repaint
                        rOrigin.revalidate();
                        rTarget.revalidate();
                        ChessGame.chessBoardPanel.revalidate();
                        rOrigin.repaint();
                        rTarget.repaint();
                    }


                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method to check if any piece is checking the king.
     * @return returns if the king is in check
     */
    public boolean isInCheck() {
        boolean check = false;
        for (Piece p : isWhite ? ChessGame.blackPieces : ChessGame.whitePieces) {
            if (p.validCapture(location)) {
                check = true;
                break;
            }
        }
        if (check) {
            SoundPlayer.playSound("sounds/Check.wav");
        }
        return check;
    }

    /**
     * Method to check if any piece is checking the king without checking a specific piece.
     * @param p2 the piece to ignore
     * @return returns if the king is in check
     */
    public boolean isInCheck(Piece p2) {
        boolean check = false;
        for (Piece p : isWhite ? ChessGame.blackPieces : ChessGame.whitePieces) {
            if (p.validCapture(location) && p != p2) {
                check = true;
                break;
            }        
        }
        if (check) {
            SoundPlayer.playSound("sounds/Check.wav");
        }

        return check;
    }

    /**
     * Method to get the piece that is checking the king.
     * @return the piece that is checking the king
     */
    public Piece getCheckingPiece() {
        for (Piece[] pieces : ChessGame.board) {
            for (Piece p : pieces) {
                if (p != null && p.isWhite != isWhite) {
                    if (p.validCapture(location) && !p.illegalMove(location)) {
                        return p;
                    }
                }
            }
        }

        return null;
    }
}
