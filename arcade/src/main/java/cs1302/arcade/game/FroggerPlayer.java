package cs1302.arcade.game; //Package statement

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//Needed javafx imports
import javafx.scene.shape.Rectangle;

import cs1302.arcade.util.ArcadeLogger;
import cs1302.arcade.game.FroggerObstacle;
//Needed local imports

/**The FroggerPlayer class is responsible for creating and
 * implementing the player object (frog).
 * @author Team Muskyteers
 * @version 05/07/2019
 */
public class FroggerPlayer extends ImageView {

    private int x;
    private int y;
    int currentLevel;
    //Instance variables

    /** The FroggerPlayer constructor creates a FroggerPlayer object which is 
     * representative of the frog in the Frogger game.
     * @param level The current game level
     */
    public FroggerPlayer(int level) {
        super(new Image("frogger/frog" + level + ".png", 30, 28, false, false));
        currentLevel = level;
        this.setTranslateX(301);
        this.setTranslateY(391);
        this.setFitWidth(30);
        this.setFitHeight(28);
        x = 300;
        y = 391;
    }
    
    /**
     * Returns the current x-coordinate value the player is in.
     * @return The current x-coordinate value of the frog.
     */
    public int getPlayerX() {
        return x;
    }
    /**
     * Returns the current y-coordinate value the player is in.
     * @return the current y-coordinate value of the frog.
     */
    public int getPlayerY() {
        return y;
    }

    /**
     * Updates the player's stored x value
     * @param val the new x coordinate
     */
    public void updateX(double val) {
        x = (int)val;
    }

    /**
     *  Determines whether the player is touching the correct pad.
     * @return player on pad?
     */
    public boolean checkOnPad(Rectangle pad) {
        return this.getBoundsInParent().intersects(pad.getBoundsInParent());
    }

    /**
     * Determines whether the player is over water.
     * @return over water?
     */
    public boolean isOverWater() {
        return (y > 60 && y < 210);
    }

    /**
     * Moves the frog in the specified direction.
     * @param d The direction in which the frog will move.
     */
    public void moveDirection(Direction d) {
        switch (d) { //Switch used to move frog based on specified direction
            case UP:
                if (y - 30 >= 0) {
                    y -= 30;
                    this.setTranslateY(y);
                }
                break;
            case DOWN:
                if (y + 30 <= 420) {
                    y += 30;
                    this.setTranslateY(y);
                }
                break;
            case LEFT:
                if (x - 30 >= 0) {
                    x -= 30;
                    this.setTranslateX(x);
                }
                break;
            case RIGHT:
                if (x + 30 <= 600) {
                    x += 30;
                    this.setTranslateX(x);
                }
                break;
        }
        ArcadeLogger.logDebug("moving, x: " + x + ", y: " + y);
    }

    /**
     * Returns whether the supplied obstacle is intersecting the player.
     * @param obs The obstacle
     * @return the intersection status
     */
    public boolean intersects(FroggerObstacle obs) {
        return this.getBoundsInParent().intersects(obs.getBoundsInParent());
    }
}