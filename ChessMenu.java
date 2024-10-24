import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class ChessMenu {
    // Variables to hold the background image and buttons
    private static BufferedImage backgroundImage;
    private static JButton startButton;   // Declare startButton here to access it later
    private static JButton quitButton;    // Declare quitButton here
    private static JButton tutorialButton; // Declare tutorialButton here

    // Function that creates and activates the ChessGame instance
    public static void activateChessGame() {
        new ChessGame();  // Create and "activate" the ChessGame
    }

    // Function that opens the tutorial image in full screen
    public static void openTutorial() {
        // Create a new frame for the tutorial
        JFrame tutorialFrame = new JFrame("Tutorial");
        tutorialFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Load the tutorial image
        BufferedImage tutorialImage = null;
        try {
            tutorialImage = ImageIO.read(new File("sprites/tutorial.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        if (tutorialImage != null) {
            // Calculate the size to maintain the aspect ratio
            int imgWidth = tutorialImage.getWidth();
            int imgHeight = tutorialImage.getHeight();
            double imgAspectRatio = (double) imgWidth / imgHeight;
    
            // Get screen dimensions
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;
    
            // Calculate the scaled dimensions
            int scaledWidth, scaledHeight;
            if (imgAspectRatio > (double) screenWidth / screenHeight) {
                // Image is wider than the screen
                scaledWidth = screenWidth;
                scaledHeight = (int) (screenWidth / imgAspectRatio);
            } else {
                // Image is taller than the screen
                scaledHeight = screenHeight;
                scaledWidth = (int) (screenHeight * imgAspectRatio);
            }
    
            // Set the frame size to the scaled dimensions
            tutorialFrame.setSize(scaledWidth, scaledHeight);
            tutorialFrame.setLocationRelativeTo(null); // Center the frame on the screen
    
            // Create a label with the image
            JLabel label = new JLabel(new ImageIcon(tutorialImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH)));
            tutorialFrame.getContentPane().add(label); // Add label to the frame
    
            // Make the frame undetectable to avoid title bar
            tutorialFrame.setUndecorated(true);
            tutorialFrame.setVisible(true); // Display the tutorial frame
        }
    }
    

    // Create the full-screen menu GUI
    public static void createMenu() {
        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("sprites/MainMenu.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the frame (window)
        JFrame frame = new JFrame("Chess Game Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the frame to full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);  // Remove title bar for a cleaner look

        // Create a panel to hold components
        JPanel panel = new BackgroundPanel();
        panel.setLayout(null); // Use null layout for absolute positioning
        frame.add(panel);

        // Add components to the panel (e.g., buttons)
        placeComponents(panel);

        // Add a component listener to reposition the buttons when the frame is resized
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                positionButtons(panel); // Center the buttons when the frame is resized
            }
        });

        // Display the frame
        frame.setVisible(true);
        positionButtons(panel); // Center the buttons when first displayed
    }

    // Function to place components inside the panel
    private static void placeComponents(JPanel panel) {
        // Create a "Start Game" button with an image
        startButton = new JButton();
        try {
            // Load the button image
            ImageIcon buttonIcon = new ImageIcon(ImageIO.read(new File("sprites/startbutton.png")));
            // Scale the image while maintaining aspect ratio
            Image scaledImage = buttonIcon.getImage().getScaledInstance(-1, 55, Image.SCALE_SMOOTH);
            startButton.setIcon(new ImageIcon(scaledImage));  // Set the scaled icon for the button

            // Set button properties
            startButton.setContentAreaFilled(false);  // Make button transparent
            startButton.setBorderPainted(false);  // Remove border
            startButton.setFocusPainted(false);  // Remove focus outline
            startButton.setFocusable(false);  // Make the button not focusable
            
            panel.add(startButton);

            // Add action listener to the button
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    activateChessGame();  // Activate the ChessGame when clicked
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a "Quit" button with an image
        quitButton = new JButton();
        try {
            // Load the quit button image
            ImageIcon quitIcon = new ImageIcon(ImageIO.read(new File("sprites/quitbutton.png")));
            // Scale the image while maintaining aspect ratio
            Image scaledQuitImage = quitIcon.getImage().getScaledInstance(-1, 55, Image.SCALE_SMOOTH);
            quitButton.setIcon(new ImageIcon(scaledQuitImage));  // Set the scaled icon for the button

            // Set button properties
            quitButton.setContentAreaFilled(false);  // Make button transparent
            quitButton.setBorderPainted(false);  // Remove border
            quitButton.setFocusPainted(false);  // Remove focus outline
            quitButton.setFocusable(false);  // Make the button not focusable
            
            panel.add(quitButton);

            // Add action listener to the quit button
            quitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);  // Exit the application when clicked
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a "Tutorial" button with an image
        tutorialButton = new JButton();
        try {
            // Load the tutorial button image
            ImageIcon tutorialIcon = new ImageIcon(ImageIO.read(new File("sprites/tutorialbutton.png")));
            // Scale the image while maintaining aspect ratio
            Image scaledTutorialImage = tutorialIcon.getImage().getScaledInstance(-1, 55, Image.SCALE_SMOOTH);
            tutorialButton.setIcon(new ImageIcon(scaledTutorialImage));  // Set the scaled icon for the button

            // Set button properties
            tutorialButton.setContentAreaFilled(false);  // Make button transparent
            tutorialButton.setBorderPainted(false);  // Remove border
            tutorialButton.setFocusPainted(false);  // Remove focus outline
            tutorialButton.setFocusable(false);  // Make the button not focusable
            
            panel.add(tutorialButton);

            // Add action listener to the tutorial button
            tutorialButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openTutorial();  // Open the tutorial when clicked
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to center the buttons
    private static void positionButtons(JPanel panel) {
        if (startButton != null && quitButton != null && tutorialButton != null) {
            // Center the "Start Game" button on the screen
            int startX = (panel.getWidth() - startButton.getPreferredSize().width) / 2;
            int startY = (panel.getHeight() - startButton.getPreferredSize().height + 740) / 2;
            startButton.setBounds(startX, startY, startButton.getPreferredSize().width, startButton.getPreferredSize().height);

            // Center the "Quit" button below the "Start Game" button
            int quitX = (panel.getWidth() - quitButton.getPreferredSize().width+1000) / 2;
            int quitY = (panel.getHeight() - quitButton.getPreferredSize().height + 740) / 2;
            quitButton.setBounds(quitX, quitY, quitButton.getPreferredSize().width, quitButton.getPreferredSize().height);

            // Center the "Tutorial" button above the "Start Game" button
            int tutorialX = (panel.getWidth() - tutorialButton.getPreferredSize().width-1000) / 2;
            int tutorialY = (panel.getHeight() - tutorialButton.getPreferredSize().height + 740) / 2;
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
                // Set rendering hints for better quality
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        // Run the full-screen menu on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> createMenu());
    }
}

