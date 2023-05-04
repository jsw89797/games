package cs1302.arcade.game;//Package statement

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
//Needed javafx imports

import cs1302.arcade.util.ArcadeLogger;
import cs1302.arcade.util.ArcadeMenuBar;
//Needed local imports

/**TwentyInterface is responsible for creating and initializing the 2048 game scene.
 * @author Team Muskyteers
 * @version 05/07/2019
 */
public class TwentyInterface extends Scene {

    TwentyGame game;
    VBox container;
    VBox gameView;
    ImageView[][] tiles;
    MenuBar menu;
    GridPane imageGrid;
    long currentScore = 0;
    Text scoreText;
    HBox scoreContainer;
    Stage stage;
    Scene main;
    //Instance variables

    /**The Twenty Interface constructor creates the 2048 scene.
     * @param main Main menu scene of the Arcade App.
     * @param stage Main stage for the Arcade app.
     */
    public TwentyInterface(Scene main, Stage stage) {
        // supplying empty root to super class
        super(new VBox(), 640, 480);
        ArcadeLogger.logInfo("initializing 2048 interface");
        this.main = main;
        this.stage = stage;
        // setting scene root as container, casting from Parent to Pane
        // this line is *essential*
        container = (VBox) getRoot();
        container.setPrefHeight(500);
        container.setPrefWidth(640);

        menu = new ArcadeMenuBar();
        MenuItem forfeit = new MenuItem("Quit to Menu");
        forfeit.setOnAction(exitToMainMenu(main,stage));
        menu.getMenus().get(2).getItems().add(forfeit);

        gameView = new VBox();
        gameView.setPrefHeight(480);
        gameView.setPrefWidth(640);
        gameView.setStyle("-fx-background-image: url('2048/board.png');");
        container.getChildren().addAll(menu, gameView);

        game = new TwentyGame();
        tiles = new ImageView[4][4];
        imageGrid = generateTiles();
        imageGrid.setPadding(new Insets(20, 0, 0, 135));
        scoreContainer = generateScoreView();
        gameView.getChildren().addAll(imageGrid, scoreContainer);
        refreshTilePositions();
        this.setOnKeyPressed(slideDirection());
    }

    /**Generates tiles for 2048 game into scene.
     * @return GridPane which tiles are on.
     */
    private GridPane generateTiles() {
        GridPane pane = new GridPane();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[i][j] = new ImageView();
                tiles[i][j].setFitWidth(75);
                tiles[i][j].setFitHeight(75);
                pane.add(tiles[i][j], j, i);
            }
        }
        pane.setHgap(23);
        pane.setVgap(23);
        return pane;
    }

    /**Constructs the score box for the 2048 game scene.
     * @return The HBox in which the score is located in the 2048 scene.
     */
    private HBox generateScoreView() {
        HBox scoreContainer = new HBox();
        scoreContainer.setPadding(new Insets(15,0,0,190));
        scoreText = new Text(Long.toString(currentScore));
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(new Font(30));
        scoreText.setX(50);
        scoreText.setY(50);
        scoreContainer.getChildren().addAll(scoreText);
        return scoreContainer;
    }

    /**Updates the position of the tiles in the 2048 scene. */
    private void refreshTilePositions() {
        Tile[][] board = game.getBoard();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[i][j].setImage(null);
                if (board[i][j] != null) {
                    String imageUrl = "2048/" + board[i][j].getValue() + ".png";
                    tiles[i][j].setImage(new Image(imageUrl, 75, 75, false, false));
                }
            }
        }
    }


    /**Updates the score text object to reflect user's current score in the scene. */
    private void updateScore() {
        currentScore = game.getScore();
        scoreText.setText(Long.toString(currentScore));
        ArcadeLogger.logDebug(Long.toString(currentScore));
    }

    /**Sets the direction the tile should slide in the executeMove(Direction d) method. */
    private EventHandler<? super KeyEvent> slideDirection(){
        return event -> {
            if(event.getCode() == KeyCode.LEFT){
                game.executeMove(Direction.LEFT);
            }
            if(event.getCode() == KeyCode.RIGHT){
                game.executeMove(Direction.RIGHT);
            }
            if(event.getCode() == KeyCode.DOWN){
                game.executeMove(Direction.DOWN);
            }
            if(event.getCode() == KeyCode.UP){
                game.executeMove(Direction.UP);
            }
            refreshTilePositions();
            updateScore();
            if(game.gameOver()) {
                if (game.isWinner()) {
                    showGameOverDialog(true);
                    stage.setScene(main);
                } else {
                    showGameOverDialog(false);
                    stage.setScene(main);
                }
            }
            
        };
    }

    /** Returns an event used for setting the scene back to the main menu.
     * @param scene Main menu scene of arcade app.
     * @param stage Main stage of arcade app.
     * @return Event setting the scene back to the main scene.
     */
    private EventHandler<ActionEvent> exitToMainMenu(Scene scene, Stage stage){
        return (e) ->{
            stage.setScene(scene);
        };
    }

    /**Shows game over dialog box for when player wins/loses.
     * @param winner Specifies if player is a winner or not.
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
    
}