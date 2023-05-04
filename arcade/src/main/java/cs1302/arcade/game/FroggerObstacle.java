package cs1302.arcade.game; //Package statement

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//Needed javafx imports

import java.util.Random;
//Needed java imports

/** Types of obstacles generated. */
enum Obstacles {
    CAR,
    TURTLE,
    LOG
}

/**The FroggerObstacle class initializes the Obstacles in the Frogger game, extending the 
 * ImageView class it creates each Obstacle as an ImageView which will translate across the 
 * frogger game screen from right to left.
 * @author Team Muskyteers
 * @version 05/07/2019
 */
public class FroggerObstacle extends ImageView {

    private Random rand = new Random();
    private Obstacles type;
    private boolean finished = false;
    private int[] carLanes = {240,270,300,330,360};
    private int[] turtleLanes = {150,90};
    private int[] logLanes = {60,120,180};
    //Instance variables

    /** The FroggerObstacle constructor creates each Frogger obstacle using the Obstacle enum.
     * @param o Type of obstacle that will be generated (i.e cars, logs, etc.).
     */
    public FroggerObstacle(Obstacles o) {
        super();
        int model;
        int lane;
        type = o;
        switch(o) { //Switch used to set and translate images based on specified obstacle type.
            case CAR:
                model = rand.nextInt(5);
                lane = rand.nextInt(5);
                this.setImage(new Image("frogger/vehicle" + model + ".png"));
                this.setTranslateY(carLanes[lane]);
                this.setTranslateX(660);
                break;
            case TURTLE:
                lane = rand.nextInt(2);
                this.setImage(new Image("frogger/turtle.png"));
                this.setTranslateY(turtleLanes[lane]);
                this.setTranslateX(-50);
                break;
            case LOG:
                model = rand.nextInt(3);
                lane = rand.nextInt(3);
                this.setImage(new Image("frogger/log" + model + ".png"));
                this.setTranslateY(logLanes[lane]);
                this.setTranslateX(660);
                break;
        }
    }

    /**Returns the type of Obstacle created (i.e cars, logs, etc.).
     * @return Type of obstacle created.
     */
    public Obstacles getType() {
        return type;
    }

    /**
     * Gets the value of the boolean variable finished.
     * @return Returns true when obstacle is finished crossing the screen.
    */
    public boolean getFinished() {
        return finished;
    }

     /** Sets the value of the boolean variable finished. */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}