package graphics;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) {
        // Declare some objects
        final FileChooser fileChooser = new FileChooser();

        // Create MenuBar
        MenuBar menuBar = new MenuBar();

        // Create menus
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");

        // Create MenuItems
        MenuItem openFileItem = new MenuItem("Open File");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem editWindowItem = new MenuItem("Edit window");

        openFileItem.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null){
                //TODO Extract the data from the selected file
            }
        });
        exitItem.setOnAction(event -> System.exit(0));
        editWindowItem.setOnAction(event -> {
            //TODO Allow the user to modify the window size
        });


        // Add menuItems to the Menus
        fileMenu.getItems().addAll(openFileItem, exitItem);
        editMenu.getItems().addAll(editWindowItem);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Windowing App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}