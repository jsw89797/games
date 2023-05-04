package cs1302.arcade;//Package statement

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
//Needed javafx imports

import cs1302.arcade.game.FroggerInterface;
import cs1302.arcade.game.TwentyInterface;
import cs1302.arcade.util.ArcadeLogger;
import cs1302.arcade.util.ArcadeMenuBar;
//Needed local imports

/** This class generates the scene and functionality of the main menu.
 * @author
 * @version 05/07/20
*/
public class ArcadeApp extends Application {

    Random rng = new Random(); // random number generator

    // JavaFX Variables
    Stage stage;
    VBox pane = new VBox();
    MenuBar menubar;
    VBox menuView;
    ListView<String> gameList;
    Scene currentGame;
    Scene mainScene;
    Text title;
    Text subtitle;

    /** {@inheritdoc} */
    @Override
    public void start(Stage stage) {
        
        ArcadeLogger.logInfo("initializing main application");

        this.stage = stage;

        menubar = new ArcadeMenuBar();
        menuView = generateMainMenu();
        pane.getChildren().add(menubar);
        pane.getChildren().addAll(menuView);
        mainScene = new Scene(pane, 640, 510);
        stage.setTitle("cs1302-arcade");
        stage.setMaxHeight(500);
        stage.setMinHeight(500);
        stage.setMaxWidth(640); 
        stage.setMinWidth(640);
        stage.setScene(mainScene);
        stage.sizeToScene();
        stage.show();

        // the group must request input focus to receive key events
        // @see
        // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#requestFocus--
        pane.requestFocus();

    } // start

    /**Generates the main menu scene for the Arcade app.
     * @return VBox with main menu elements.
     */
    private VBox generateMainMenu() {

        ArcadeLogger.logInfo("initializing main menu");

        VBox container = new VBox();
        container.setPrefWidth(640);
        container.setPrefHeight(440);

        VBox titleContainer = new VBox();
        title = new Text("cs1302-arcade");
        subtitle = new Text("a collection of JavaFX games, by Team Muskyteers");
        title.setFont(new Font(36));
        subtitle.setFont(new Font(18));
        VBox items = new VBox();
        items.getChildren().addAll(title, subtitle);
        titleContainer.getChildren().addAll(title, subtitle);
        
        ArrayList<String> games = new ArrayList<>(0);
        games.add("Frogger");
        games.add("2048");
        gameList = new ListView<>();
        gameList.getItems().setAll(games);
        gameList.setPrefWidth(100);
        gameList.setPrefHeight(200);
        Button playGame = new Button("Play Game");
        playGame.setOnAction(launchGame());

        container.getChildren().addAll(titleContainer, gameList, playGame);
        container.setMargin(titleContainer, new Insets(25, 0, 40, 25));
        container.setMargin(gameList, new Insets(0, 50, 15, 25));
        container.setMargin(playGame, new Insets(0, 50, 25, 25));
        return container;
    }

    /* Helper Methods */

    /**Creates the event setting the scene to user's choice of game.
     * @return Event setting the scene to user's choice of game.
     */
    private EventHandler<ActionEvent> launchGame() {
        return e -> {
            String gameVal = gameList.getSelectionModel().getSelectedItem();
            switch (gameVal) { //Switch used to set game scene based on user selection.
                case "Frogger":
                    currentGame = new FroggerInterface(mainScene, stage);
                    stage.setScene(currentGame);
                    break;
                case "2048":
                    currentGame = new TwentyInterface(mainScene, stage);
                    stage.setScene(currentGame);
                    break;
                default:
                    break;
            }
        };
    }

} // ArcadeApp
