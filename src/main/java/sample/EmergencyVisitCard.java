package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EmergencyVisitCard {
    @FXML
    private Label startLab;
    @FXML
    private Label endLab;
    public void setData(String start, String end){
        startLab.setText(start);
        endLab.setText(end);

    }
}
