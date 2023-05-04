package cs1302.arcade.util; //Package statemnet

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
//Needed imports

/**
 * The universal MenuBar for the ArcadeApp.
 * @author Team Muskyteers
 * @version 4/30/19
 */
public class ArcadeMenuBar extends MenuBar {

    /**
     * Constructs the MenuBar with all shared menus.
     */
    public ArcadeMenuBar() {
        super();
        this.setPrefWidth(640);
        // Defining menus
        Menu m1 = new Menu("File");
        Menu m2 = new Menu("View");
        Menu m3 = new Menu("Game");
        Menu m4 = new Menu("Help");
        // Defining common items
        MenuItem exit = new MenuItem("Exit");
        MenuItem about = new MenuItem("About");
        MenuItem highScores = new MenuItem("High Scores");
        // Linking actions
        exit.setOnAction(e -> {
            exitApplication();
        });
        // Adding components
        m2.getItems().addAll(highScores);
        m1.getItems().addAll(exit);
        m4.getItems().addAll(about);
        this.getMenus().addAll(m1,m2,m3,m4);
    }

    /**
     * Exits the overall application immediately.
     */
    public static void exitApplication() {
        ArcadeLogger.logInfo("application exiting");
        System.exit(0);
    }
}