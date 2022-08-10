package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ModifyDepart implements Initializable {

    @FXML
    private ChoiceBox<String> IdBox;

    @FXML
    private TextField capTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    private String selectedId;
    @FXML
    void submit(ActionEvent event) throws SQLException {
        Connection connection = Main.oracleDataSource.getConnection();
        connection.createStatement().executeUpdate("update departments set nameofdep ='"+
                nameTextField.getText().trim()+"', Phone = '"+phoneTextField.getText().trim()+"', capacity = "+
                Integer.parseInt(capTextField.getText().trim())+"where depart_id = '"+selectedId+"'");
        connection.close();
        JOptionPane.showMessageDialog(null, "Updated successfully");

    }

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        try {
          connection  = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet resultSet = null;
        try {
            resultSet = connection.createStatement().executeQuery("Select depart_id from departments ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<String> ids = new ArrayList<String>();
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                ids.add(resultSet.getString("depart_id"));
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        //String [] idsArray = (String[]) ids.toArray();
        IdBox.getItems().removeAll();
        IdBox.getItems().addAll(ids);
        IdBox.setOnAction(this::comboAction);
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void comboAction(ActionEvent event) {
        try {


            selectedId = IdBox.getValue();
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ResultSet resultSet = connection.createStatement().executeQuery("Select * from departments where depart_id = '" +
                    selectedId + "'");
            resultSet.next();
            capTextField.setText(String.valueOf(resultSet.getInt("Capacity")));
            nameTextField.setText(resultSet.getString("Nameofdep"));
            phoneTextField.setText(resultSet.getString("phone"));
            connection.close();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
