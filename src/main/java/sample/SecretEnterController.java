package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SecretEnterController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField pass;

    public void returnToMainView(ActionEvent event) throws IOException {
        ((Stage) (((Node) (event.getSource())).getScene().getWindow())).setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/mainView.fxml"))));

    }

    public void forgotpass(ActionEvent event) throws IOException {
        Stage stage = ((Stage) (((Node) (event.getSource())).getScene().getWindow()));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/ForgetPassword.fxml"));
        Parent root = loader.load();
        ForgetPassword forgetPassword = loader.getController();
        forgetPassword.whereDidICameFrom = 0;
        forgetPassword.docRaBut.setVisible(false);
        forgetPassword.nurRaBut.setVisible(false);
        stage.setScene(new Scene(root));

    }

    @FXML
    public void signInForSec(ActionEvent event) {
        Connection con = null;
        String stuffID = username.getText();
        String password = pass.getText();
        if (stuffID.trim().isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You must  fill the fields");
            return;
        }
        try {
            con = Main.oracleDataSource.getConnection();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select stuff_id, password ,name from secres");
            while (resultSet.next()) {
                String secId = resultSet.getString("stuff_id");
                if (secId.equals(stuffID.trim())) {
                    if (password.trim().equals(resultSet.getString("password"))) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MainViewForSec.fxml"));
                        Stage stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
                        stage.setScene(new Scene(loader.load()));
                        MainViewForSec mainViewForSec = loader.getController();
                        mainViewForSec.setInitial(secId, resultSet.getString("name"));
                        stage.show();
                        con.close();
                        return;
                    } else {
                        JOptionPane.showMessageDialog(null, "The ID/PassWord is invalid");
                        con.close();
                        return;
                    }

                }
            }
            JOptionPane.showMessageDialog(null, "The Id/PassWord is invalid");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
