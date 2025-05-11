package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
    @FXML
    private GridPane grid;

    private String currentPlayer = "X";
    private Button[][] cells = new Button[3][3];

    @FXML
    public void initialize() {
        createBoard();
    }

    private void createBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button cell = new Button();
                cell.setPrefSize(100, 100);
                cell.setStyle("-fx-font-size: 36px; -fx-focus-color: transparent;");
                final int r = row;
                final int c = col;
                cell.setOnAction(e -> handleMove(r, c));
                cells[row][col] = cell;
                grid.add(cell, col, row);
            }
        }
    }

    private void handleMove(int row, int col) {
        Button clicked = cells[row][col];
        if (clicked.getText().isEmpty()) {
            clicked.setText(currentPlayer);
            // Add logic to check for win here
            currentPlayer = currentPlayer.equals("X") ? "O" : "X";
        }
    }
}