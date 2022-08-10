package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;

public class MakeASuccessorForManager {

    @FXML
    private TextField addField;

    @FXML
    private Button button1;

    @FXML
    private TextField fEmail;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField newPassConfirmField;

    @FXML
    private PasswordField newPassField;

    @FXML
    private PasswordField oldPassField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField sEmail;
    @FXML
    void goBack(ActionEvent event) throws SQLException, IOException {
        ResultSet resultSet = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD).createStatement()
                .executeQuery("Select * from managers where currently = 1");
        resultSet.next();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MainViewForManager.fxml"));
        Parent root = loader.load();
        MainViewForManager mainViewForManager = loader.getController();
        mainViewForManager.setNameAndId(resultSet.getString("manager_name"), resultSet.getString("manager_id"));
        ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
    }

    @FXML
    void submitManager(ActionEvent event) throws SQLException, IOException {
        String oldPass =oldPassField.getText().trim();
        if(oldPass.isEmpty()){
            JOptionPane.showMessageDialog(null, "Please enter the past password Correctly");
            return;
        }
        Connection connection = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);
        ResultSet resultSet = connection.createStatement()
                .executeQuery("Select Pass from managers where currently = 1");
        resultSet.next();
        if(!resultSet.getString("pass").equals(oldPass)){
            JOptionPane.showMessageDialog(null, "Please enter the past password Correctly");
            connection.close();
            return;
        }

        String id = idField.getText().trim();
        if(id.isEmpty()){
            JOptionPane.showMessageDialog(null, "Please enter an id");
            connection.close();
            return;
        }
        ResultSet resultSet1 = connection.createStatement()
                .executeQuery("Select manager_id from managers");
        while(resultSet1.next()){
            if(resultSet1.getString("manager_id").equals(id)){
                JOptionPane.showMessageDialog(null, "Please Don't use a past id");
                connection.close();
                return;
            }
        }
        if(newPassField.getText().trim().isEmpty() || newPassConfirmField.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Please enter a password");
            connection.close();
            return;
        }
        String passWord = newPassField.getText();
        String confirmes = newPassConfirmField.getText();
        if(!passWord.equals(confirmes)){
            JOptionPane.showMessageDialog(null, "Passwords must match");
            connection.close();
            return;
        }
        connection.setAutoCommit(false);

        PreparedStatement insertStatement = connection.prepareStatement("insert into managers (MANAGER_ID, MANAGER_NAME, " +
                "MANAGER_FMAIL, MANAGER_SECMAIL, CURRENTLY, PASS, STARTDATE, ENDDATE) values (?, ?, ?, ?, ?, ?, ?, ? )");
        insertStatement.setString(1, id);
        insertStatement.setString(2, nameField.getText().trim());
        insertStatement.setString(3, fEmail.getText().trim());
        insertStatement.setString(4, sEmail.getText().trim());
        insertStatement.setInt(5, 1);
        insertStatement.setString(6, passWord);
        java.sql.Date currDate = new java.sql.Date(new java.util.Date().getTime());
        insertStatement.setDate(7, currDate);
        insertStatement.setNull(8, Types.DATE);

        PreparedStatement updateStatement = connection.prepareStatement("update managers set currently = 0 , enddate = ? "
                +" where currently = 1");
        updateStatement.setDate(1, currDate);
        updateStatement.executeUpdate();
        insertStatement.executeUpdate();



        connection.commit();
        JOptionPane.showMessageDialog(null, "Succesfully, the new manager registered");
        connection.close();
        ((Stage) (((Node) (event.getSource())).getScene().getWindow()))
                .setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/mainView.fxml"))));


    }

}