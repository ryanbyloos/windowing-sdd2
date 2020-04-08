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

/**
 * This is the graphical app itself, it extends Application from JavaFX.
 */
public class MainApplication extends Application {

    private final int width = 800;
    private final int height = 600;
    private GraphicsContext gc;
    private File file;
    private Window currentWindow;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method is used to draw all the segments of the windowed tree on the canvas.
     *
     * @param window The window used to choose the segments in the pst
     * @param pst    The PrioritySearchTree from which we draw the segments.
     */
    private void updateCanvas(Window window, PrioritySearchTree pst) {
        clearCanvas();
        this.currentWindow = window;

        Double[] w = currentWindow.searchWindow;
        double rx = width / (2 * Math.max(-w[0], w[1]));
        double ry = height / (2 * Math.max(-w[2], w[3]));
        double ratio = Math.min(rx, ry);

        for (PSTNode n : window.queryPST(pst)) {
            if (n != null && n.getLinkedTo() != null)
                strokeScaledLine(ratio, n.getX(), n.getY(), n.getLinkedTo().getX(), n.getLinkedTo().getY());
        }
    }

    /**
     * This method is used to clear the canvas area.
     */
    private void clearCanvas() {
        this.currentWindow = null;
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    /**
     * This method is a wrapper for the strokeLine() method from the GraphicsContext class.
     * It is used so the window is scaled to the canvas. It simulate a zoom in/out to make the window more perceptible.
     *
     * @param ratio the ratio to get the right scale.
     */
    private void strokeScaledLine(double ratio, double x, double y, double xp, double yp) {
        gc.strokeLine(ratio * x + (width >> 1),
                ratio * y + (height >> 1),
                ratio * xp + (width >> 1),
                ratio * yp + (height >> 1));
    }

    @Override
    public void start(Stage stage) {
        // Declare the fileChooser for later
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/main/java/scenes"));

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
                WindowDialog windowDialog = new WindowDialog(currentWindow);
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
        Scene scene = new Scene(root, width, height);

        Canvas canvas = new Canvas(scene.getWidth() - menuBar.getWidth(), scene.getHeight() - menuBar.getHeight());
        gc = canvas.getGraphicsContext2D();
        root.getChildren().addAll(menuBar, canvas);

        stage.setTitle("Windowing App");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}