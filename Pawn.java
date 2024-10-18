import javax.swing.*;
import java.awt.*;

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

        if (enPassant(target)) {
            System.out.println("Enpassant successful");
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
                return true;
            }
        } else {
            if (deltaX == 1 && deltaY == 1) {
                return true;
            }
        }

        return false;
    }

    public boolean enPassant(Point target) {
        if (isWhite) {
            //if white en passant can only happen at row index 3
            if (location.x != 3) {
                System.out.println("white can only enpassant on row index 3");
                return false;
            }

            //check for piece one row below target
            System.out.println("//check for piece one row below target");
            if (ChessGame.board[target.x - 1][target.y] != null) {
                if (ChessGame.board[target.x - 1][target.y] instanceof Pawn) {
                    //if piece is pawn check if it moved two squares at once and last turn
                    System.out.println("//if piece is pawn check if it moved two squares at once and last turn");
                    Pawn p = (Pawn) ChessGame.board[target.x - 1][target.y];
                    if (p.turnDoubleMoveHappened + 1 == ChessGame.turnCounter) {
                        //if all conditions are valid capture the pawn
                        System.out.println("//if all conditions are valid capture the pawn");
                        JPanel temp = ChessGame.chessBoardPanel;
                        JPanel sq = (JPanel) temp.getComponent(8 * (target.x + 1) + target.y);
                        JPanel prevSq = (JPanel) temp.getComponent(target.x * 8 + target.y);

                        //remove the captured piece
                        System.out.println("//remove the captured piece");
                        sq.removeAll();
                        sq.putClientProperty("piece", null);
                        ChessGame.board[target.x - 1][target.y] = null;

                        // Revalidate and repaint the chessboard
                        System.out.println("// Revalidate and repaint the chessboard");
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
            System.out.println("");
            if (location.x != 4) {
                System.out.println("black can only enpassant on row index 4");
                return false;
            }

            //check for piece one row above target
            System.out.println("//check for piece one row above target");
            if (ChessGame.board[target.x + 1][target.y] != null) {
                if (ChessGame.board[target.x + 1][target.y] instanceof Pawn) {
                    //if piece is pawn check if it moved two squares at once and last turn
                    System.out.println("//if piece is pawn check if it moved two squares at once and last turn");
                    Pawn p = (Pawn) ChessGame.board[target.x + 1][target.y];
                    if (p.turnDoubleMoveHappened + 1 == ChessGame.turnCounter) {
                        //if all conditions are valid capture the pawn
                        System.out.println("//if all conditions are valid capture the pawn");
                        JPanel temp = ChessGame.chessBoardPanel;
                        JPanel sq = (JPanel) temp.getComponent(8 * (target.x - 1) + target.y);
                        JPanel prevSq = (JPanel) temp.getComponent(target.x * 8 + target.y);

                        //remove the captured piece
                        System.out.println("//remove the captured piece");
                        sq.removeAll();
                        sq.putClientProperty("piece", null);
                        ChessGame.board[target.x + 1][target.y] = null;

                        // Revalidate and repaint the chessboard
                        System.out.println("// Revalidate and repaint the chessboard");
                        prevSq.revalidate();
                        sq.revalidate();
                        ChessGame.chessBoardPanel.revalidate();
                        prevSq.repaint();
                        sq.repaint();

                        return true;
                    } else {
                        System.out.println("turndoublemoved is different");
                    }
                } else {
                    System.out.println("not a pawn");
                }
            } else {
                System.out.println("square is null " + ChessGame.board[target.x + 1][target.y]);
            }
        }

        System.out.println("invalid enpassant");
        return false;
    }

    public boolean promotion(Point target) {

        return false;
    }
}
