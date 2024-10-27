import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class ChessMenu {
    // Variables to hold the background image and buttons
    private static BufferedImage backgroundImage;
    private static JButton startButton;
    private static JButton quitButton;
    private static JButton tutorialButton;
    

    // Function that creates and activates the ChessGame instance
    public static void activateChessGame(JFrame frame) {
        // Start the chess game
        new ChessGame();  // Create and "activate" the ChessGame
    
        // Set up a timer to dispose of the menu frame after 0.5 seconds
        Timer timer = new Timer(500, event -> frame.dispose());
        timer.setRepeats(false);
        timer.start();
    }
    

    // Function that opens the tutorial image in full screen
    public static void openTutorial(JFrame previousFrame) {
        JFrame tutorialFrame = new JFrame("Tutorial");
        tutorialFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        BufferedImage tutorialImage = null;
        try {
            tutorialImage = ImageIO.read(new File("sprites/tutorial.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        if (tutorialImage != null) {
            int imgWidth = tutorialImage.getWidth();
            int imgHeight = tutorialImage.getHeight();
            double imgAspectRatio = (double) imgWidth / imgHeight;
    
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;
    
            int scaledWidth, scaledHeight;
            if (imgAspectRatio > (double) screenWidth / screenHeight) {
                scaledWidth = screenWidth;
                scaledHeight = (int) (screenWidth / imgAspectRatio);
            } else {
                scaledHeight = screenHeight;
                scaledWidth = (int) (screenHeight * imgAspectRatio);
            }
    
            // Scale the tutorial image
            Image scaledImage = tutorialImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            BufferedImage bufferedScaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedScaledImage.createGraphics();
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();
    
            tutorialFrame.setSize(scaledWidth, scaledHeight);
            tutorialFrame.setLocationRelativeTo(null);
    
            // Create main panel to hold both the image and the button
            JPanel panel = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(bufferedScaledImage, 0, 0, null); // Draw the pre-scaled image
                }
            };
    
            // Load and add the button to the same panel
            try {
                BufferedImage buttonIcon = ImageIO.read(new File("sprites/MainMenubuttons.png"));
                JButton exitButton = new JButton(new ImageIcon(buttonIcon));
                exitButton.setBounds(1170, 770, buttonIcon.getWidth(), buttonIcon.getHeight());
                exitButton.setBorderPainted(false);
                exitButton.setContentAreaFilled(false);
                exitButton.setFocusPainted(false);
                exitButton.addActionListener(e -> tutorialFrame.dispose());
                exitButton.addActionListener(e -> SoundPlayer.playSound("sounds/close.wav"));
                panel.add(exitButton);
            } catch (IOException e) {
                e.printStackTrace();
            }
    
            tutorialFrame.setContentPane(panel);
            tutorialFrame.setUndecorated(true);
            tutorialFrame.setVisible(true);
        }
    }
    
    // Create the full-screen menu GUI
    public static void createMenu() {
        try {
            backgroundImage = ImageIO.read(new File("sprites/MainMenu.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Chess Game Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        JPanel panel = new BackgroundPanel();
        panel.setLayout(null);
        frame.add(panel);

        placeComponents(panel, frame);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                positionButtons(panel);
            }
        });

        frame.setVisible(true);
        positionButtons(panel);
    }

private static void placeComponents(JPanel panel, JFrame frame) {
    startButton = new JButton();
    try {
        ImageIcon buttonIcon = new ImageIcon(ImageIO.read(new File("sprites/MainMenubuttons.png")));
        Image scaledImage = buttonIcon.getImage().getScaledInstance(-1, 55, Image.SCALE_SMOOTH);
        startButton.setIcon(new ImageIcon(scaledImage));
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setFocusable(false);
        panel.add(startButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("sounds/open.wav");
                activateChessGame(frame);
                

            }
        });
    } catch (IOException e) {
        e.printStackTrace();
    }

    quitButton = new JButton();
    try {
        ImageIcon quitIcon = new ImageIcon(ImageIO.read(new File("sprites/MainMenubuttons.png")));
        Image scaledQuitImage = quitIcon.getImage().getScaledInstance(-1, 55, Image.SCALE_SMOOTH);
        quitButton.setIcon(new ImageIcon(scaledQuitImage));
        quitButton.setContentAreaFilled(false);
        quitButton.setBorderPainted(false);
        quitButton.setFocusPainted(false);
        quitButton.setFocusable(false);
        panel.add(quitButton);

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("sounds/close.wav");
                System.exit(0);
            }
        });
    } catch (IOException e) {
        e.printStackTrace();
    }

    tutorialButton = new JButton();
    try {
        ImageIcon tutorialIcon = new ImageIcon(ImageIO.read(new File("sprites/MainMenubuttons.png")));
        Image scaledTutorialImage = tutorialIcon.getImage().getScaledInstance(-1, 55, Image.SCALE_SMOOTH);
        tutorialButton.setIcon(new ImageIcon(scaledTutorialImage));


        tutorialButton.setOpaque(false);
        tutorialButton.setContentAreaFilled(false);
        tutorialButton.setBorderPainted(false);
        tutorialButton.setFocusPainted(false);
        tutorialButton.setFocusable(false);
        
        panel.add(tutorialButton);

        tutorialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("sounds/open.wav");
                openTutorial(frame);
            }
        });
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    // Function to position buttons as specified
    private static void positionButtons(JPanel panel) {
        if (startButton != null && quitButton != null && tutorialButton != null) {
            int startX = (panel.getWidth() - startButton.getPreferredSize().width) / 2;
            int startY = (panel.getHeight() - startButton.getPreferredSize().height)/ 2 - 104;
            startButton.setBounds(startX, startY, startButton.getPreferredSize().width, startButton.getPreferredSize().height);

            int quitX = (panel.getWidth() - quitButton.getPreferredSize().width ) / 2;
            int quitY = (panel.getHeight() - startButton.getPreferredSize().height)/ 2 + 50;
            quitButton.setBounds(quitX, quitY, quitButton.getPreferredSize().width, quitButton.getPreferredSize().height);

            int tutorialX = (panel.getWidth() - tutorialButton.getPreferredSize().width) / 2;
            int tutorialY = (panel.getHeight() - startButton.getPreferredSize().height) / 2 -30;
            tutorialButton.setBounds(tutorialX, tutorialY, tutorialButton.getPreferredSize().width, tutorialButton.getPreferredSize().height);
        }
    }

    // Custom panel class to paint the background image
    static class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createMenu());
    }
}
