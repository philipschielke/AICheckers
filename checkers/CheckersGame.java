
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import static java.lang.Math.abs;

/**
 * This class does the work of letting the users play checkers, and it displays
 * the checkerboard.
 */
interface GameListener {

    public void console(String str);

    public void refreshBoard();

    public void gameInProgress(boolean gameState);
}

public class CheckersGame {

    CheckersData boardData;
    CheckersMove[] legalMoves;  // An array containing the legal moves for the
    //   current player.

    int selectedRow, selectedCol;  // If the current player has selected a piece to
    //     move, these give the row and column
    //     containing that piece.  If no piece is
    //     yet selected, then selectedRow is -1.

    int currentPlayer;      // Whose turn is it now?  The possible values 
    //    are CheckersData.RED and CheckersData.BLACK.

    Random generator = new Random();
    boolean gameInProgress = false;
    Boolean aiRed = false;
    Boolean aiBlack = true;

    java.util.List<GameListener> listeners = new ArrayList<GameListener>();

    public void addListener(GameListener toAdd) {
        listeners.add(toAdd);
    }

    private void console(String str) {
        for (GameListener bl : listeners) {
            bl.console(str);
        }
    }

    private void refreshBoard() {
        for (GameListener bl : listeners) {
            bl.refreshBoard();
        }
    }

    private void setGameInProgress(boolean gameState) {
        gameInProgress = gameState;
        for (GameListener bl : listeners) {
            bl.gameInProgress(gameState);
        }
    }

    public int currentPlayer() {
        return currentPlayer;
    }

    public boolean gameInProgress() {
        return gameInProgress;
    }

    /**
     * Constructor. Create the buttons and label. Listens for mouse clicks and
     * for clicks on the buttons. Create the board and start the first game.
     */
    public CheckersGame(CheckersData passedBoard) {
        boardData = passedBoard;
    }

    /**
     * Start a new game
     */
    public void doNewGame() {
        setGameInProgress(false);
        console("New Game");
        if (gameInProgress() == true) {
            // This should not be possible, but it doens't hurt to check.
            console("Finish the current game first!");
            return;
        }

        boardData.setUpGame();   // Set up the pieces.
        currentPlayer = CheckersData.RED;   // RED moves first.
        legalMoves = boardData.getLegalMoves(CheckersData.RED);  // Get RED's legal moves.
        selectedRow = -1;   // RED has not yet selected a piece to move.
        console("Red:  Make your move.");
        setGameInProgress(true);
        refreshBoard();
    }

    /**
     * Current player resigns. Game ends. Opponent wins.
     */
    void doResign() {
        System.out.println("Resign Game");
        if (gameInProgress() == false) {  // Should be impossible.
            console("There is no game in progress!");
            return;
        }
        if (currentPlayer == CheckersData.RED) {
            gameOver("RED resigns.  BLACK wins.");
        } else {
            gameOver("BLACK resigns.  RED wins.");
        }
    }

    /**
     * The game ends. The parameter, str, is displayed as a message to the user.
     * The states of the buttons are adjusted so players can start a new game.
     * This method is called when the game ends at any point in this class.
     */
    public void gameOver(String str) {
        console(str);
        setGameInProgress(false);
    }

    /**
     * This is called by mousePressed() when a player clicks on the square in
     * the specified row and col. It has already been checked that a game is, in
     * fact, in progress.
     */
    public void doClickSquare(int row, int col) {
        if (!gameInProgress()) {
            console("Click \"New Game\" to start a new game.");
        } else {
            if (currentPlayer() == CheckersData.BLACK
                    && aiBlack == true) {
                console("Click to move black");
                AIblackMove blacksMove = new AIblackMove(this,legalMoves);
                doMakeMove(blacksMove.nextMove());
                return;
            } else if (currentPlayer() == CheckersData.RED
                    && aiRed == true) {
                console("Click to move red");
                AIredMove redsMove = new AIredMove(this,legalMoves);
                // AIredMoveBetter redsMove = new AIredMoveBetter(this, legalMoves);
                doMakeMove(redsMove.nextMove());
                return;
            }
        


        /* If the player clicked on one of the pieces that the player
         can move, mark this row and col as selected and return.  (This
         might change a previous selection.)  Reset the message, in
         case it was previously displaying an error message. */
        for (int i = 0; i < legalMoves.length; i++) {
            System.out.println("In one");
            if (legalMoves[i].fromRow == row && legalMoves[i].fromCol == col) {
                selectedRow = row;
                selectedCol = col;
                if (currentPlayer == CheckersData.RED) {
                    console("RED:  Make your move.");
                } else {
                    console("BLACK:  Make your move.");
                }
                refreshBoard();
                return;
            }
        }

        /* If no piece has been selected to be moved, the user must first
         select a piece.  Show an error message and return. */
        if (selectedRow < 0) {
                        System.out.println("In two");

            console("Click the piece you want to move.");
            return;
        }

        /* If the user clicked on a square where the selected piece can be
         legally moved, then make the move and return. */
        for (int i = 0; i < legalMoves.length; i++) {
                        System.out.println("In three");

            if (legalMoves[i].fromRow == selectedRow && legalMoves[i].fromCol == selectedCol
                    && legalMoves[i].toRow == row && legalMoves[i].toCol == col) {
                doMakeMove(legalMoves[i]);
                return;
            }
        }

        /* If we get to this point, there is a piece selected, and the square where
         the user just clicked is not one where that piece can be legally moved.
         Show an error message. */
        console("Click the square you want to move to.");
        }
    }  // end doClickSquare()

