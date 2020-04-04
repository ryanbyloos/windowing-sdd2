package graphics;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class WindowDialog extends Dialog<Double[]> {
    public WindowDialog(Double[] searchWindow) {
        setHeaderText("Edit window");

        TextField X = new TextField();
        TextField Xp = new TextField();
        TextField Y = new TextField();
        TextField Yp = new TextField();

        applyNumericFilter(X, Xp, Y, Yp);

        GridPane gridPane = new GridPane();
        gridPane.addColumn(0, new Label("X"), X, new Label("X'"), Xp);
        gridPane.addColumn(1, new Label("Y"), Y, new Label("Y'"), Yp);

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
            String text = textFields[i].getText();
            if (!text.isEmpty())
                results[i] = Double.parseDouble(text);
        }
    }
}
