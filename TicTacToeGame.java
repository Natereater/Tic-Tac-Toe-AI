

/**
 * @author Nathaniel
 * TicTacToeGame
 * 2019-4-1
 */
import java.util.Scanner;
public class TicTacToeGame
{
    
    private int[] board;
    
    // The two AI's used to play as X and O respectively
    private TicTacToeQ X_AI;
    private TicTacToeQ O_AI;
    
    private int numTrainingGames;
    
    
    /*
     * On the Board:
     * 0 -> Empty
     * 1 -> X
     * 2 -> O
     * 
     * if not done getWinner() returns 0
     * if X wins getWinner() returns 1
     * if O wins getWinner() returns 2
     * if Tie getWinner() returns 3
     */
    private final int X = 1;
    private final int O = 2;
    private final int TIE = 3;
    
    private Scanner get;
    
    
    
    /**
     * Constructor
     * @param games (number of games that the AI will use to train on)
     */
    public TicTacToeGame( int games )
    {
        numTrainingGames = games;
        board = new int[9];
        
        // Initialize the two AI's
        X_AI = new TicTacToeQ();
        O_AI = new TicTacToeQ();
        
        // Scanner is used for when user will play against the AI's
        get = new Scanner(System.in);
        
        // Train the AI's
        train();
    }
    
    
    
    
    
    
    
    
    
    /**
     * Play numTrainingGames number of games with two AI's
     * AI's will move until the game is decided and then
     */
    public void train()
    {
        boolean turn = true;
        // True = X's turn
        // False = O's turn
        
        int result = 0;
        
        for (int game = 0; game < numTrainingGames; game++)
        {
            // every game starts with X's turn
            turn = true;
            while (getWinner() == 0)
            {
                if (turn)
                {
                    // let X's AI make a move
                    board[X_AI.makeMove(board)] = X;
                }
                
                else
                {
                    // let O's AI make a move
                    board[O_AI.makeMove(board)] = O;
                }
                
                //change turn
                turn = !turn;
            } // end individual game
            
            // get the final result of the game
            result = getWinner();
            
            // Have X and O learn from their respective Win or Loss or Tie
            // Tie is a minor loss for X and a minor win for O
            X_AI.learn(result == X  , result == TIE);
            O_AI.learn(result == O || result == TIE , result == TIE);
            
            // reset the board and if 5% of training has passed, print training progress
            clearBoard();
            printProgress(game);
            
        }
    }
    
    
    
    
    
    
    
    
    
    /**
     * Watch as the X and O AI's play against each other
     * The two AI's will use makeMove() when deciding to move
     * @param numGames
     */
    public void displayGames( int numGames )
    {
        boolean turn = true;
        int result = 0;
        
        for (int game = 0; game < numGames; game++)
        {
            
            System.out.println("\n\n#####################################################\n");
            
            turn = true;
            while (getWinner() == 0)
            {
                if (turn)
                {
                    board[X_AI.makeMove(board)] = X;
                }
                
                else
                {
                    board[O_AI.makeMove(board)] = O; // letter O not 0
                }
                
                turn = !turn;
                
                displayBoard();
            }
            
            result = getWinner();
            
            
            X_AI.learn(result == X || result == TIE , result == TIE);
            O_AI.learn(result == O || result == TIE , result == TIE);
            
            
            clearBoard();
        }
    }
    
    
    
    
    
    
    
    
    
