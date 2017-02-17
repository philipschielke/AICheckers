import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/*** 
    * This panel displays a 160-by-160 checkerboard pattern with
    * a 2-pixel black border.  It is assumed that the size of the
    * panel is set to exactly 164-by-164 pixels.  
 **/

public class BoardPanel extends JPanel implements MouseListener {

      CheckersData boardData;
      CheckersGame checkersGame;
      CheckersMove[] legalMoves;  // An array containing the legal moves for the
                                  //   current player.

      int selectedRow, selectedCol;  // If the current player has selected a piece to
                                     //     move, these give the row and column
                                     //     containing that piece.  If no piece is
                                     //     yet selected, then selectedRow is -1.

      public BoardPanel(CheckersData passedBoard, CheckersGame passedGame){
      	  addMouseListener(this);
      	  boardData = passedBoard;
      	  checkersGame = passedGame;	
      	  setBackground(Color.BLACK);
      }

	     /**
       * Draw  checkerboard pattern in gray and lightGray.  Draw the
       * checkers.  If a game is in progress, hilite the legal moves.
       */
      public void paintComponent(Graphics g) {
         System.out.println("Painting!");
         /* Draw a two-pixel black border around the edges of the canvas. */
         
         g.setColor(Color.black);
         g.drawRect(0,0,getSize().width-1,getSize().height-1);
         g.drawRect(1,1,getSize().width-3,getSize().height-3);
         
         /* Draw the squares of the checkerboard and the checkers. */
         
         for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
               if ( row % 2 == col % 2 )
                  g.setColor(Color.LIGHT_GRAY);
               else
                  g.setColor(Color.GRAY);
               g.fillRect(2 + col*20, 2 + row*20, 20, 20);
               switch (boardData.pieceAt(row,col)) {
               case CheckersData.RED:
                  g.setColor(Color.RED);
                  g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                  break;
               case CheckersData.BLACK:
                  g.setColor(Color.BLACK);
                  g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                  break;
               case CheckersData.RED_KING:
                  g.setColor(Color.RED);
                  g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                  g.setColor(Color.WHITE);
                  g.drawString("K", 7 + col*20, 16 + row*20);
                  break;
               case CheckersData.BLACK_KING:
                  g.setColor(Color.BLACK);
                  g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                  g.setColor(Color.WHITE);
                  g.drawString("K", 7 + col*20, 16 + row*20);
                  break;
               }
            }
         }
         
         /* If a game is in progress, hilite the legal moves.   Note that legalMoves
          is never null while a game is in progress. */      
         
         if (checkersGame.gameInProgress()) {
         	legalMoves = boardData.getLegalMoves(checkersGame.currentPlayer());
            /* First, draw a 2-pixel cyan border around the pieces that can be moved. */
            g.setColor(Color.cyan);
            for (int i = 0; i < legalMoves.length; i++) {
               g.drawRect(2 + legalMoves[i].fromCol*20, 2 + legalMoves[i].fromRow*20, 19, 19);
               g.drawRect(3 + legalMoves[i].fromCol*20, 3 + legalMoves[i].fromRow*20, 17, 17);
            }
               /* If a piece is selected for moving (i.e. if selectedRow >= 0), then
                draw a 2-pixel white border around that piece and draw green borders 
                around each square that that piece can be moved to. */
            if (selectedRow >= 0) {
               g.setColor(Color.white);
               g.drawRect(2 + selectedCol*20, 2 + selectedRow*20, 19, 19);
               g.drawRect(3 + selectedCol*20, 3 + selectedRow*20, 17, 17);
               g.setColor(Color.green);
               for (int i = 0; i < legalMoves.length; i++) {
                  if (legalMoves[i].fromCol == selectedCol && legalMoves[i].fromRow == selectedRow) {
                     g.drawRect(2 + legalMoves[i].toCol*20, 2 + legalMoves[i].toRow*20, 19, 19);
                     g.drawRect(3 + legalMoves[i].toCol*20, 3 + legalMoves[i].toRow*20, 17, 17);
                  }
               }
            }
         }

      }  // end paintComponent()


      /**
       * Respond to a user click on the board.  If no game is in progress, show 
       * an error message.  Otherwise, find the row and column that the user 
       * clicked and call doClickSquare() to handle it.
       */
      public void mousePressed(MouseEvent evt) {
	  	  int col = (evt.getX() - 2) / 20;
		    int row = (evt.getY() - 2) / 20;
		    if (col >= 0 && col < 8 && row >= 0 && row < 8){
			     checkersGame.doClickSquare(row, col);
		    }
      }
      
      public void mouseReleased(MouseEvent evt) { }
      public void mouseClicked(MouseEvent evt) { }
      public void mouseEntered(MouseEvent evt) { }
      public void mouseExited(MouseEvent evt) { }
      
}
      