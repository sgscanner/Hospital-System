package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


public class MainViewForSec {
    @FXML
    private Label nameLabel;
    @FXML
    private Label idLabel;
    private String secId;
    private String secName;

    public void setInitial(String id, String name) {
        this.secId = id;
        this.secName = name;
        this.idLabel.setText("ID: " + id);
        this.nameLabel.setText("Name: " + secName);
    }

    @FXML
    public void logOut(ActionEvent event) {
        try {
            ((Stage) (((Node) event.getSource()).getScene().getWindow()))
                    .setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/mainView.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToExitPat(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/ExitAptient.fxml"));
        Parent root = loader.load();
        ExitAptient enterAPatient = loader.getController();
        enterAPatient.setSecid(this.secId);
        ((Stage) ((Node) (event.getSource())).getScene().getWindow()).setScene(new Scene(root));

    }

    @FXML
    public void goToEnterPat(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/enterAPatient.fxml"));
        Parent root = loader.load();
        EnterAPatient enterAPatient = loader.getController();
        enterAPatient.setSecid(this.secId);
        ((Stage) ((Node) (event.getSource())).getScene().getWindow()).setScene(new Scene(root));

    }

    @FXML
    public void goToAddDoc(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/addDocOrNurse.fxml"));
        Parent root = loader.load();
        ((Stage) ((Node) (event.getSource())).getScene().getWindow()).setScene(new Scene(root));
        AddDocOrNurse addDocOrNurse = loader.getController();
        addDocOrNurse.setSecId(secId);

    }

    @FXML
    public void goToAddPat(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/addpatient.fxml"));
        Parent root = loader.load();
        ((Stage) ((Node) (event.getSource())).getScene().getWindow()).setScene(new Scene(root));
        AddPatient addDocOrNurse = loader.getController();
        addDocOrNurse.setSecId(secId);

    }

    @FXML
    void goToAllDocs(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/wholeDocs.fxml"));
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(new Scene(loader.load()));
        WholeDocs wholeDocs = loader.getController();
        wholeDocs.setSecIdI(secId);

    }

    @FXML
    void goToAllNurs(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/wholeNurses.fxml"));
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(new Scene(loader.load()));
        WholeNurses wholeDocs = loader.getController();
        wholeDocs.setSecIdI(secId);

    }

    @FXML
    void goToAllPats(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/wholepats.fxml"));
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(new Scene(loader.load()));
        WholePats wholeDocs = loader.getController();
        wholeDocs.setSecIdI(secId);

    }

}
