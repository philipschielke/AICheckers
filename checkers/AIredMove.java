/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author phil
 */
public class AIredMove {

    //This is the current state of the game
    CheckersGame currentGame;
    //This array contains the legal moves at this point in the game for red.
    CheckersMove legalMoves[];

    // The constructor.
    public AIredMove(CheckersGame game, CheckersMove moves[]) {
        currentGame = game;
        legalMoves = moves;
    }

    // This is where your logic goes to make a move.
    public CheckersMove nextMove() {
        // Here are some simple ideas:
        // 1. Always pick the first move
        // return legalMoves[0];
        // 2. Pick a random move
        return legalMoves[currentGame.generator.nextInt(legalMoves.length)];  
    }

}
