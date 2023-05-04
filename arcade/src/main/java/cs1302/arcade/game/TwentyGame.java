package cs1302.arcade.game;//Package statement

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
//Needed java imports

import cs1302.arcade.util.ArcadeLogger;
//Needed local improts

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
//Needed javafx imports

/**Direction moved by tiles. */
enum Direction {
    UP, DOWN, LEFT, RIGHT
}

/**The TwentyGame class is responsible for the functionality of the 2048 game. 
 * @author Team Muskyteers
 * @version 05/07/2019
*/
public class TwentyGame {

    private Tile[][] board;
    private int boardCapacity = 14;
    private long currentScore = 0;
    private boolean gameOver = false;
    private boolean isWinner = false;
    //Instance variables
        
    /**The TwentyGame constructor initializes the fundamentals of the 2048 game,
     * such as the board and initial tiles.
     */
    public TwentyGame() {
        ArcadeLogger.logInfo("initializing 2048 game script");
        board = new Tile[4][4];
        Integer[] pos = getValidPosition();
        set(pos[0], pos[1], generateNewTile());
        pos = getValidPosition();
        set(pos[0], pos[1], generateNewTile());
    }

    /** Returns the 2048 board.
     * @return Copy of the current 2048 board.
     */
    public Tile[][] getBoard() {
        return Arrays.copyOf(board, board.length);
    }

    /** Sets a position on the board to a tile value.
     * @param x X-coordinate of board.
     * @param y Y-coordinate of board.
     * @param value Tile passed into the game board.
    */
    public void set(int x, int y, Tile value) {
        this.board[x][y] = value;
    }

    /**Returns the current score.
     * @return The current score obtained by the user.
    */
    public long getScore() {
        return currentScore;
    }

    /**Returns true if the user has won the game and reached 2048.
     * Shows the game over dialog if player has won.
     * @return Boolean corresponding to if the user has won the game or not.
     */
    public boolean isWinner() {
        return isWinner;
    }

    /** Returns true if the board is full.
     * @return Boolean corresponding to if the board has anymore space on it or not.
     */
    public boolean isFull() {
        return boardCapacity == 0;
    }

