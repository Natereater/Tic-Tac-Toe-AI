

/**
 * @author Nathaniel
 * TicTacToeQ
 * 2019-4-1
 * 
 * This class is the actual AI that will learn to play a single side (X or O) of ticTacToe
 */
import java.util.ArrayList;
public class TicTacToeQ
{
    
    // A 2d array storing every single possibility that a TicTacToe board could have
    private int[][] states;
    
    // A 2d Array with the likelyhood of making every move for each given possible game-state
    private double[][] weights;
    
    // stateHstory stores ever game state that is put before the AI in a single game
    private ArrayList<Integer> stateHistory;
    
    // moveHistory stores the history of every move the AI made when presented with each state
    private ArrayList<Integer> moveHistory;
    
    
    // some valuable constants
    private final int NUM_SQUARES = 9;
    private final int DEFAULT_WEIGHT = 9;
    private final double LEARN_RATE = .1;
    
    
    
    
    public TicTacToeQ()
    {
        states = new int[(int) Math.pow(3,NUM_SQUARES)][NUM_SQUARES];
        weights = new double[(int) Math.pow(3,NUM_SQUARES)][NUM_SQUARES];
        initializeStates();
        initializeWeights();
        
        stateHistory = new ArrayList<Integer>();
        moveHistory = new ArrayList<Integer>();
    }
    
    
    
    
    
    
    
    
    public int makeMove( int[] currentState )
    {
        int move = 0;
        int given = findState(currentState);
        double[] qValues = new double[NUM_SQUARES];
        double sum = 0;
        double randomizer = 0;
        
        for (int i = 0; i < qValues.length; i++)
        {
            qValues[i] = weights[given][i];
            sum += qValues[i];
        }
        randomizer = Math.random() * sum;
        
        
        int i = 0;
        while (randomizer > qValues[i])
        {
            move++;
            randomizer -= qValues[i];
            i++;
        }
        
        
        while (states[given][move] != 0)
        {
            move++;
        }
        
        
        stateHistory.add(given);
        moveHistory.add(move);
        return move;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public int makeAdvancedMove( int[] currentState )
    {
        int given = findState(currentState);
        double max = -1;
        int move = 0;
        
        for (int i = 0; i < NUM_SQUARES; i++)
        {
            if (weights[given][i] > max)
            {
                max = weights[given][i];
                move = i;
            }
        }
        
        
        
        while (states[given][move] != 0)
        {
            move++;
        }
        
        stateHistory.add(given);
        moveHistory.add(move);
        return move;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void learn( boolean win , boolean tie )
    {
        int currentState = 0;
        int givenMove = 0;
        double adjustor = (win) ? LEARN_RATE : -LEARN_RATE;
        
        if (tie)
        {
            adjustor /= 3;
        }
        
        for (int i = 0; i < moveHistory.size(); i++ )
        {
            currentState = stateHistory.get(i);
            givenMove = moveHistory.get(i);
            
            for ( int j = 0; j < NUM_SQUARES; j++)
            {
                if (j == givenMove)
                {
                    weights[currentState][j] += adjustor;
                    if (weights[currentState][j] < 0)
                    {
                        weights[currentState][j] = 0;
                    }
                }
                else if (states[currentState][j] == 0)
                {
                    weights[currentState][j] -= adjustor / 2;
                    if (weights[currentState][j] < 0)
                    {
                        weights[currentState][j] = 0;
                    }
                }
            }
            
        }
        
        stateHistory.clear();
        moveHistory.clear();
    }
    
    
    
    
    
    
    
    
    
    public int findState( int[] currentState)
    {
        int state = 0;
        for (int i = 0; i < currentState.length; i++)
        {
            state += ( currentState[8 - i] * Math.pow(3 , i) );
        }
        
        return state;
    }
    
    
    
    
    
    
    
    
    
    private void initializeStates()
    {
        for (int layer = 1; layer < states.length; layer++)
        {
            
            for (int i = 0; i < NUM_SQUARES; i++)
            {
                states[layer][i] = states[layer - 1][i];
            }
            
            
            states[layer][NUM_SQUARES - 1] += 1;
            for (int i = NUM_SQUARES - 1; i >= 0; i--)
            {
                if (states[layer][i] >= 3)
                {
                    states[layer][i] = 0;
                    states[layer][i - 1] += 1;
                }
            }
        }
        System.out.println("States Generated");
    }
    
    
    
    
    
    
    
    
    
    
    private void initializeWeights()
    {
        for (int i = 0; i < weights.length; i++)
        {
            for (int j = 0; j < weights[0].length; j++)
            {
                if (states[i][j] == 0)
                {
                    weights[i][j] = DEFAULT_WEIGHT;
                }
                else
                {
                    weights[i][j] = 0;
                }
            }
        }
    }
}
