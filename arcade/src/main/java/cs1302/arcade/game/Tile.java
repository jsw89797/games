package cs1302.arcade.game; //Package statement

/**
 * This class represents an individual tile in TwentyGame
 * @author Team Muskyteers
 * @version 4/30/19
 */
public class Tile {

    private Integer value = 0;
    private boolean merged = false;
    //Instance variables

    /**
     * Initializes a new tile of the specified value
     * @param val value of the tile
     */
    public Tile(int val) {
        value = val;
    }

    /**
     * Gets the value of the tile.
     * @return the value as an integer
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Get the merged status of the tile
     * @return merged status
     */
    public boolean getMerged() {
        return merged;
    }

    /**
     * Sets the numerical value of the tile
     * @param val new numerical value
     */
    public void setValue(int val) {
        value = val;
    }

    /**
     * Sets the merged status of the tile
     * @param merge new merge status
     */
    public void setMerged(boolean merge) {
        merged = merge;
    }

    /**
     * Determines if two tiles are equal
     * @param t the tile to compare
     * @return the equality status
     */
    public boolean equals(Tile t) {
        return this.value == t.getValue();
    }
}