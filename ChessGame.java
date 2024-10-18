import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class ChessGame {
    public static JPanel chessBoardPanel; //visual board
    private Piece selectedPiece = null;
    private JPanel previousSquare = null;  
    private int pieceSize = 105;
    public static Piece[][] board; //logical board
    public static int turnCounter = 0;

    public ChessGame() {
        board = new Piece[8][8];
        renderChessboard();
        renderPieces();
    }

    private void renderChessboard() {
        Color brown = new Color(149, 69, 53);
        Color black = new Color(50, 50, 50);
        Color white = new Color(200, 200, 200);

        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(brown);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);
        frame.setLayout(null);

        chessBoardPanel = new JPanel();
        chessBoardPanel.setLayout(new GridLayout(8, 8));

        boolean isWhite = true;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.setBackground(isWhite ? white : black);
                panel.addMouseListener(new ChessMouseListener());

                chessBoardPanel.add(panel);
                isWhite = !isWhite;
            }
            isWhite = !isWhite;
        }

        chessBoardPanel.setBounds(50, 50, 600, 600);
        frame.add(chessBoardPanel);

        JButton restartButton = new JButton("Restart");
        restartButton.setBounds(frame.getWidth() - 100, 60, 80, 30);
        frame.add(restartButton);

        restartButton.addActionListener(e -> resetGame());

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(frame.getWidth() - 100, 100, 80, 30);
        frame.add(exitButton);

        exitButton.addActionListener(e -> System.exit(0));

        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int screenWidth = frame.getWidth();
                int screenHeight = frame.getHeight();
                int newSize = Math.min(screenWidth, screenHeight) - 100;
                chessBoardPanel.setBounds((screenWidth - newSize) / 2, (screenHeight - newSize) / 2, newSize, newSize);
                restartButton.setBounds(screenWidth - 100, screenHeight - 70, 80, 30);
                exitButton.setBounds(screenWidth - 100, screenHeight - 40, 80, 30);
            }
        });

        frame.dispatchEvent(new java.awt.event.ComponentEvent(frame, java.awt.event.ComponentEvent.COMPONENT_RESIZED));
        frame.setVisible(true);
    }

    private void renderPieces() {
        placePiece("sprites/Black/black-rook.png", 0, 0, new Rook(false, new Point(0, 0)));
        placePiece("sprites/Black/black-knight.png", 0, 1, new Knight(false, new Point(0, 1)));
        placePiece("sprites/Black/black-bishop.png", 0, 2, new Bishop(false, new Point(0, 2)));
        placePiece("sprites/Black/black-queen.png", 0, 3, new Queen(false, new Point(0, 3)));
        placePiece("sprites/Black/black-king.png", 0, 4, new King(false, new Point(0, 4)));
        placePiece("sprites/Black/black-bishop.png", 0, 5, new Bishop(false, new Point(0, 5)));
        placePiece("sprites/Black/black-knight.png", 0, 6, new Knight(false, new Point(0, 6)));
        placePiece("sprites/Black/black-rook.png", 0, 7, new Rook(false, new Point(0, 7)));

        for (int col = 0; col < 8; col++) {
            placePiece("sprites/Black/black-pawn.png", 1, col, new Pawn(false, new Point(1, col)));
        }

        placePiece("sprites/White/white-rook.png", 7, 0, new Rook(true, new Point(7, 0)));
        placePiece("sprites/White/white-knight.png", 7, 1, new Knight(true, new Point(7, 1)));
        placePiece("sprites/White/white-bishop.png", 7, 2, new Bishop(true, new Point(7, 2)));
        placePiece("sprites/White/white-queen.png", 7, 3, new Queen(true, new Point(7, 3)));
        placePiece("sprites/White/white-king.png", 7, 4, new King(true, new Point(7, 4)));
        placePiece("sprites/White/white-bishop.png", 7, 5, new Bishop(true, new Point(7, 5)));
        placePiece("sprites/White/white-knight.png", 7, 6, new Knight(true, new Point(7, 6)));
        placePiece("sprites/White/white-rook.png", 7, 7, new Rook(true, new Point(7, 7)));

        for (int col = 0; col < 8; col++) {
            placePiece("sprites/White/white-pawn.png", 6, col, new Pawn(true, new Point(6, col)));
        }

        chessBoardPanel.revalidate();
        chessBoardPanel.repaint();
    }

    private void placePiece(String imagePath, int row, int col, Piece piece) {
        ImageIcon pieceIcon = new ImageIcon(resizeImage(new ImageIcon(imagePath).getImage(), pieceSize, pieceSize));
        JLabel pieceLabel = new JLabel(pieceIcon);

        JPanel square = (JPanel) chessBoardPanel.getComponent(row * 8 + col);
        square.putClientProperty("piece", piece);
        board[row][col] = piece;
        square.add(pieceLabel, BorderLayout.CENTER);
    }

    private Image resizeImage(Image originalImage, int width, int height) {
        return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    private void resetGame() {
        for (Component square : chessBoardPanel.getComponents()) {
            ((JPanel) square).removeAll();

            board = new Piece[8][8];
            renderPieces();
        }
    }

    private class ChessMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            JPanel clickedSquare = (JPanel) e.getSource();

            //check if a piece is already selected
            System.out.println("//check if a piece is already selected");
            if (previousSquare != null && selectedPiece != null) {
                //if the same square is clicked twice deselct the piece
                System.out.println("//if the same square is clicked twice deselct the piece");
                if (previousSquare == clickedSquare) {
                    previousSquare = null;
                    selectedPiece = null;
                } else {
                    //if it is not the same square check if there is a piece on the square
                    System.out.println("//if it is not the same square check if there is a piece on the square");
                    if (clickedSquare.getClientProperty("piece") != null) {
                        //if there is a piece store it
                        System.out.println("//if there is a piece store it");
                        Piece p = (Piece) clickedSquare.getClientProperty("piece");

                        //check the colour
                        System.out.println("//check the colour");
                        if (p.isWhite != selectedPiece.isWhite) {
                            //if it is not the same colour check for valid capture
                            System.out.println("//if it is not the same colour check for valid capture");

                            if (selectedPiece.validCapture(new Point(p.location.x, p.location.y))) {

                                //if capture is valid then capture
                                System.out.println("//if capture is valid then capture");
                                //first remove captured piece
                                System.out.println("//first remove captured piece");
                                clickedSquare.removeAll();
                                clickedSquare.putClientProperty("piece", null);
                                board[p.location.x][p.location.y] = null;

                                //move to new square
                                System.out.println("//move to new square");
                                clickedSquare.add(previousSquare.getComponent(0));
                                clickedSquare.putClientProperty("piece", selectedPiece);
                                board[p.location.x][p.location.y] = selectedPiece;
                                selectedPiece.move(new Point(p.location.x, p.location.y));

                                //remove it from previous location
                                System.out.println(" //remove it from previous location");
                                previousSquare.removeAll();
                                previousSquare.putClientProperty("piece", null);
                                board[selectedPiece.location.x][selectedPiece.location.y] = null;

                                // Revalidate and repaint the chessboard
                                System.out.println("// Revalidate and repaint the chessboard");
                                previousSquare.revalidate();
                                clickedSquare.revalidate();
                                chessBoardPanel.revalidate();
                                previousSquare.repaint();
                                clickedSquare.repaint();
    
                                // Reset selection after the move
                                System.out.println("// Reset selection after the move");
                                selectedPiece = null;
                                previousSquare = null;
                                turnCounter++;
                            }
                        } else {
                            //if it is the same colour then deselect the pieces
                            System.out.println("//if it is the same colour then deselect the pieces");
                            previousSquare = null;
                            selectedPiece = null;
                        }
                    } else {
                        //if there is no piece on the square check for valid move
                        System.out.println("//if there is no piece on the square check for valid move");
                        //find the location of the target square on the grid
                        System.out.println("//find the location of the target square on the grid");
                        int targetIndex = chessBoardPanel.getComponentZOrder(clickedSquare);
                        int targetRow = targetIndex / 8;
                        int targetCol = targetIndex % 8; 

                        if (selectedPiece.validMove(new Point(targetRow, targetCol))) {
                            //if it is a valid move then move the piece
                            System.out.println("//if it is a valid move then move the piece");

                            //move to new square
                            System.out.println("//move to new square");
                            clickedSquare.add(previousSquare.getComponent(0));
                            clickedSquare.putClientProperty("piece", selectedPiece);
                            board[targetRow][targetCol] = selectedPiece;
                            selectedPiece.move(new Point(targetRow, targetCol));
                            
                            //remove it from previous location
                            System.out.println("//remove it from previous location");
                            previousSquare.removeAll();
                            previousSquare.putClientProperty("piece", null);
                            board[selectedPiece.location.x][selectedPiece.location.y] = null;

                            // Revalidate and repaint the chessboard
                            System.out.println("// Revalidate and repaint the chessboard");
                            previousSquare.revalidate();
                            clickedSquare.revalidate();
                            chessBoardPanel.revalidate();
                            previousSquare.repaint();
                            clickedSquare.repaint();

                            // Reset selection after the move
                            System.out.println("// Reset selection after the move");
                            selectedPiece = null;
                            previousSquare = null;
                            turnCounter++;
                        } else {
                            //if it is not valid then deselect it and the square
                            System.out.println("//if it is not valid then deselect it and the square");
                            previousSquare = null;
                            selectedPiece = null;
                        }
                    }


                }
                
            } else {
                //2if location has a piece select it and store its location
                System.out.println("//2if location has a piece select it and store its location");
                if ((Piece) clickedSquare.getClientProperty("piece") != null) {
                    selectedPiece = (Piece) clickedSquare.getClientProperty("piece");
                    previousSquare = clickedSquare;
                }
                
            }

            System.out.println();
            System.out.println("end");
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        new ChessGame();
    }
}
