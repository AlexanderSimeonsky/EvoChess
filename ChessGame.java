import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Class to handle the game flow.
 */
public class ChessGame {
    public static JFrame frame;
    public static JPanel chessBoardPanel; //visual board
    private Piece selectedPiece = null;
    private JPanel previousSquare = null;  
    private int pieceSize = 105;
    public static Piece[][] board; //logical board
    public static int turnCounter = 0;
    public static boolean isWhiteTurn = true; 
    private JLabel turnLabel;
    private JLabel scoreLabel;
    public static Point whiteKingLocation; //track position of kings
    public static Point blackKingLocation;
    public boolean gameIsOver;
    public static ArrayList<Piece> whitePieces;
    public static ArrayList<Piece> blackPieces;
    public int blackscore;
    public int whitescore;

    /**
     * Constructor that begins the game flow.
     */
    public ChessGame() {
        board = new Piece[8][8];
        turnCounter = 0;
        isWhiteTurn = true;
        previousSquare = null;
        selectedPiece = null;
        gameIsOver = false;
        whitePieces = new ArrayList<Piece>(16);
        blackPieces = new ArrayList<Piece>(16);
        renderChessboard();
        renderPieces();
        blackscore = 0;
        whitescore = 0;
    }

    /**
     * Method to activate the chess menu.
     * @param frame The frame to dispose of.
     */
    public static void activateChessMenu(JFrame frame) {
        SoundPlayer.playSound("sounds/close.wav");
        new ChessMenu();
        ChessMenu.createMenu();  
    
        // Set up a timer to dispose of the menu frame after 0.5 seconds
        Timer timer = new Timer(500, event -> frame.dispose());
        timer.setRepeats(false);
        timer.start();
    }

