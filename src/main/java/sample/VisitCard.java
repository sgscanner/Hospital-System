package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class VisitCard {
    @FXML
    private Label deparLab;
    @FXML
    private Label startLab;
    @FXML
    private Label endLab;

    public void setAll(String depar, String start, String end) {
        this.deparLab.setText(depar);
        this.startLab.setText(start);
        this.endLab.setText(end);

    }
}
