package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class MedCard {
@FXML
    private Label nameAndDo;
    @FXML
    private Label docLab;
    @FXML
    private Label datLab;
    @FXML
    private Label amouLab;


    public void setAll(String nd, String doc, String dat, String amou){
        this.nameAndDo.setText(nd);
        this.docLab.setText(doc);
        this.datLab.setText(dat);
        this.amouLab.setText(amou);

    }
}
