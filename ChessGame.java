import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//TODO game crashes when empty square is clicked
//TODO make it so a piece can't jump over other pieces
//TODO make it so allied pieces can't be captured

public class ChessGame extends JPanel {

    public static JPanel chessBoardPanel; // Panel to hold the chessboard
    private JLabel selectedPiece = null; // Track the currently selected piece
    private JPanel previousSquare = null; // Track the square from which the piece was picked
    private final int pieceSize = 105; // Size of the chess pieces
    public static Piece[][] board; // 2D array to represent the board

    // Constructor
    public ChessGame() {
        board = new Piece[8][8]; // Initialize the chess board
        renderChessboard();
        renderPieces();
    }

    // Method to render the chessboard
    public void renderChessboard() {
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

        // Create a chessboard panel with a GridLayout for an 8x8 grid
        chessBoardPanel = new JPanel();
        chessBoardPanel.setLayout(new GridLayout(8, 8));

        boolean isWhite = true;

        // Create 8x8 chessboard tiles
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout()); // Allows piece placement
                panel.setBackground(isWhite ? white : black);
                panel.addMouseListener(new ChessMouseListener()); // Add mouse listener to each square
                
                chessBoardPanel.add(panel);
                isWhite = !isWhite;
            }
            isWhite = !isWhite; // Alternate starting color for the next row
        }

        // Add the chessboard panel to the frame
        chessBoardPanel.setBounds(50, 50, 600, 600); // Initial size
        frame.add(chessBoardPanel);

        // Create the "Restart" button
        JButton restartButton = new JButton("Restart");
        restartButton.setBounds(frame.getWidth() - 100, 60, 80, 30);
        frame.add(restartButton);

        // Restart action listener
        restartButton.addActionListener(e -> resetGame());

        // Create the "Exit" button
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(frame.getWidth() - 100, 100, 80, 30);
        frame.add(exitButton);

        // Exit action listener
        exitButton.addActionListener(e -> System.exit(0)); // Exit action

        // Adjust positions of components when the window is resized
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

    // Method to render the chess pieces on the board
    public void renderPieces() {
        String basePath = "sprites/"; // Path to the folder with images

        // Example of placing pieces: first row (black side)
        placePiece(basePath + "Black/black-rook.png", 0, 0, new Rook(false, new Point(0, 0)));
        placePiece(basePath + "Black/black-knight.png", 0, 1, new Knight(false, new Point(0, 1)));
        placePiece(basePath + "Black/black-bishop.png", 0, 2, new Bishop(false, new Point(0, 2)));
        placePiece(basePath + "Black/black-queen.png", 0, 3, new Queen(false, new Point(0, 3)));
        placePiece(basePath + "Black/black-king.png", 0, 4, new King(false, new Point(0, 4)));
        placePiece(basePath + "Black/black-bishop.png", 0, 5, new Bishop(false, new Point(0, 5)));
        placePiece(basePath + "Black/black-knight.png", 0, 6, new Knight(false, new Point(0, 6)));
        placePiece(basePath + "Black/black-rook.png", 0, 7, new Rook(false, new Point(0, 7)));

        // Second row (black pawns)
        for (int col = 0; col < 8; col++) {
            placePiece(basePath + "Black/black-pawn.png", 1, col, new Pawn(false, new Point(1, col)));
        }

        // Example of placing pieces: last row (white side)
        placePiece(basePath + "White/white-rook.png", 7, 0, new Rook(true, new Point(7, 0)));
        placePiece(basePath + "White/white-knight.png", 7, 1, new Knight(true, new Point(7, 1)));
        placePiece(basePath + "White/white-bishop.png", 7, 2, new Bishop(true, new Point(7, 2)));
        placePiece(basePath + "White/white-queen.png", 7, 3, new Queen(true, new Point(7, 3)));
        placePiece(basePath + "White/white-king.png", 7, 4, new King(true, new Point(7, 4)));
        placePiece(basePath + "White/white-bishop.png", 7, 5, new Bishop(true, new Point(7, 5)));
        placePiece(basePath + "White/white-knight.png", 7, 6, new Knight(true, new Point(7, 6)));
        placePiece(basePath + "White/white-rook.png", 7, 7, new Rook(true, new Point(7, 7)));

        // Seventh row (white pawns)
        for (int col = 0; col < 8; col++) {
            placePiece(basePath + "White/white-pawn.png", 6, col, new Pawn(true, new Point(6, col)));
        }

        // Revalidate and repaint to ensure pieces are rendered
        chessBoardPanel.revalidate();
        chessBoardPanel.repaint();
    }

    // Helper method to place a piece at a specific location on the board
    private void placePiece(String imagePath, int row, int col, Piece piece) {
        ImageIcon pieceIcon = new ImageIcon(resizeImage(new ImageIcon(imagePath).getImage(), pieceSize, pieceSize));
        JLabel pieceLabel = new JLabel(pieceIcon);

        // Each component in the GridLayout can be accessed by its index (row * 8 + col)
        JPanel square = (JPanel) chessBoardPanel.getComponent(row * 8 + col);
        square.putClientProperty("piece", piece);
        board[row][col] = piece; // Update the board representation
        square.add(pieceLabel, BorderLayout.CENTER); // Add piece to the square
    }

    // Method to resize an image
    private Image resizeImage(Image originalImage, int width, int height) {
        return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    // Method to reset the game by clearing the board and placing pieces back in the original position
    private void resetGame() {
        // Remove all pieces from the board
        for (Component square : chessBoardPanel.getComponents()) {
            ((JPanel) square).removeAll();
        }

        // Re-render the pieces
        board = new Piece[8][8]; // Reset the board
        renderPieces();
    }

    // Inner class to handle mouse events for each square on the chessboard
    private class ChessMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            JPanel clickedSquare = (JPanel) e.getSource(); // Get the square that was clicked
    
            // If a piece is already selected, we want to move it to the clicked square
            if (selectedPiece != null) {
                Piece selectedPieceObj = (Piece) previousSquare.getClientProperty("piece"); // Get the selected piece
    
                // Ensure there is a piece to move
                if (selectedPieceObj != null) {
                    // Determine the target square coordinates
                    int targetIndex = chessBoardPanel.getComponentZOrder(clickedSquare); // Get the index of the clicked square
                    int targetRow = targetIndex / 8; // Calculate the row
                    int targetCol = targetIndex % 8; // Calculate the column
    
                    // Create a target point for movement
                    Point targetPoint = new Point(targetRow, targetCol);
    
                    // Check if the move is valid
                    if (selectedPieceObj.validMove(targetPoint)) {
                        // Check if the target square has a piece to capture
                        Piece targetPiece = (Piece) clickedSquare.getClientProperty("piece");
    
                        // Move the piece to the new square
                        clickedSquare.add(selectedPiece); // Add the piece to the new square
                        previousSquare.removeAll(); // Clear the old square
    
                        // Update the board representation
                        ChessGame.board[selectedPieceObj.location.x][selectedPieceObj.location.y] = null; // Clear the old position
                        ChessGame.board[targetRow][targetCol] = selectedPieceObj; // Set the new position
    
                        // Update the piece's location
                        selectedPieceObj.move(targetPoint);
    
                        // Set the new piece to the square's property
                        clickedSquare.putClientProperty("piece", selectedPieceObj); // Set the new piece in the target square
                        previousSquare.putClientProperty("piece", null); // Clear the old square's piece in property
    
                        // Revalidate and repaint the chessboard
                        previousSquare.revalidate();
                        clickedSquare.revalidate();
                        chessBoardPanel.revalidate();
                        previousSquare.repaint();
                        clickedSquare.repaint();
    
                        // Reset selection after the move
                        selectedPiece = null;
                        previousSquare = null;
                    } else {
                        System.out.println("Invalid Move Attempted: " + selectedPieceObj + " to " + targetPoint);
                    }
                } else {
                    System.out.println("No piece found on the previous square!"); // Debugging output
                }
            } else {
                // If no piece is selected, select the piece from the clicked square
                Component component = clickedSquare.getComponent(0); // Get the first component of the clicked square
    
                if (component instanceof JLabel) { // Ensure it's a JLabel
                    selectedPiece = (JLabel) component; // Store the selected piece
                    previousSquare = clickedSquare; // Track the original square
    
                    // Get the piece from the previous square and log its information
                    Piece piece = (Piece) previousSquare.getClientProperty("piece");
                    System.out.println("Selected Piece: " + piece + " from " + previousSquare);
                } else {
                    System.out.println("No piece clicked!"); // Debugging output
                }
            }
        }
    }
    
    public static void main(String[] args) {
        new ChessGame();
    }
}
