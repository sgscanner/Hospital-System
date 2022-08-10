package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.JOptionPane;
import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

public class ManagerLogIN implements Initializable {
    @FXML
    private TextField manIDField;
    @FXML
    private PasswordField manPassField;
    @FXML
    private PasswordField forgPassPass1;
    @FXML
    private PasswordField forgPassPass2;

    @FXML
    private Button verifyButton;

    @FXML
    private int firstVerNum;

    @FXML
    private int secVerNum;
    @FXML
    public void enableForgPass() throws SQLException, MessagingException {
        ResultSet resultSet = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD).createStatement()
                .executeQuery("Select * from managers where currently = 1");
        resultSet.next();
        Random random = new Random();
        firstVerNum = random.nextInt(999999);
        secVerNum = random.nextInt(999999);
        System.out.println(firstVerNum);
        System.out.println(secVerNum);
        SendEmail.sendemail(resultSet.getString("MANAGER_FMAIL"), firstVerNum);
        SendEmail.sendemail(resultSet.getString("MANAGER_SECMAIL"), secVerNum);
        verifyButton.setDisable(false);
        forgPassPass1.setDisable(false);
        forgPassPass2.setDisable(false);


    }

    @FXML
    public void managerLogIn(ActionEvent event) throws SQLException, IOException {
        ResultSet resultSet0 = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD).createStatement()
                .executeQuery("Select * from managers where currently = 1");
        resultSet0.next();
        if(resultSet0.getString("manager_id").equals(manIDField.getText().trim()) && resultSet0.getString("pass").equals(manPassField.getText().trim())){
            ResultSet resultSet = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD).createStatement()
                    .executeQuery("Select * from managers where currently = 1");
            resultSet.next();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MainViewForManager.fxml"));
            Parent root = loader.load();
            MainViewForManager mainViewForManager = loader.getController();
            mainViewForManager.setNameAndId(resultSet.getString("manager_name"), resultSet.getString("manager_id"));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
        }
        else JOptionPane.showMessageDialog(null, "The Id/pass is invalid");
    }
    @FXML
    public void checkThePass(ActionEvent event) throws SQLException, IOException {
        if((Integer.parseInt(forgPassPass1.getText().trim()) == firstVerNum) && (Integer.parseInt(forgPassPass2.getText().trim()) == secVerNum)){
            ResultSet resultSet = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD).createStatement()
                    .executeQuery("Select * from managers where currently = 1");
            resultSet.next();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MainViewForManager.fxml"));
            Parent root = loader.load();
            MainViewForManager mainViewForManager = loader.getController();
            mainViewForManager.setNameAndId(resultSet.getString("manager_name"), resultSet.getString("manager_id"));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));


        }else JOptionPane.showMessageDialog(null, "One of the passwords is wrong");




    }
    public void returnToMainView(ActionEvent event) throws IOException {
        ((Stage) (((Node) (event.getSource())).getScene().getWindow()))
                .setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/mainView.fxml"))));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        verifyButton.setDisable(true);
        forgPassPass1.setDisable(true);
        forgPassPass2.setDisable(true);
    }
}
