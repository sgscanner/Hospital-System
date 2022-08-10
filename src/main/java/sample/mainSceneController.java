package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class mainSceneController {

    @FXML
    Button secLogButton;
    @FXML
    public void goToSecLogIn(ActionEvent event) throws IOException {
       Stage stage = ((Stage) ( ( (Node)(event.getSource())).getScene().getWindow()));
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/secreEnter.fxml"))));
        stage.show();

    }

    @FXML
    public void goToPatLogIn(ActionEvent event) throws IOException {
       Stage stage =  ((Stage) ( ( (Node)(event.getSource())).getScene().getWindow()));
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/PatientEnter.fxml"))));
        stage.show();
    }
    @FXML
    public void goToManagerLogIn(ActionEvent event) throws IOException {


        Stage stage =  ((Stage) ( ( (Node)(event.getSource())).getScene().getWindow()));
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/managerLogIN.fxml"))));
        stage.show();
    }
    @FXML
    public void goToDoctorLogIn(ActionEvent event) throws IOException {


        Stage stage =  ((Stage) ( ( (Node)(event.getSource())).getScene().getWindow()));
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/doctorLogIn.fxml"))));
        stage.show();
    }
    public void exitTheSystem(){

        System.exit(0);
    }

}
