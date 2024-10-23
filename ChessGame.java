import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Class to handle the game flow.
 */
public class ChessGame {
    public static JFrame frame;
    public static JPanel chessBoardPanel; //visual board
    private Piece selectedPiece;
    private JPanel previousSquare;  
    private int pieceSize = 105;
    public static Piece[][] board; //logical board
    public static int turnCounter;
    public static boolean isWhiteTurn; 
    private JLabel turnLabel;
    public static Point whiteKingLocation; //track position of kings
    public static Point blackKingLocation;
    public boolean gameIsOver;
    public static ArrayList<Piece> whitePieces;
    public static ArrayList<Piece> blackPieces;

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
    }

    private void renderChessboard() {
        Color brown = new Color(149, 69, 53);
        Color black = new Color(50, 50, 50);
        Color white = new Color(200, 200, 200);

        frame = new JFrame();
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

        turnLabel = new JLabel(isWhiteTurn ? "White's turn" : "Black's turn");
        turnLabel.setForeground(isWhiteTurn ? white : black);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 20));
        turnLabel.setBounds(50, 50, 250, 100);
        turnLabel.setBackground(white);
        frame.add(turnLabel);


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
        for (Component square : chessBoardPanel.getComponents()) {
            ((JPanel) square).removeAll();

            board = new Piece[8][8];
            renderPieces();
        }
    }

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
                            int pieceIndex = chessBoardPanel.getComponentZOrder(clickedSquare);
                            int pieceRow = pieceIndex / 8;
                            int pieceCol = pieceIndex % 8;
                            //Piece p = board[pieceRow][pieceCol];
                            Piece p = (Piece) clickedSquare.getClientProperty("piece");
    
                            //check the colour
                            if (p.isWhite != selectedPiece.isWhite) {
                                //if it is not the same colour check for valid capture
    
                                Point point = new Point(p.location.x, p.location.y);
                                if (selectedPiece.validCapture(point) && !selectedPiece.illegalMove(point)) {
    
                                    //if capture is valid then capture
                                    //first remove captured piece
                                    clickedSquare.removeAll();
                                    clickedSquare.putClientProperty("piece", null);
                                    board[p.location.x][p.location.y] = null;
                                    if (p.isWhite) {
                                        whitePieces.remove(p);
                                    } else {
                                        blackPieces.remove(p);
                                    }
    
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
                                    System.out.println("valid move");

                                    //check for game end
                                    if (checkMate()) {
                                        System.out.println("GAMEOVER");
                                        gameEnd("Checkmate");
                                        gameIsOver = true;
                                    } else if (draw()) {
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
                                System.out.println("invalid move");
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

                                System.out.println("valid move");

                                //check for game end
                                if (checkMate()) {
                                    System.out.println("GAMEOVER");
                                    gameEnd("Checkmate");
                                    gameIsOver = true;
                                } else if (draw()) {
                                    gameEnd("Draw");
                                    gameIsOver = true;
                                }
    
                                // Reset selection after the move
                                selectedPiece = null;
                                previousSquare = null;
                                turnCounter++;
    
                            } else {
                                //if it is not valid then deselect it and the square
                                System.out.println("move invalid");
                                previousSquare = null;
                                selectedPiece = null;
                            }
                        }
    
    
                    }
                    
                } else {
                    //2if location has a piece select it and store its location
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

                        System.out.println("piece selected");
                    }
                    
                }
    
                System.out.println();
                System.out.println("end");
                System.out.println();
            }
        }

        public boolean checkMate() {
            //get the location of the king
            System.out.println("CHECKMATE CHECK");
            King king = getKing();

            //check if there is an active check
            if (king.isInCheck()) {
                //if yes check for possible moves
                //option 1 king can move
                System.out.println();
                System.out.println("king can escape check");
                System.out.println();
                if (kingCanEscape(king)) {
                    System.out.println("King can escape");
                    return false;
                }

                System.out.println();
                System.out.println("piece can be captured check");
                System.out.println();
                //option 2 checking piece can be captured
                if (checkingPieceCanBeCaptured(king)) {
                    System.out.println("piece can be captured");
                    return false;
                }

                System.out.println();
                System.out.println("piece can be blocked check");
                System.out.println();
                //option 3 check can be blocked
                if (checkCanBeBlocked(king)) {
                    System.out.println("piece can be blocked");
                    return false;
                }
                
            } else {
                //king is not in check and so checkmate is impossible
                System.out.println("king not in check");
                return false;
            }

            return true;
        }

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
    
            if (stalemate(king)) {
                return true;
            }
    
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
                System.out.println("King can escape");
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
                    if (p.hasLegalMoves() && !(p instanceof King)) {
                        //piece can move
                        return false;
                    }
                }
            }
    
            //no piece can move so stalemate
            System.out.println("No piece can move");
            return true;
        }
    
        private boolean deadPosition(ArrayList<Piece> pieces, int count) {
    
            //check the type of pieces remaining
            for (Piece p : pieces) {
                if (p instanceof Rook || p instanceof Queen || p instanceof Pawn) {
                    //can always win if you have a rook or queen or pawn
                    return false;
                } else if (p instanceof King) {
                    if (count == 2) {
                        //only kings left
                        return true;
                    }
                } else {
                    //knight or bishop
                    //check if the remaining 2 non king pieces are of the same colour
                    for (Piece p2 : pieces) {
                        if (!(p2 instanceof King) && p2 != p && p2.isWhite == p.isWhite) {
                            //pieces are same colour can win
                            return false;
                        } else {
                            //pieces are different colour so draw
                            return true;
                        }
                    }
                }
            }
    
            //return false as a default
            return false;
        }

        public boolean kingCanEscape(King king) {
            System.out.println("POSSIBLE KING MOVE CHECK");
        
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
                            System.out.println("Valid escape move: " + point);
                            return true; // King can escape by moving
                        }
                    } else {
                        // If the square is occupied by an enemy piece, check for a valid capture
                        if (king.isWhite != targetPiece.isWhite) {
                            if (king.validCapture(point) && !king.illegalMove(point)) {
                                System.out.println("Valid escape capture: " + point);
                                return true; // King can escape by capturing
                            }
                        }
                    }
                }
            }
        
            System.out.println("King can't escape");
            return false; // King can't escape
        }
           

        public boolean checkingPieceCanBeCaptured(King king) {
            //fetch checking piece
            Piece checkingPiece = king.getCheckingPiece();

            //fetch pieces of the king's colour
            ArrayList<Piece> alliedPieces = getAlliedPieces(king.isWhite);

            //check if checking piece can be captured
            for (Piece piece : alliedPieces) {
                if (piece.validCapture(checkingPiece.location) && !piece.illegalMove(checkingPiece.location)) {
                    System.out.println("CAN BE CAPTURES");
                    //piece can be captured
                    return true;
                }
            }

            //piece can't be captured
            return false;
        }

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
                //straight line
                return canBlockStraightLine(alliedPieces, checkingPiece, king, deltaX, deltaY);
                
            } else if (checkingPiece instanceof Bishop) {
                //diagonal line
                return canBlockDiagonalLine(alliedPieces, checkingPiece, king, deltaX, deltaY);

            } else if (checkingPiece instanceof Queen) {
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

        public static boolean pointInBoard (Point point) {
            return point.x >= 0 && point.x <= 7 && point.y >= 0 && point.y <= 7; 
        }

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

        public King getKing() {
            for (Piece piece : isWhiteTurn ? whitePieces : blackPieces) {
                if (piece instanceof King) {
                    return (King) piece;
                }
            }

            return null;
        }

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

        private void gameEnd(String message) {
            JLabel endLabel;
            
            if (message == "Checkmate") {
                endLabel = new JLabel(isWhiteTurn ? "Black WINS! Checkmate!" : "White WINS! Checkmate!");
            } else {
                endLabel = new JLabel("DRAW!");
            }

            endLabel.setForeground(isWhiteTurn ? Color.BLACK : Color.WHITE);
            endLabel.setFont(new Font("Arial", Font.BOLD, 20));
            endLabel.setBounds(50, 200, 250, 100);
            endLabel.setBackground(Color.WHITE);
            frame.add(endLabel);

            frame.revalidate();
            frame.repaint();
        }

    }
    
    public static void main(String[] args) {
        new ChessGame();
    }
}