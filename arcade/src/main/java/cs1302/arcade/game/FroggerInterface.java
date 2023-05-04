package cs1302.arcade.game;//Package statement

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
//Needed javafx imports

import java.util.ArrayList;
import java.util.List;
//Needed java improts

import cs1302.arcade.util.ArcadeLogger;
import cs1302.arcade.util.ArcadeMenuBar;
//Needed local imports

/** 
 * This is the main class for the Frogger game, it generates the
 * stack pane and places needed objects (Frog, cars, etc..) into the 
 * pane.
 * @author Team Muskyteers
 * @version 05/07/2019
*/
public class FroggerInterface extends Scene {

    // JavaFX and Util References
    private Scene main;
    private Stage stage;
    private VBox container;
    private StackPane gameView;
    private MenuBar menu;
    private Text scoreText;
    private HBox scoreContainer;
    private Timeline animations;
    private Timeline generator;
    private Timeline remover;
    private Rectangle[] pads;
    private int[] padCoords = {300, 135, 475};
    private String direction = "UP";

    // Game Objects
    private int level = 1;
    private int lives = 3;
    private long currentScore = 0;
    private FroggerPlayer player;
    private List<FroggerObstacle> obstacles;
    private List<KeyFrame> frames;
    private boolean riding = false;
    private boolean hit = false;
    private int cycles = 0;

    /** This is the constructor for FroggerInterface.java. In this
    * our Frogger game scene is made.
    * @param main Main menu scene for Arcade app.
    * @param stage Main stage for Arcade app.
    */
    public FroggerInterface(Scene main, Stage stage) {
        // supplying empty root to super class
        super(new VBox(), 640, 480);
        this.main = main;
        this.stage = stage;
        ArcadeLogger.logInfo("initializing frogger interface");
        // setting scene root as container, casting from Parent to Pane
        // this line is *essential*
        container = (VBox) getRoot();
        container.setPrefHeight(500);
        container.setPrefWidth(640);
        menu = new ArcadeMenuBar();
        MenuItem forfeit = new MenuItem("Quit to Menu");
        forfeit.setOnAction(exitToMainMenu(main,stage));
        menu.getMenus().get(2).getItems().add(forfeit);
        // Setting up main game view
        gameView = new StackPane();
        gameView.setPrefHeight(480);
        gameView.setPrefWidth(640);
        gameView.setAlignment(Pos.TOP_LEFT);
        gameView.setStyle("-fx-background-image: url('frogger/level.png');");
        container.getChildren().addAll(menu, gameView);
        // Setting up game logic
        obstacles = new ArrayList<FroggerObstacle>();
        frames = new ArrayList<KeyFrame>();
        player = new FroggerPlayer(1);
        animations = generateTimeline();
        initializeTimers();
        animations.getKeyFrames().add(checkGameState());
        animations.play();
        pads = generatePads();
        scoreContainer = generateScoreView();
        
        gameView.getChildren().addAll(player, scoreContainer);
        this.setOnKeyPressed(moveDirection());
        
    }

    /*   Generators and Initializers */
    
    /**
     * This method is responsible for setting the timeline that makes our
     * obstacles (i.e logs, cars, etc.) move across the screen.
     */
    public Timeline generateTimeline(){
        Timeline a = new Timeline();
        a.setCycleCount(Timeline.INDEFINITE);
        return a;
    }

     /**Sets the timer for our KeyFrame, as the level increases the timer will be lower making 
     * objects much faster.
     */
    private void initializeTimers() {
        generator = generateTimeline();
        remover = generateTimeline();
        int duration = 500;
        switch(this.level) {
            case 1:
                duration = 2000;
                break;
            case 2:
                duration = 1500;
                break;
            case 3:
                duration = 1000;
                break;
        } 
        generator.getKeyFrames().add(obstacleGenerator(duration));
        generator.play();
        remover.getKeyFrames().add(obstacleRemover());
        remover.play();
    }

     /**Constructs the score box for the Frogger game scene.
     * @return The HBox in which the score is located in the Frogger scene.
     */
    private HBox generateScoreView() {
        HBox scoreContainer = new HBox();
        scoreContainer.setPadding(new Insets(15,0,0,20));
        scoreText = new Text(Long.toString(currentScore));
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(new Font(30));
        scoreText.setX(50);
        scoreText.setY(50);
        scoreContainer.getChildren().addAll(scoreText);
        return scoreContainer;
    }

