import java.awt.*;
import javax.swing.*;

/**. */
public class ChessGame extends JPanel {

    private JPanel chessBoardPanel; // Panel to hold the chessboard

    /**Constructor. */
    public ChessGame() {
        renderChessboard();
        renderPieces();
    }

    /**Method to render the chessboard. */
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
                chessBoardPanel.add(panel);
                isWhite = !isWhite;
            }
            isWhite = !isWhite; // Alternate starting color for the next row
        }

        // Add the chessboard panel to the frame
        chessBoardPanel.setBounds(50, 50, 600, 600); // Initial size
        frame.add(chessBoardPanel);

        // Create the "Exit" button
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(frame.getWidth() - 100, 20, 80, 30);
        frame.add(exitButton);

        exitButton.addActionListener(e -> System.exit(0)); // Exit action

        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int screenWidth = frame.getWidth();
                int screenHeight = frame.getHeight();
                int newSize = Math.min(screenWidth, screenHeight) - 100;
                chessBoardPanel.setBounds((screenWidth - newSize) / 2, (screenHeight - newSize) / 2, newSize, newSize);
                exitButton.setBounds(screenWidth - 100, screenHeight - 40, 80, 30);
            }
        });

        frame.dispatchEvent(new java.awt.event.ComponentEvent(frame, java.awt.event.ComponentEvent.COMPONENT_RESIZED));
        frame.setVisible(true);
    }

    /**Method to render the chess pieces on the board. */
    public void renderPieces() {
        String basePath = "sprites/"; // Path to the folder with images

        // Example of placing pieces: first row (black side)
        placePiece(basePath + "Black/black-rook.png", 0, 0);
        placePiece(basePath + "Black/black-knight.png", 0, 1);
        placePiece(basePath + "Black/black-bishop.png", 0, 2);
        placePiece(basePath + "Black/black-queen.png", 0, 3);
        placePiece(basePath + "Black/black-king.png", 0, 4);
        placePiece(basePath + "Black/black-bishop.png", 0, 5);
        placePiece(basePath + "Black/black-knight.png", 0, 6);
        placePiece(basePath + "Black/black-rook.png", 0, 7);

        // Second row (black pawns)
        for (int col = 0; col < 8; col++) {
            placePiece(basePath + "Black/black-pawn.png", 1, col);
        }

        // Example of placing pieces: last row (white side)
        placePiece(basePath + "White/white-rook.png", 7, 0);
        placePiece(basePath + "White/white-knight.png", 7, 1);
        placePiece(basePath + "White/white-bishop.png", 7, 2);
        placePiece(basePath + "White/white-queen.png", 7, 3);
        placePiece(basePath + "White/white-king.png", 7, 4);
        placePiece(basePath + "White/white-bishop.png", 7, 5);
        placePiece(basePath + "White/white-knight.png", 7, 6);
        placePiece(basePath + "White/white-rook.png", 7, 7);

        // Seventh row (white pawns)
        for (int col = 0; col < 8; col++) {
            placePiece(basePath + "White/white-pawn.png", 6, col);
        }

        // Revalidate and repaint to ensure pieces are rendered
        chessBoardPanel.revalidate();
        chessBoardPanel.repaint();
    }

    // Helper method to place a piece at a specific location on the board
    private void placePiece(String imagePath, int row, int col) {
        ImageIcon pieceIcon = new ImageIcon(imagePath);
        JLabel pieceLabel = new JLabel(pieceIcon);

        // Each component in the GridLayout can be accessed by its index (row * 8 + col)
        JPanel square = (JPanel) chessBoardPanel.getComponent(row * 8 + col);
        square.add(pieceLabel, BorderLayout.CENTER); // Add piece to the square
    }

    // Main method to start the game
    public static void main(String[] args) {
        new ChessGame(); // Start the game
    }
}