    /**
     * User will play as X against a fully trained O AI
     * The AI will use makeAdvancedMove() to decide moves
     * @param numGames (number of games to play against the O AI)
     */
    public void playAgainstO( int numGames )
    {
        boolean turn = true;
        int result = 0;
        
        for (int game = 0; game < numGames; game++)
        {
            
            // This line will print between every new game
            System.out.println("\n\n#####################################################\n");
            
            turn = true;
            while (getWinner() == 0)
            {
                if (turn)
                {
                    System.out.print("Make Move: ");
                    board[get.nextInt()] = X;
                }
                
                else
                {
                    board[O_AI.makeAdvancedMove(board)] = O; // letter O not 0
                }
                
                turn = !turn;
                
                displayBoard();
            }
            
            result = getWinner();
            
            printGameResults( result );
            O_AI.learn(result == O || result == TIE , result == TIE);
            
            
            clearBoard();
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Play as O against a fully trained X AI
     * AI will use makeAdvancedMove() to decide moves
     * @param numGames
     */
    public void playAgainstX( int numGames )
    {
        boolean turn = true;
        int result = 0;
        
        for (int game = 0; game < numGames; game++)
        {
            
            System.out.println("\n\n#####################################################\n");
            
            turn = true;
            while (getWinner() == 0)
            {
                if (turn)
                {
                    board[X_AI.makeAdvancedMove(board)] = X;
                }
                
                else
                {
                    System.out.print("Make Move: ");
                    board[get.nextInt()] = O; // letter O not 0
                }
                
                turn = !turn;
                
                displayBoard();
            }
            
            result = getWinner();
            
            printGameResults( result );
            X_AI.learn(result == X || result == TIE , result == TIE);
            
            
            clearBoard();
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    // Print out the board's current state
    private void displayBoard()
    {
        for (int i = 0; i < board.length; i++)
        {
            if (board[i] == 0)
                System.out.print( "- ");
            
            if (board[i] == 1)
                System.out.print( "X " );
            
            if (board[i] == 2)
                System.out.print( "O " );
            
            
            if (i % 3 == 2)
            {
                System.out.println();
            }
        }
        System.out.println("\n\n");
    }
    
    
    
    
    
    
    
    
    
    /**
     * Return
     * 0 if game not yet done
     * 1 if X has won
     * 2 if O has won
     * 3 if it is a catsgame
     * 
     * @return 0, 1, 2, or 3 
     */
    private int getWinner()
    {
        if (       (board[0] == 1 && board[1] == 1 && board[2] == 1) 
                || (board[3] == 1 && board[4] == 1 && board[5] == 1)
                || (board[6] == 1 && board[7] == 1 && board[8] == 1) 
                || (board[0] == 1 && board[3] == 1 && board[6] == 1) 
                || (board[1] == 1 && board[4] == 1 && board[7] == 1) 
                || (board[2] == 1 && board[5] == 1 && board[8] == 1) 
                || (board[0] == 1 && board[4] == 1 && board[8] == 1) 
                || (board[2] == 1 && board[4] == 1 && board[6] == 1) )
        {
            return 1;
        }
        
        
        else if (  (board[0] == 2 && board[1] == 2 && board[2] == 2) 
                || (board[3] == 2 && board[4] == 2 && board[5] == 2)
                || (board[6] == 2 && board[7] == 2 && board[8] == 2) 
                || (board[0] == 2 && board[3] == 2 && board[6] == 2) 
                || (board[1] == 2 && board[4] == 2 && board[7] == 2) 
                || (board[2] == 2 && board[5] == 2 && board[8] == 2) 
                || (board[0] == 2 && board[4] == 2 && board[8] == 2) 
                || (board[2] == 2 && board[4] == 2 && board[6] == 2) )
        {
            return 2;
        }
        
        
        
        else if (   board[0] != 0
                &&  board[1] != 0
                &&  board[2] != 0
                &&  board[3] != 0
                &&  board[4] != 0
                &&  board[5] != 0
                &&  board[6] != 0
                &&  board[7] != 0
                &&  board[8] != 0)
        {
            return 3;
        }
        
        else
        {
            return 0;
        }
    }// end getWinner
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Print a loading bar with every 5% progress the training has towards being complete
     * @param game
     */
    private void printProgress( int game )
    {
        if (game % (numTrainingGames / 20) == 0)
        {
            System.out.print("[" );
            for (int j = 0; j < game / (numTrainingGames / 20); j++)
            {
                System.out.print("=");
            }
            for (int j = game / (numTrainingGames / 20); j < 20; j++)
            {
                System.out.print(".");
            }
            System.out.println("] " + (5 * game / (numTrainingGames / 20)) + "%" );
        }
    }
    
    
    
    
    
    
    // Reset the board between games
    private void clearBoard()
    {
        for (int i = 0; i < board.length; i++)
        {
            board[i] = 0;
        }
    }
    
    
    
    
    /**
     * print who wins a given game with the result
     * @param result
     */
    private void printGameResults( int result )
    {
        if (result == 1)
        {
            System.out.println("X WINS");
        }
        
        else if (result == 2)
        {
            System.out.println("O WINS");
        }
        
        else
        {
            System.out.println("CATSGAME");
        }
    }
    
    
    

}