    /**Creates a car Obstacle for the Frogger game. */
    private void spawnCar() {
        FroggerObstacle obs = new FroggerObstacle(Obstacles.CAR);
        KeyFrame k = generateObstacleAnimation(obs);
        gameView.getChildren().add(obs);
        frames.add(k);
        obstacles.add(obs);
        animations.getKeyFrames().add(k);
    }

    /**Creates a log Obstacle for the Frogger game.  */
    private void spawnLog() {
        FroggerObstacle obs = new FroggerObstacle(Obstacles.TURTLE);
        KeyFrame k = generateObstacleAnimation(obs);
        gameView.getChildren().add(obs);
        frames.add(k);
        obstacles.add(obs);
        animations.getKeyFrames().add(k);
    }



    /**Creates a turtle Obstacle for the Frogger game. */
    private void spawnTurtle() {
        FroggerObstacle obs = new FroggerObstacle(Obstacles.LOG);
        KeyFrame k = generateObstacleAnimation(obs);
        gameView.getChildren().add(obs);
        frames.add(k);
        obstacles.add(obs);
        animations.getKeyFrames().add(k);
    }

    /**
     * Presents a game over dialog to the player
     * @param winner the winning status of the game
     */
    public void showGameOverDialog(boolean winner) {
        Platform.runLater(() -> {
            // Creates a dialog with type error
            Alert alert;
            if (winner) {
                alert = new Alert(AlertType.CONFIRMATION, "You have won the game!");
            } else {
                alert = new Alert(AlertType.ERROR, "You have lost the game. Try again!");
            }
            alert.show();
        });
    }

    /*   Game Logic Methods */

    /**Removes objects that have crossed the entire screen. */
    private void removeFinishedObstacles() {
        for (int i = 0; i < obstacles.size(); i++) {
            FroggerObstacle obs = obstacles.get(i);
            KeyFrame frame = frames.get(i);
            if (obs.getFinished()) {
                animations.getKeyFrames().remove(frame);
                gameView.getChildren().remove(obs);
                obstacles.remove(i);
                frames.remove(i);
            }
        }
    }

    /**Ends frogger game after a win. 
     * @param isWinner Boolean returning true if user has won the game.
    */
    private void endGame(boolean isWinner) {
        showGameOverDialog(isWinner);
        stage.setScene(main);
        animations.stop();
        generator.stop();
        remover.stop();
    }

    /**Removes a life from the player, and determines if they have anymore remaining.
     * @return True if player has greater than 0 lives.
     */
    private boolean killPlayer() {
        ArcadeLogger.logInfo("player died");
        gameView.getChildren().remove(player);
        player = new FroggerPlayer(this.level);
        gameView.getChildren().add(player);
        player.toFront();
        lives--;
        return lives > 0;
    }

    /**Increases the user's current level. */
    private void levelUp() {
        level++;
        lives = 3;
        FroggerPlayer newPlayer = new FroggerPlayer(level);
        gameView.getChildren().remove(player);
        player = newPlayer;
        gameView.getChildren().add(player);
    }

    /**Generates lily pads for frog.
     * @return Rectangle array of the frog's lily pads.
     */
    private Rectangle[] generatePads() {
        Rectangle[] r = new Rectangle[3];
        for (int i = 0; i < 3; i++) {
            r[i] = new Rectangle(20,20);
            r[i].setTranslateX(padCoords[i]);
            r[i].setTranslateY(31);
            r[i].setFill(Color.TRANSPARENT);
        }
        return r;
    }

    /**Translates logs across the X.
     * @param o Obstacle object that will be animated.
     */
    private void logMover(FroggerObstacle o) {
        if (o.getTranslateX() < -200) {
            o.setFinished(true);
        } else {
            o.setTranslateX(o.getTranslateX() - 1*level);
            if (player.intersects(o)) {
                riding = true;
                player.setTranslateX(player.getTranslateX() - 1*level);
                player.updateX(player.getTranslateX());
            }
        }
    }

    /**Translates turtles across the X.
     * @param o Obstacle object that will be animated.
     */
    private void turtleMover(FroggerObstacle o) {
        if (o.getTranslateX() > 700) {
            o.setFinished(true);
        } else {
            o.setTranslateX(o.getTranslateX() + 2*level);
            if (player.intersects(o)) {
                riding = true;
                player.setTranslateX(player.getTranslateX() + 2*level);
                player.updateX(player.getTranslateX());
            } 
        }
    }