    /**
     * This is called when the current player has chosen the specified move.
     * Make the move, and then either end or continue the game appropriately.
     */
    void doMakeMove(CheckersMove move) {

        boardData.makeMove(move, false);

        /* If the move was a jump, it's possible that the player has another
         jump.  Check for legal jumps starting from the square that the player
         just moved to.  If there are any, the player must jump.  The same
         player continues moving. */
        
        if (move.isJump()) {
            legalMoves = boardData.getLegalJumpsFrom(currentPlayer, move.toRow, move.toCol);
            if (legalMoves != null) {
                if (currentPlayer == CheckersData.RED) {
                    console("RED:  You must continue jumping.");
                } else {
                    console("BLACK:  You must continue jumping.");
                }
                selectedRow = move.toRow;  // Since only one piece can be moved, select it.
                selectedCol = move.toCol;
                refreshBoard();
                return;
            }
        }

        /* The current player's turn is ended, so change to the other player.
         Get that player's legal moves.  If the player has no legal moves,
         then the game ends. */
        if (currentPlayer == CheckersData.RED) {
            currentPlayer = CheckersData.BLACK;
            legalMoves = boardData.getLegalMoves(currentPlayer);
            if (legalMoves == null) {
                gameOver("BLACK has no moves.  RED wins.");
            } else if (legalMoves[0].isJump()) {
                console("BLACK:  Make your move.  You must jump.");
            } else {
                console("BLACK:  Make your move.");
            }
        } else {
            currentPlayer = CheckersData.RED;
            legalMoves = boardData.getLegalMoves(currentPlayer);
            if (legalMoves == null) {
                gameOver("RED has no moves.  BLACK wins.");
            } else if (legalMoves[0].isJump()) {
                console("RED:  Make your move.  You must jump.");
            } else {
                console("RED:  Make your move.");
            }
        }

        /* Set selectedRow = -1 to record that the player has not yet selected
         a piece to move. */
        selectedRow = -1;

        /* As a courtesy to the user, if all legal moves use the same piece, then
         select that piece automatically so the user won't have to click on it
         to select it. */
        if (legalMoves != null) {
            boolean sameStartSquare = true;
            for (int i = 1; i < legalMoves.length; i++) {
                if (legalMoves[i].fromRow != legalMoves[0].fromRow
                        || legalMoves[i].fromCol != legalMoves[0].fromCol) {
                    sameStartSquare = false;
                    break;
                }
            }
            if (sameStartSquare) {
                selectedRow = legalMoves[0].fromRow;
                selectedCol = legalMoves[0].fromCol;
            }
        }

        /* Make sure the board is redrawn in its new state. */
        refreshBoard();

    }  // end doMakeMove();


       // This code simulates a move on cur_board.  You pass it a board,
    // a move, and a player, and it makes the move on that board including
    // additional jumps if those are needed.
    void simulateMove(CheckersData cur_board, CheckersMove move, int player) {

        cur_board.makeMove(move);

        /* If the move was a jump, it's possible that the player has another
         jump.  Check for legal jumps starting from the square that the player
         just moved to.  If there are any, the player must jump.  The same
         player continues moving. That means their could be another choice to make.
         */
        if (move.isJump()) {
            CheckersMove[] newMoves;
            newMoves = cur_board.getLegalJumpsFrom(player, move.toRow, move.toCol);
            if (newMoves != null) {
                simulateMove(cur_board, newMoves[0], player);
            }
    	     // we have to keep jumping until we can't!  If there is more than one jump
            // we are just going to have to pick the first one.  More logic could go here
        }
        return;
    }
}  // end class Board
