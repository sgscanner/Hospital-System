package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
public class AddDepart {
    @FXML
    private TextField depIDField;
    @FXML
    private TextField depPhoField;
    @FXML
    private TextField depCapField;
    @FXML
    private TextField depNameField;
    @FXML
    private void submitDepartment() throws SQLException {
        String tempID = depIDField.getText().trim();
        if(tempID.length()>6) {
            JOptionPane.showMessageDialog(null, "The Id mustn't exceed 6 digits");

            return;

        }
        if(tempID.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The Id must be determined");

            return;

        }
        Connection connection = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);
        ResultSet resultSet = connection.createStatement().executeQuery("Select depart_id from departments");
        while (resultSet.next()){
            if(tempID.equals(resultSet.getString("depart_id"))){
                JOptionPane.showMessageDialog(null, "The Id mustn't match any previous id's");
                connection.close();
                return;
            }
        }
        connection.createStatement()
                .executeUpdate("insert into departments values ('"+tempID+"', "+Integer.parseInt(depCapField.getText().trim())
                +",'" + depNameField.getText().trim()+"', '" +depPhoField.getText().trim()+"')");
        connection.close();
        JOptionPane.showMessageDialog(null, "Department added successfully");



    }
    @FXML
    public void goBack(ActionEvent event) throws SQLException, IOException {
        ResultSet resultSet = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD).createStatement()
                .executeQuery("Select * from managers where currently = 1");
        resultSet.next();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MainViewForManager.fxml"));
        Parent root = loader.load();
        MainViewForManager mainViewForManager = loader.getController();
        mainViewForManager.setNameAndId(resultSet.getString("manager_name"), resultSet.getString("manager_id"));
        ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
    }
}