    /**Translates cars across the X. 
     * @param o Obstacle object that will be animated.
    */
    private void carMover(FroggerObstacle o) {
        if (o.getTranslateX() < -200) {
            o.setFinished(true);
        } else {
            o.setTranslateX(o.getTranslateX() - 1*level);
            if (player.intersects(o)) {
                hit = true;
            } 
        }
    }

    /**Updates the score text object to reflect user's current score in the scene. */
    private void updateScore() {
        scoreText.setText(Long.toString(currentScore));
        ArcadeLogger.logDebug(Long.toString(currentScore));
    }

    /*   Keyframe Generators */

    /**
     * This method is responsibile for the animation 
     * of our obstacles (i.e logs, cars, etc.).
     * @param o Obstacle that is animated.
     * @return KeyFrame that makes obstacles move from right to left,
     *  used for timeline
     */
    private KeyFrame generateObstacleAnimation(FroggerObstacle o) {
        int duration = 15;
        return new KeyFrame(Duration.millis(duration), (e) -> {
            if (o.getType() == Obstacles.TURTLE) {
                turtleMover(o);
            } else if (o.getType() == Obstacles.LOG) {
                logMover(o);
            } else if (o.getType() == Obstacles.CAR) {
                carMover(o);
            }
        });
    }

    /**Generates obstacles in the Frogger game.
     * @return KeyFrame with generated obstacles and their translation across X.
     */
    private KeyFrame obstacleGenerator(int duration) {
        return new KeyFrame(
            Duration.millis(duration),
            (e) -> {
                ArcadeLogger.logDebug("creating obstacles");
                animations.play();
                spawnCar();
                spawnCar();
                spawnTurtle();
                spawnLog();
            }
        );
    }

    /**Removes obstacles in the game after they have crossed the screen.
     * @return Keyframe used to remove Obstacles after they have moved across entire screen.
     */
    private KeyFrame obstacleRemover() {
        return new KeyFrame(Duration.millis(3000), (e) -> {
            ArcadeLogger.logDebug("cleared obstacles, " + 
            animations.getKeyFrames().size() +  " remaining");
            removeFinishedObstacles();
        });
    }

    /** Checks if player is alive or dead.
     * @return KeyFrame checking if player is alive or dead every 15 milliseconds.
     */
    private KeyFrame checkGameState() {
        return new KeyFrame(Duration.millis(15), (e) -> {
            player.toFront();
            if (player.isOverWater() && !riding && cycles > 1) {
                cycles = 0;
                if (!killPlayer()) {
                    endGame(false);
                }
            } else if (player.isOverWater() && !riding) {
                cycles++;
            }
            if (hit) {
                hit = false;
                if (!killPlayer()) {
                    endGame(false);
                }
            }
            if (player.checkOnPad(pads[level-1])) {
                if (level < 3) {
                    levelUp();
                } else {
                    endGame(true);
                }
            }
            riding = false;
        });
    }

    /*   Event Handlers */

    /**
     * This method is responsible for handling which arrow key is pressed and then
     * moves the frog in that direction accordingly.
     * @return event sets and moves frog in the direction indicated by the user
     */
    private EventHandler<? super KeyEvent> moveDirection(){
        return event -> {
            if(event.getCode() == KeyCode.LEFT){
                player.moveDirection(Direction.LEFT);
                direction = "LEFT";
                //String set to prevent user from getting free points
            }
            if(event.getCode() == KeyCode.RIGHT){
                player.moveDirection(Direction.RIGHT);
                direction = "RIGHT";
            }
            if(event.getCode() == KeyCode.DOWN){
                player.moveDirection(Direction.DOWN);
                direction = "DOWN";
            }
            if(event.getCode() == KeyCode.UP){
                player.moveDirection(Direction.UP);
                if(!direction.equals("DOWN")){
                    currentScore += 10 * level;
                    //If-statement used to check if player went down before going up
                    //Prevents user from getting free points
                }   
                direction = "UP";
            }
            updateScore();
        };
    }

    /** Returns an event used for setting the scene back to the main menu.
     * @param scene Main menu scene of arcade app.
     * @param stage Main stage of arcade app.
     * @return Event setting the scene back to the main scene.
     */
    private EventHandler<ActionEvent> exitToMainMenu(Scene scene, Stage stage){
        return (e) -> {
            stage.setScene(scene);
        };
    }
}