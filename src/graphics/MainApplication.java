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
import structure.PSTNode;
import structure.PrioritySearchTree;
import structure.Windower;

import java.io.File;

public class MainApplication extends Application {

    public GraphicsContext gc;

    public static void main(String[] args) {
        launch(args);
    }

    public void draw(String path, GraphicsContext gc) {
        Windower windower = new Windower(path);
        PrioritySearchTree pst = new PrioritySearchTree(path);
        for (PSTNode n : windower.reportInSubtree(pst)) {
            if (n != null && n.getLinkedTo() != null)
                gc.strokeLine(n.getX(), n.getY(), n.getLinkedTo().getX(), n.getLinkedTo().getY());
        }
    }

    public void clearCanvas(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
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
        MenuItem clearCanvasItem = new MenuItem("Clear canvas");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem editWindowItem = new MenuItem("Edit window");

        openFileItem.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                clearCanvas(gc);
                System.out.println(file.getPath());
                draw(file.getPath(), gc);
            }
        });
        clearCanvasItem.setOnAction(event -> clearCanvas(gc));
        exitItem.setOnAction(event -> System.exit(0));
        editWindowItem.setOnAction(event -> {
            //TODO Allow the user to modify the window size
        });

        // Add menuItems to the Menus
        fileMenu.getItems().addAll(openFileItem, clearCanvasItem, exitItem);
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