    private void renderChessboard() {
        Color black = new Color(50, 50, 50);
        Color white = new Color(200, 200, 200);
        
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        // Load the background image and make it final
        final Image backgroundImage;
        try {
            backgroundImage = ImageIO.read(new File("sprites/GameBackground.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            return; // Exit the method if the image can't be loaded
        }
    
        // Custom JPanel for background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(null);
        frame.setContentPane(backgroundPanel);
    
        // Set fullscreen mode
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);
    
        // Create and configure the chessboard panel
        chessBoardPanel = new JPanel();
        chessBoardPanel.setLayout(new GridLayout(8, 8));
        chessBoardPanel.setOpaque(false); // Make chessboard panel transparent
    
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
        backgroundPanel.add(chessBoardPanel);
    
        //space
    
        // Resize components based on window resizing
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int screenWidth = frame.getWidth();
                int screenHeight = frame.getHeight();
                int newSize = Math.min(screenWidth, screenHeight) - 100;
                chessBoardPanel.setBounds((screenWidth - newSize) - 1325 / 2, (screenHeight - newSize) / 2, newSize, newSize);              
            }
        });
    
        // Turn label
        turnLabel = new JLabel(isWhiteTurn ? "White's turn" : "Black's turn");
        turnLabel.setForeground(isWhiteTurn ? white : black);
        turnLabel.setFont(new Font("calibri", Font.BOLD, 20));
        turnLabel.setBounds(1220, 70, 250, 100);
        turnLabel.setBackground(black);
        turnLabel.setForeground(white);
        backgroundPanel.add(turnLabel);

        scoreLabel = new JLabel(whitescore + "-" + blackscore);
        scoreLabel.setFont(new Font("calibri", Font.BOLD, 120));
        scoreLabel.setBounds(1195, 115, 500, 200);
        scoreLabel.setBackground(white);
        backgroundPanel.add(scoreLabel);

        try {
            BufferedImage buttonImage = ImageIO.read(new File("sprites/MainMenubuttons.png"));
            JButton backToMenuButton = new JButton(new ImageIcon(buttonImage));
            backToMenuButton.setBounds(1100, 770, buttonImage.getWidth(), buttonImage.getHeight());
            backToMenuButton.setBorderPainted(false);
            backToMenuButton.setContentAreaFilled(false);
            backToMenuButton.setFocusPainted(false);
            backToMenuButton.addActionListener(e -> activateChessMenu(frame));
            backgroundPanel.add(backToMenuButton);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedImage buttonIcon = ImageIO.read(new File("sprites/MainMenubuttons.png"));
            JButton newGameButton = new JButton(new ImageIcon(buttonIcon));
            newGameButton.setBounds(1100, 10, buttonIcon.getWidth(), buttonIcon.getHeight());
            newGameButton.setBorderPainted(false);
            newGameButton.setContentAreaFilled(false);
            newGameButton.setFocusPainted(false);
            newGameButton.addActionListener(e -> resetGame());
            backgroundPanel.add(newGameButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Trigger initial layout
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

        //store location of kings
        whiteKingLocation = new Point(7, 4);
        blackKingLocation = new Point(0, 4);

        chessBoardPanel.revalidate();
        chessBoardPanel.repaint();
    }

    private void placePiece(String imagePath, int row, int col, Piece piece) {
        ImageIcon pieceIcon = new ImageIcon(resizeImage(new ImageIcon(imagePath).getImage(), pieceSize, pieceSize));
        JLabel pieceLabel = new JLabel(pieceIcon);

        JPanel square = (JPanel) chessBoardPanel.getComponent(row * 8 + col);
        square.putClientProperty("piece", piece);
        board[row][col] = piece;
        if (piece.isWhite) {
            whitePieces.add(piece);
        } else {
            blackPieces.add(piece);
        }
        square.add(pieceLabel, BorderLayout.CENTER);
    }

    public static Image resizeImage(Image originalImage, int width, int height) {
        return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    private void resetGame() {
        board = new Piece[8][8];
        turnCounter = 0;
        isWhiteTurn = true;
        previousSquare = null;
        selectedPiece = null;
        gameIsOver = false;
        whitePieces = new ArrayList<Piece>(16);
        blackPieces = new ArrayList<Piece>(16);
        renderChessboard();
        renderPieces();
        
        
    }

    /**
     * Class to handle mouse events on the chessboard.
     * Also includes logic of movement and game end conditions.
     */
    public class ChessMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (!gameIsOver) {
                JPanel clickedSquare = (JPanel) e.getSource();
    
                //check if a piece is already selected
                if (previousSquare != null && selectedPiece != null) {
                    //if the same square is clicked twice deselct the piece
                    if (previousSquare == clickedSquare) {
                        previousSquare = null;
                        selectedPiece = null;
                    } else {
                        //if it is not the same square check if there is a piece on the square
                        if (clickedSquare.getClientProperty("piece") != null) {
                            //if there is a piece store it
                            Piece p = (Piece) clickedSquare.getClientProperty("piece");
    
                            //check the colour
                            if (p.isWhite != selectedPiece.isWhite) {
                                //if it is not the same colour check for valid capture
    
                                Point point = new Point(p.location.x, p.location.y);
                                if (selectedPiece.validCapture(point) && !selectedPiece.illegalMove(point)) {
    
                                    //if capture is valid then capture

                                    //update the acquiredPoints field in the piece
                                    //and check for evolution
                                    selectedPiece.acquiredPoints += p.points;
                                    Piece temp;
                                    if (!(selectedPiece instanceof King)) {
                                        temp = selectedPiece.pieceEvolves();
                                        if (temp != null) {
                                            selectedPiece = temp;                                          
                                        }
                                    }

                                    //first remove captured piece
                                    clickedSquare.removeAll();
                                    clickedSquare.putClientProperty("piece", null);
                                    board[p.location.x][p.location.y] = null;
                                    
                                    if (p.isWhite) {
                                        whitePieces.remove(p);
                                    } else {
                                        blackPieces.remove(p);
                                    }
                                    SoundPlayer.playSound("sounds/capture.wav");
    
                                    //move to new square
                                    clickedSquare.add(previousSquare.getComponent(0));
                                    clickedSquare.putClientProperty("piece", selectedPiece);
                                    board[p.location.x][p.location.y] = selectedPiece;
                                    selectedPiece.move(new Point(p.location.x, p.location.y));
                                    p.move(new Point(-1, -1));

                                    //if king moved update variable for king location
                                    if (selectedPiece instanceof King) {
                                        if (selectedPiece.isWhite) {
                                            whiteKingLocation = new Point(p.location.x, p.location.y);
                                        } else {
                                            blackKingLocation = new Point(p.location.x, p.location.y);
                                        }
                                    }
    
                                    //remove it from previous location
                                    previousSquare.removeAll();
                                    previousSquare.putClientProperty("piece", null);
                                    board[selectedPiece.location.x][selectedPiece.location.y] = null;
    
                                    // Revalidate and repaint the chessboard
                                    previousSquare.revalidate();
                                    clickedSquare.revalidate();
                                    chessBoardPanel.revalidate();
                                    previousSquare.repaint();
                                    clickedSquare.repaint();

                                    //change piece turn
                                    isWhiteTurn = !isWhiteTurn;
                                    turnLabel.setText(isWhiteTurn ? "White's turn" : "Black's turn");

                                    //check for game end
                                    if (checkMate()) {
                                        gameEnd("Checkmate");
                                        gameIsOver = true;
                                    }

                                    if (draw()) {
                                        gameEnd("Draw");
                                        gameIsOver = true;
                                    }
        
                                    // Reset selection after the move
                                    selectedPiece = null;
                                    previousSquare = null;
                                    turnCounter++;
    
                                }
                            } else {
                                //if it is the same colour then deselect the pieces
                                
                                previousSquare = null;
                                selectedPiece = null;
                            }
                        } else {
                            //if there is no piece on the square check for valid move
                            //find the location of the target square on the grid
                            int targetIndex = chessBoardPanel.getComponentZOrder(clickedSquare);
                            int targetRow = targetIndex / 8;
                            int targetCol = targetIndex % 8; 
    
                            Point point = new Point(targetRow, targetCol);
                            if (selectedPiece.validMove(point) && !selectedPiece.illegalMove(point)) {
                                //if it is a valid move then move the piece    
                                //move to new square
                                clickedSquare.add(previousSquare.getComponent(0));
                                clickedSquare.putClientProperty("piece", selectedPiece);
                                board[targetRow][targetCol] = selectedPiece;
                                selectedPiece.move(new Point(targetRow, targetCol));
                                if (isWhiteTurn) {
                                    SoundPlayer.playSound("sounds/whiteMove.wav");
                                } else {
                                    SoundPlayer.playSound("sounds/blackMove.wav");
                                }

                                //if king moved update variable for king location
                                if (selectedPiece instanceof King) {
                                    if (selectedPiece.isWhite) {
                                        whiteKingLocation = new Point(targetRow, targetCol);
                                    } else {
                                        blackKingLocation = new Point(targetRow, targetCol);
                                    }
                                }
                                
                                //remove it from previous location
                                previousSquare.removeAll();
                                previousSquare.putClientProperty("piece", null);
                                board[selectedPiece.location.x][selectedPiece.location.y] = null;
    
                                // Revalidate and repaint the chessboard
                                previousSquare.revalidate();
                                clickedSquare.revalidate();
                                chessBoardPanel.revalidate();
                                previousSquare.repaint();
                                clickedSquare.repaint();

                                //change piece turn
                                isWhiteTurn = !isWhiteTurn;
                                turnLabel.setText(isWhiteTurn ? "White's turn" : "Black's turn");

                                //check for game end
                                if (checkMate()) {
                                    gameEnd("Checkmate");
                                    gameIsOver = true;
                                } 

                                if (draw()) {
                                    gameEnd("Draw");
                                    gameIsOver = true;
                                }
    
                                // Reset selection after the move
                                selectedPiece = null;
                                previousSquare = null;
                                turnCounter++;
    
                            } else {
                                //if it is not valid then deselect it and the square
                                SoundPlayer.playSound("sounds/illegal.wav");
                                previousSquare = null;
                                selectedPiece = null;
                            }
                        }
                    }
                    
                } else {
                    //if location has a piece select it and store its location
                    if ((Piece) clickedSquare.getClientProperty("piece") != null) {
                        selectedPiece = (Piece) clickedSquare.getClientProperty("piece");
                        previousSquare = clickedSquare;
    
                        //check whos turn it is
                        if (isWhiteTurn && !selectedPiece.isWhite) {
                            selectedPiece = null;
                            previousSquare = null;
                        } else if (!isWhiteTurn && selectedPiece.isWhite) {
                            selectedPiece = null;
                            previousSquare = null;
                        }
                    }
                    
                }
            }
        }

        /**
         * Method to check for checkmate.
         * @return True if checkmate, false otherwise.
         */
        public boolean checkMate() {
            //get the location of the king
            King king = getKing();

            //check if there is an active check
            if (king.isInCheck()) {
                //if yes check for possible moves
                //option 1 king can move

                if (kingCanEscape(king)) {
                    return false;
                }

                //option 2 checking piece can be captured
                if (checkingPieceCanBeCaptured(king)) {
                    return false;
                }

                //option 3 check can be blocked
                if (checkCanBeBlocked(king)) {
                    return false;
                }
                
            } else {
                //king is not in check and so checkmate is impossible
                return false;
            }
            
            SoundPlayer.playSound("sounds/close.wav");
            return true;
        }

        /**
         * Method to check for draw.
         * @return True if draw, false otherwise.
         */
        private boolean draw() {
            ArrayList<Piece> pieces = new ArrayList<Piece>();
    
            for (Piece piece : whitePieces) {
                pieces.add(piece);
            }
            for (Piece piece : blackPieces) {
                pieces.add(piece);
            }

            //get number of pieces remaining
            int count = pieces.size();
    
            //no need to check for dead position if there are 5 or more pieces
            if (count >= 5) {
                return false;
            }
    
            King king = getKing();
    
            //check for stalemate (no leagal moves remaining)
            if (stalemate(king)) {
                return true;
            }
    
            //check for dead position (no possible checkmate)
            if (deadPosition(pieces, count)) {
                return true;
            }
            
            //no draw condition is met
            return false;
        }
    
        private boolean stalemate(King king) {
            //check if king can move
            if (kingCanEscape(king)) {
                //king can move
                return false;
            }
    
            //check if any other allied piece can move
            ArrayList<Piece> alliedPieces = getAlliedPieces(isWhiteTurn);
    
            //check count
            if (alliedPieces.size() == 1) {
                //only king 
                return true;
            } else {
                //check if any piece can move
                for (Piece p : alliedPieces) {
                    if (p.hasLegalMoves()) {
                        //piece can move
                        return false;
                    }
                }
            }
    
            //no piece can move so stalemate
            SoundPlayer.playSound("sounds/close.wav");
            return true;
        }
    
        private boolean deadPosition(ArrayList<Piece> pieces, int count) {
    
            //check the type of pieces remaining
            for (Piece p : pieces) {
                return !(p instanceof Rook || p instanceof Queen || p instanceof Pawn);
            }
    
            //return false as a default
            return false;
        }

        /**
         * Method to check if the king can escape a check.
         * @param king The king to check.
         * @return True if the king can escape, false otherwise.
         */
        public boolean kingCanEscape(King king) {        
            // Store king's current position
            Point pos = king.location;
        
            // List all possible moves for the king
            Point[] possibleMoves = {
                new Point(pos.x + 1, pos.y - 1), // up left
                new Point(pos.x + 1, pos.y),     // up center
                new Point(pos.x + 1, pos.y + 1), // up right
                new Point(pos.x, pos.y + 1),     // right
                new Point(pos.x - 1, pos.y + 1), // down right
                new Point(pos.x - 1, pos.y),     // down center
                new Point(pos.x - 1, pos.y - 1), // down left
                new Point(pos.x, pos.y - 1)      // left
            };
        
            // Check if any move is valid for the king to escape
            for (Point point : possibleMoves) {
                // Check if the square is within the board boundaries
                if (pointInBoard(point)) {
                    // Get the piece on the target square
                    Piece targetPiece = null;
                    for (Piece piece : king.isWhite ? whitePieces : blackPieces) {
                        if (point.equals(piece.location)) {
                            targetPiece = piece;
                        }
                    }
        
                    // If the square is empty, check for a valid move
                    if (targetPiece == null) {
                        if (king.validMove(point) && !king.illegalMove(point)) {
                            return true; // King can escape by moving
                        }
                    } else {
                        // If the square is occupied by an enemy piece, check for a valid capture
                        if (king.isWhite != targetPiece.isWhite) {
                            if (king.validCapture(point) && !king.illegalMove(point)) {
                                return true; // King can escape by capturing
                            }
                        }
                    }
                }
            }
        
            return false; // King can't escape
        }
           

        /**
         * Method to check if the checking piece can be captured.
         * @param king The king to check.
         * @return True if the checking piece can be captured, false otherwise.
         */
        public boolean checkingPieceCanBeCaptured(King king) {
            //fetch checking piece
            Piece checkingPiece = king.getCheckingPiece();

            //fetch pieces of the king's colour
            ArrayList<Piece> alliedPieces = getAlliedPieces(king.isWhite);

            //check if checking piece can be captured
            for (Piece piece : alliedPieces) {
                if (piece.validCapture(checkingPiece.location) && !piece.illegalMove(checkingPiece.location)) {
                    //piece can be captured
                    return true;
                }
            }

            //piece can't be captured
            return false;
        }

        /**
         * Method to check if the check can be blocked.
         * @param king The king to check.
         * @return True if the check can be blocked, false otherwise.
         */
        public boolean checkCanBeBlocked(King king) {
            //get checking piece
            Piece checkingPiece = king.getCheckingPiece();

            //get allied pieces
            ArrayList<Piece> alliedPieces = getAlliedPieces(king.isWhite);

            //get the difference in position between the piece and king
            int deltaX = king.location.x - checkingPiece.location.x;
            int deltaY = king.location.y - checkingPiece.location.y;

            //depending on what piece is doing the check the squares that need
            //to be checked will be different
            if (checkingPiece instanceof Rook) {
                //no need to check for evolution since the new moves of the rook can't be blocked
                //straight line
                return canBlockStraightLine(alliedPieces, checkingPiece, king, deltaX, deltaY);
                
            } else if (checkingPiece instanceof Bishop) {
                //no need to check for evolution since the new moves of the bishop can't be blocked
                //diagonal line
                return canBlockDiagonalLine(alliedPieces, checkingPiece, king, deltaX, deltaY);
    

            } else if (checkingPiece instanceof Queen) {
                //no need to check for evolution since the new moves of the queen can't be blocked

                //can be either straight or diagonal
                if (deltaX == 0 || deltaY == 0) {
                    //straight line
                    return canBlockStraightLine(alliedPieces, checkingPiece, king, deltaX, deltaY);
                } else {
                    //diagonal line
                    return canBlockDiagonalLine(alliedPieces, checkingPiece, king, deltaX, deltaY);
                }

            } else if (checkingPiece instanceof Knight) {
                //can't block knight
                return false;
                
            } else if (checkingPiece instanceof Pawn) {
                //can't block pawn
                return false;
            }

            //check can't be blocked
            return false;
        }

        public static boolean pointInBoard(Point point) {
            return point.x >= 0 && point.x <= 7 && point.y >= 0 && point.y <= 7; 
        }

        /**
         * Method to get all allied pieces.
         * @param isWhite The colour of the pieces.
         * @return An ArrayList of allied pieces.
         */
        public ArrayList<Piece> getAlliedPieces(boolean isWhite) {
            ArrayList<Piece> alliedPieces = new ArrayList<Piece>();

            for (Piece[] pieces : board) {
                for (Piece piece : pieces) {
                    if (piece != null) {
                        if (piece.isWhite == isWhite) {
                            alliedPieces.add(piece);
                        }
                    }
                }
            }

            return alliedPieces;
        }

        /**
         * Method to get the king.
         * @return The king.
         */
        public King getKing() {
            for (Piece piece : isWhiteTurn ? whitePieces : blackPieces) {
                if (piece instanceof King) {
                    return (King) piece;
                }
            }

            return null;
        }

        /**
         * Method to check if a check that is on a straight line can be blocked.
         * @param alliedPieces The allied pieces.
         * @param checkingPiece The checking piece.
         * @param king The king.
         * @param deltaX The change in x.
         * @param deltaY The change in y.
         * @return True if the check can be blocked, false otherwise.
         */
        public boolean canBlockStraightLine(ArrayList<Piece> alliedPieces, Piece checkingPiece, King king, int deltaX, int deltaY) {
            if (deltaX == 0) {
                //horizontal
                if (deltaY > 0) {
                    //piece is left of king
                    for (int i = king.location.y - 1; i < checkingPiece.location.y; i--) {
                        for (Piece piece : alliedPieces) {
                            Point p = new Point(king.location.x, i);
                            if (piece.validMove(p) && !piece.illegalMove(p) && !(piece instanceof King)) {
                                //can be blocked
                                return true;
                            }
                        }
                    }                        
                } else if (deltaY < 0) {
                    //piece is right of king
                    for (int i = king.location.y + 1; i > checkingPiece.location.y; i++) {
                        for (Piece piece : alliedPieces) {
                            Point p = new Point(king.location.x, i);
                            if (piece.validMove(p) && !piece.illegalMove(p) && !(piece instanceof King)) {
                                //can be blocked
                                return true;
                            }
                        }
                    }
                }
            } else if (deltaY == 0) {
                //vertical
                if (deltaX > 0) {
                    //piece is above king
                    for (int i = king.location.x - 1; i < checkingPiece.location.x; i--) {
                        for (Piece piece : alliedPieces) {
                            Point p = new Point(i, king.location.y);
                            if (piece.validMove(p) && !piece.illegalMove(p) && !(piece instanceof King)) {
                                //can be blocked
                                return true;
                            }
                        }
                    }
                } else if (deltaX < 0) {
                    //piece is below king
                    for (int i = king.location.x + 1; i > checkingPiece.location.x; i++) {
                        for (Piece piece : alliedPieces) {
                            Point p = new Point(i, king.location.y);
                            if (piece.validMove(p) && !piece.illegalMove(p) && !(piece instanceof King)) {
                                //can be blocked
                                return true;
                            }
                        }
                    }

                }    
            }

            //can't be blocked
            return false;
        }

        /**
         * Method to check if a check that is on a diagonal line can be blocked.
         * @param alliedPieces The allied pieces.
         * @param checkingPiece The checking piece.
         * @param king The king.
         * @param deltaX The change in x.
         * @param deltaY The change in y.
         * @return True if the check can be blocked, false otherwise.
         */
        private boolean canBlockDiagonalLine(ArrayList<Piece> alliedPieces, Piece checkingPiece, King king, int deltaX, int deltaY) {
            if (checkingPiece.location.x < king.location.x) {
                //piece is above king
                if (checkingPiece.location.y < king.location.y) {
                    //piece is left of king
                    for (int i = king.location.y - 1; i < checkingPiece.location.y; i--) {
                        int diff = Math.abs(i - king.location.y);
                        for (Piece piece : alliedPieces) {
                            Point p = new Point(king.location.x - diff, i);
                            if (piece.validMove(p) && !piece.illegalMove(p) && !(piece instanceof King)) {
                                //can be blocked
                                return true;
                            }
                        }
                    }
                    
                } else if (checkingPiece.location.y > king.location.y) {
                    //piece is right of king
                    for (int i = king.location.y + 1; i > checkingPiece.location.y; i++) {
                        int diff = Math.abs(i - king.location.y);
                        for (Piece piece : alliedPieces) {
                            Point p = new Point(king.location.x - diff, i);
                            if (piece.validMove(p) && !piece.illegalMove(p) && !(piece instanceof King)) {
                                //can be blocked
                                return true;
                            }
                        }
                    }

                }

            } else if (checkingPiece.location.x > king.location.x) {
                //piece is below king
                if (checkingPiece.location.y < king.location.y) {
                    //piece is left of king
                    for (int i = king.location.y - 1; i > checkingPiece.location.y; i--) {
                        int diff = Math.abs(i - king.location.y);
                        for (Piece piece : alliedPieces) {
                            Point p = new Point(king.location.x + diff, i);
                            if (piece.validMove(p) && !piece.illegalMove(p) && !(piece instanceof King)) {
                                //can be blocked
                                return true;
                            }
                        }
                    }
                    
                } else if (checkingPiece.location.y > king.location.y) {
                    //piece is right of king
                    for (int i = king.location.y + 1; i < checkingPiece.location.y; i++) {
                        int diff = Math.abs(i - king.location.y);
                        for (Piece piece : alliedPieces) {
                            Point p = new Point(king.location.x + diff, i);
                            if (piece.validMove(p) && !piece.illegalMove(p) && !(piece instanceof King)) {
                                //can be blocked
                                return true;
                            }
                        }
                    }

                }
            }

            //can't be blocked
            return false;
        }
    }

    private void gameEnd(String message) {
        JLabel endLabel;
        
        if (message == "Checkmate") {
            endLabel = new JLabel(isWhiteTurn ? "Black WINS! Checkmate!" : "White WINS! Checkmate!");
        } else {
            endLabel = new JLabel("DRAW!");
        }
        if (isWhiteTurn) {
            blackscore++;
            scoreLabel.setText(whitescore + "-" + blackscore);
        } else {
            whitescore++;
            scoreLabel.setText(whitescore + "-" + blackscore);
        }

        endLabel.setForeground(isWhiteTurn ? Color.white : Color.white);
        endLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        endLabel.setBounds(1170, 220, 250, 100);
        endLabel.setBackground(Color.WHITE);
        frame.add(endLabel);

        frame.revalidate();
        frame.repaint();
    }
       
}