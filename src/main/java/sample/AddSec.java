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
import java.sql.*;

public class AddSec {

    @FXML
    private TextField addressField;

    @FXML
    private PasswordField confPassField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField ssnField;

    @FXML
    void goBack(ActionEvent event) throws SQLException, IOException {
        ResultSet resultSet = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD).createStatement()
                .executeQuery("Select * from managers where currently = 1");
        resultSet.next();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MainViewForManager.fxml"));
        Parent root = loader.load();
        MainViewForManager mainViewForManager = loader.getController();
        mainViewForManager.setNameAndId(resultSet.getString("manager_name"),
                resultSet.getString("manager_id"));
        ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
    }

    @FXML
    void submit(ActionEvent event) throws SQLException {
        String id = idField.getText().trim();
        if(id.isEmpty()){
            JOptionPane.showMessageDialog(null, "Please enter an id");
            return;
        }
        String passWord = passField.getText();
        String confirmes = confPassField.getText();
        if(passWord.isEmpty()|| confirmes.isEmpty()){
            JOptionPane.showMessageDialog(null, "Please enter a pssword");

            return;
        }
        if(!passWord.equals(confirmes)){
            JOptionPane.showMessageDialog(null, "Passwords must match");

            return;
        }
        Connection connection = Main.oracleDataSource.getConnection();
        ResultSet resultSet1 = connection.createStatement()
                .executeQuery("Select stuff_id from secres");
        while(resultSet1.next()){
            if(resultSet1.getString("stuff_id").equals(id)){
                JOptionPane.showMessageDialog(null, "Please Don't use a past id");
                connection.close();
                return;
            }
        }
        PreparedStatement insertStatement = connection.prepareStatement("insert into secres (stuff_ID, NAME, " +
                "EMAIL,  PASSword, phone, address , ssn) values (?, ?, ?, ?, ?, ?, ?)");
        insertStatement.setString(1, id);
        insertStatement.setString(2, nameField.getText().trim());
        insertStatement.setString(3, emailField.getText().trim());
        insertStatement.setString(4, passWord);
        insertStatement.setString(5, phoneField.getText().trim());
        insertStatement.setString(6, addressField.getText().trim());
        insertStatement.setString(7, ssnField.getText().trim());


        insertStatement.executeUpdate();
        connection.close();
        JOptionPane.showMessageDialog(null, "Added Successfully");





    }

}
