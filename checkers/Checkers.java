import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * This panel lets two users play checkers against each other.
 * Red always starts the game.  If a player can jump an opponent's
 * piece, then the player must jump.  When a player can make no more
 * moves, the game ends.
 * 
 * The class has a main() routine that lets it be run as a stand-alone
 * application.  The application just opens a window that uses an object
 * of type Checkers as its content pane.
 * 
 * There is also a nested class, Checker.Applet, that can be used
 * as an applet version of the program.  The applet size should be 
 * 350-by-250 (or very close to that).
 * 
 */
public class Checkers extends JPanel implements ActionListener {
/*     CheckersMove[] legalMoves;  // An array containing the legal moves for the
                                  //   current player.
 */     
     Responder responder = new Responder();

     JButton resignButton;   // Button that a player can use to end 
     JButton newGameButton;  // Button for starting a new game.
     JLabel message;         // Label for displaying messages to the user.
     JPanel radioPanel;
     JRadioButton AIred; 
     JRadioButton AIblack; 
     JRadioButton humanRed;
     JRadioButton humanBlack;
     ButtonGroup redGroup;
     ButtonGroup blackGroup;
     
     CheckersData boardData;  // The data for the checkers board is kept here.
                              // This board is also responsible for generating
                              // lists of legal moves.
     BoardPanel boardPanel;
     CheckersGame checkersGame;
     

   /**
    * Main routine makes it possible to run Checkers as a stand-alone
    * application.  Opens a window showing a Checkers panel; the program
    * ends when the user closes the window.
    */
   public static void main(String[] args) {
      JFrame window = new JFrame("Checkers");
      Checkers content = new Checkers();
      window.setContentPane(content);
      window.pack();
      Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
      window.setLocation( (screensize.width - window.getWidth())/2,
            (screensize.height - window.getHeight())/2 );
      window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      window.setResizable(false);  
      window.setVisible(true);
   }

   /**
    * The constructor creates the Board (which in turn creates and manages
    * the buttons and message label), adds all the components, and sets
    * the bounds of the components.  A null layout is used.  (This is
    * the only thing that is done in the main Checkers class.)
    */
   public Checkers() {
      
      /* init the game objects */
      boardData = new CheckersData();  // The data for the checkers board is kept here.
                           // This board is also responsible for generating
                           // lists of legal moves.
      checkersGame = new CheckersGame(boardData);
      boardPanel = new BoardPanel(boardData, checkersGame);
      checkersGame.addListener(responder);

      System.out.println("Game in progress: " + checkersGame.gameInProgress());

      /* init UI */
      /* Create the components and add them to the applet. */
      setLayout(null);  // I will do the layout myself.
      setPreferredSize( new Dimension(350,250) );
      
      setBackground(new Color(0,150,0));  // Dark green background.
      
      AIred = new JRadioButton("AI red");
      AIblack = new JRadioButton("AI black");
      humanRed = new JRadioButton("Human red");
      humanBlack = new JRadioButton("Human black");
      humanRed.setSelected(true);
      AIblack.setSelected(true);
      AIred.addActionListener(this);
      humanRed.addActionListener(this);
      AIblack.addActionListener(this);
      humanBlack.addActionListener(this);
      redGroup = new ButtonGroup();
      blackGroup = new ButtonGroup();
      redGroup.add(AIred);
      redGroup.add(humanRed);
      blackGroup.add(AIblack);
      blackGroup.add(humanBlack);
      radioPanel = new JPanel(new GridLayout(0, 1));
      radioPanel.add(AIblack);
      radioPanel.add(humanBlack);
      radioPanel.add(AIred);
      radioPanel.add(humanRed);


      resignButton = new JButton("Resign");
      resignButton.addActionListener(this);
      resignButton.setEnabled(false);
      newGameButton = new JButton("New Game");
      newGameButton.addActionListener(this);
      newGameButton.setEnabled(true);
      message = new JLabel("",JLabel.CENTER);
      message.setFont(new  Font("Serif", Font.BOLD, 14));
      message.setForeground(Color.green);

      add(boardPanel);
      add(newGameButton);
      add(resignButton);
      add(radioPanel, BorderLayout.LINE_START);
      add(message);
      
      /* Set the position and size of each component by calling
       its setBounds() method. */   
      boardPanel.setBounds(20,20,164,164); // Note:  size MUST be 164-by-164 !
      newGameButton.setBounds(210, 20, 120, 30);
      resignButton.setBounds(210, 60, 120, 30);
      radioPanel.setBounds(210,100,120,80);
      message.setBounds(0, 200, 350, 30);
      
   } // end constructor


    /**
      * Respond to user's click on one of the two buttons.
    */
    public void actionPerformed(ActionEvent evt) {
         System.out.println("ActionEvent");
         Object src = evt.getSource();
         if (src == newGameButton)
            checkersGame.doNewGame();
         else if (src == resignButton)
            checkersGame.doResign();
         else if (src == AIblack)
             checkersGame.aiBlack = true;
         else if (src == humanBlack)
             checkersGame.aiBlack = false;
          else if (src == AIred)
             checkersGame.aiRed = true;
         else if (src == humanRed)
             checkersGame.aiRed = false;

    }

    private class Responder implements GameListener {
          public void console(String str) {
              System.out.println("Message: " + str);
              message.setText(str);
          }

          public void refreshBoard() {
              boardPanel.repaint();
          }

          public void gameInProgress(boolean inProgress) {
              if (inProgress){
                  newGameButton.setEnabled(false);
                  resignButton.setEnabled(true);
              } else {
                  newGameButton.setEnabled(true);
                  resignButton.setEnabled(false);
              }
          }
    }

  
} // end class Checkers

