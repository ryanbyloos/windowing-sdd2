package graphics;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import structure.Window;

public class WindowDialog extends Dialog<Double[]> {

    Double[] maxSize;

    public WindowDialog(Window window) {
        setHeaderText("Edit window");

        Double[] searchWindow = window.searchWindow;
        maxSize = window.windowSize;

        TextField X = new TextField(Double.toString(searchWindow[0]));
        TextField Xp = new TextField(Double.toString(searchWindow[1]));
        TextField Y = new TextField(Double.toString(searchWindow[2]));
        TextField Yp = new TextField(Double.toString(searchWindow[3]));

        CheckBox infX = new CheckBox("−∞");
        CheckBox infXp = new CheckBox("+∞");
        CheckBox infY = new CheckBox("−∞");
        CheckBox infYp = new CheckBox("+∞");

        infX.setOnAction(e -> {
            if (infX.isSelected())
                X.setDisable(true);
            else
                X.setDisable(false);
        });
        infXp.setOnAction(e -> {
            if (infX.isSelected())
                Xp.setDisable(true);
            else
                Xp.setDisable(false);
        });
        infY.setOnAction(e -> {
            if (infY.isSelected())
                Y.setDisable(true);
            else
                Y.setDisable(false);
        });
        infYp.setOnAction(e -> {
            if (infYp.isSelected())
                Yp.setDisable(true);
            else
                Yp.setDisable(false);
        });

        applyNumericFilter(X, Xp, Y, Yp);

        GridPane gridPane = new GridPane();
        gridPane.addColumn(0, new Label("X"), X, infX, new Label("X'"), Xp, infXp);
        gridPane.addColumn(1, new Label("Y"), Y, infY, new Label("Y'"), Yp, infYp);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setContent(gridPane);

        setResultConverter(callback -> {
            if (callback.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                TextField[] textFields = {X, Xp, Y, Yp};
                getDoubleFromTextField(textFields, searchWindow);
            }
            return searchWindow;
        });
    }

    public void applyNumericFilter(TextField... textFields) {
        for (TextField textField : textFields) {
            textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if (!newValue.matches("-?\\d*([.]\\d*)?")) {
                    textField.setText(oldValue);
                }
            });
        }
    }

    public void getDoubleFromTextField(TextField[] textFields, Double[] results) {
        for (int i = 0; i < textFields.length; i++) {
            if (textFields[i].isDisabled()) {
                results[i] = maxSize[i];
            } else {
                String text = textFields[i].getText();
                if (!text.isEmpty())
                    results[i] = Double.parseDouble(text);
            }
        }
    }
}
