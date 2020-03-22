package graphics;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import structure.PrioritySearchTree;

import java.io.File;
import java.io.IOException;

public class MainApplication extends Application {

    public GraphicsContext gc;
    public PrioritySearchTree tree;

    public static void main(String[] args) {
        launch(args);
    }

    public void draw(PrioritySearchTree tree, GraphicsContext gc) {
        //TODO
    }

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
            if (file != null) {
                //TODO Extract the data from the selected file
                try {
                    tree = new PrioritySearchTree(file.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

        VBox root = new VBox();
        Scene scene = new Scene(root, 800, 600);

        Canvas canvas = new Canvas(scene.getWidth() - menuBar.getWidth(), scene.getHeight() - menuBar.getHeight());
        gc = canvas.getGraphicsContext2D();
        root.getChildren().addAll(menuBar, canvas);

        stage.setTitle("Windowing App");
        stage.setScene(scene);
        stage.show();
    }
}