    /**
     * This method accepts a {@link Direction} and shifts the board
     * in that direction, as well as combines tiles.
     * @param d The direction the player wants to shift the board
     */
    public void executeMove(Direction d) {
        if (!gameOver()){
            ArcadeLogger.logDebug("executing move: " + d);
            switch(d){//Switch used to combine tiles based on specified direction.
                case UP:
                    combineUp();
                    break;
                case DOWN:
                    combineDown();
                    break;
                case LEFT:
                    combineLeft();
                    break;
                case RIGHT:
                    combineRight();
                    break;
            }
            if (!isFull()) {
                Integer[] newPos = getValidPosition();
                set(newPos[0], newPos[1], generateNewTile());
                boardCapacity--;
            }
        } else {
            boolean gameOver = true;
        }
    }

   
    /** Shifts tiles and values on the board to the left. */
    private void shiftLeft() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Tile t = board[i][j];
                if (t == null) {
                    for (int k = j; k < 4; k++) {
                        if (board[i][k] != null) {
                            board[i][j] = board[i][k];
                            board[i][k] = null;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**Shifts tiles and values on the board to the right. */
    private void shiftRight() {
        for (int i = 3; i >= 0; i--) {
            for (int j = 3; j >= 0; j--) {
                Tile t = board[i][j];
                if (t == null) {
                    for (int k = j; k >= 0; k--) {
                        if (board[i][k] != null) {
                            board[i][j] = board[i][k];
                            board[i][k] = null;
                            break;
                        }
                    }
                }
            }
        }
    }
    
    /**Shifts tiles and values on the board up. */
    private void shiftUp() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Tile t = board[j][i];
                if (t == null) {
                    for (int k = j; k < 4; k++) {
                        if (board[k][i] != null) {
                            board[j][i] = board[k][i];
                            board[k][i] = null;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**Shifts tiles and values on the board down. */
    private void shiftDown() {
        for (int i = 3; i >= 0; i--) {
            for (int j = 3; j >= 0; j--) {
                Tile t = board[j][i];
                if (t == null) {
                    for (int k = j; k >= 0; k--) {
                        if (board[k][i] != null) {
                            board[j][i] = board[k][i];
                            board[k][i] = null;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**Combines values and tiles on the board when shifting left. */
    private void combineLeft() {
        if (!isFull()) {
            shiftLeft();
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                Tile t = board[i][j];
                Tile t2 = board[i][j+1];
                if (t != null && t2 != null && !t.getMerged() && !t2.getMerged()) {
                    if (t2.equals(t)) {
                        t.setMerged(true);
                        int mergedVal = t.getValue()+t.getValue();
                        t.setValue(mergedVal);
                        currentScore += mergedVal;
                        if (mergedVal == 2048) {
                            isWinner = true;
                            gameOver = true;
                            break;
                        }
                        board[i][j+1] = null;
                        this.boardCapacity++;
                    }
                    shiftLeft();
                }
            }
        }
        unmarkMergedTiles();
    }

    /**Combines values and tiles on the board when shifting right. */
    private void combineRight() {
        if (!isFull()) {
            shiftRight();
        }
        for (int i = 3; i >= 0; i--) {
            for (int j = 3; j > 0; j--) {
                Tile t = board[i][j];
                Tile t2 = board[i][j-1];
                if (t != null && t2 != null && !t.getMerged() && !t2.getMerged()) {
                    if (t2.equals(t)) {
                        t.setMerged(true);
                        int mergedVal = t.getValue()+t.getValue();
                        t.setValue(mergedVal);
                        currentScore += mergedVal;
                        if (mergedVal == 2048) {
                            isWinner = true;
                            gameOver = true;
                            break;
                        }
                        board[i][j-1] = null;
                        this.boardCapacity++;
                    }
                    shiftRight();
                }
            }
        }
        unmarkMergedTiles();
    }

    /**Combines values and tiles on the board when shifting up */
    private void combineUp() {
        if (!isFull()) {
            shiftUp();
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                Tile t = board[j][i];
                Tile t2 = board[j+1][i];
                if (t != null && t2 != null && !t.getMerged() && !t2.getMerged()) {
                    if (t2.equals(t)) {
                        t.setMerged(true);
                        int mergedVal = t.getValue()+t.getValue();
                        t.setValue(mergedVal);
                        currentScore += mergedVal;
                        if (mergedVal == 2048) {
                            isWinner = true;
                            gameOver = true;
                            break;
                        }
                        board[j+1][i] = null;
                        this.boardCapacity++;
                    }
                    shiftUp();
                }
            }
        }
        unmarkMergedTiles();
    }

    /**Combines values and tiles on the board when shifting down. */
    private void combineDown() {
        if (!isFull()) {
            shiftDown();
        }
        for (int i = 3; i >= 0; i--) {
            for (int j = 3; j > 0; j--) {
                Tile t = board[j][i];
                Tile t2 = board[j-1][i];
                if (t != null && t2 != null && !t.getMerged() && !t2.getMerged()) {
                    if (t2.equals(t)) {
                        t.setMerged(true);
                        int mergedVal = t.getValue()+t.getValue();
                        t.setValue(mergedVal);
                        currentScore += mergedVal;
                        if (mergedVal == 2048) {
                            isWinner = true;
                            gameOver = true;
                            break;
                        }
                        board[j-1][i] = null;
                        this.boardCapacity++;
                    }
                    shiftDown();
                }
            }
        }
        unmarkMergedTiles();
    }

    /* Helper Methods */

    /**Checks valid moves remaining on the board and returns if the game is over.
     * @return True if the user has no more valid moves and the game is over.
     */
    public boolean gameOver() {
        if (!isFull()) {
            return false;
        }
        boolean hasValidMoves = false;
        int prev = 0;
        // Checking Horizontals
        for (int i = 0; i < 4; i++) {
            if (hasValidMoves) {
                break;
            }
            for (int j = 0; j < 4; j++) {
                if (prev == board[i][j].getValue()) {
                    hasValidMoves = true;
                    break;
                }
                prev = board[i][j].getValue();
            }
        }
        // Checking verticals
        for (int i = 0; i < 4; i++) {
            if (hasValidMoves) {
                break;
            }
            for (int j = 0; j < 4; j++) {
                if (prev == board[j][i].getValue()) {
                    hasValidMoves = true;
                    break;
                }
                prev = board[j][i].getValue();
            }
        }
        return !hasValidMoves;
    }

    /**Inserts another valid position for a tile into the board array.
     *@return  Random valid space on the game board.
    */
    protected Integer[] getValidPosition() {
        ArrayList<Integer[]> validSpaces = new ArrayList<>(0);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == null) {
                    // Insert a valid position to the ArrayList
                    Integer[] coords = {i,j};
                    validSpaces.add(coords);
                }
            }
        }
        // Select a random spot
        int random = new Random().nextInt(validSpaces.size());
        return validSpaces.get(random);
    }

    /**Unmarks tiles that have been merged in the previous turn. 
     * During each turn a tile cannot merge multiple times, the merged tile is
     * then marked so it cannot merge with a possible other tile that has the same 
     * value until the next turn.
     */
    private void unmarkMergedTiles() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != null) {
                    board[i][j].setMerged(false);
                }
            }
        }
    }

    /**Generates a new tile in a random position on the board.
     * @return A newly generated tile with a random value.
     */
    private static Tile generateNewTile() {
        int i = new Random().nextInt(10);
        if (i == 0) {
            return new Tile(4);
        } else {
            return new Tile(2);
        }
    }


}