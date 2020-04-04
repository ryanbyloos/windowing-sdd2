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
import structure.Window;

import java.io.File;

public class MainApplication extends Application {

    public GraphicsContext gc;
    public File file;
    private Window currentWindow;

    public static void main(String[] args) {
        launch(args);
    }


    public void updateCanvas(Window window, PrioritySearchTree pst) {
        clearCanvas();
        this.currentWindow = window;
        double x = gc.getCanvas().getWidth() / 2;
        double y = gc.getCanvas().getHeight() / 2;
        for (PSTNode n : window.queryPST(pst)) {
            if (n != null && n.getLinkedTo() != null)
                gc.strokeLine(n.getX() + x, n.getY() + y, n.getLinkedTo().getX() + x, n.getLinkedTo().getY() + y);
        }
    }

    public void clearCanvas() {
        this.currentWindow = null;
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    @Override
    public void start(Stage stage) {
        // Declare some objects
        final FileChooser fileChooser = new FileChooser();

        // Create MenuBar
        MenuBar menuBar = new MenuBar();

        // Create Menus
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");

        // Create MenuItems
        MenuItem openFileItem = new MenuItem("Open File");
        MenuItem clearCanvasItem = new MenuItem("Clear canvas");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem editWindowItem = new MenuItem("Edit window");

        // Set actions to each MenuItems
        openFileItem.setOnAction(event -> {
            file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                updateCanvas(new Window(file), new PrioritySearchTree(file));
            }
        });
        clearCanvasItem.setOnAction(event -> clearCanvas());
        exitItem.setOnAction(event -> System.exit(0));
        editWindowItem.setOnAction(event -> {
            if (currentWindow != null) {
                WindowDialog windowDialog = new WindowDialog(currentWindow.searchWindow);
                Double[] size = windowDialog.showAndWait().get();
                updateCanvas(new Window(file, size), new PrioritySearchTree(file));
            }
        });

        // Add MenuItems to the Menus
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
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}