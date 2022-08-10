package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.Period;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddDocOrNurse implements Initializable {
    @FXML
    private Label nameTextField;

    private String secId;
    @FXML
    private ChoiceBox<String> IdBox;
    @FXML
    private ToggleGroup Type;

    @FXML
    private TextField addressField;

    @FXML
    private CheckBox cB1;

    @FXML
    private CheckBox cB2;

    @FXML
    private CheckBox cB3;

    @FXML
    private CheckBox cB4;

    @FXML
    private CheckBox cB5;

    @FXML
    private CheckBox cB6;

    @FXML
    private CheckBox cB7;

    @FXML
    private PasswordField confPassField;

    @FXML
    private RadioButton docRaBut;

    @FXML
    private TextField emailField;

    @FXML
    private TextField fromF1;

    @FXML
    private TextField fromF2;

    @FXML
    private TextField fromF3;

    @FXML
    private TextField fromF4;

    @FXML
    private TextField fromF5;

    @FXML
    private TextField fromF6;

    @FXML
    private TextField fromF7;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private RadioButton nurRaBut;

    @FXML
    private PasswordField passField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField ssnField;

    @FXML
    private TextField toF1;

    @FXML
    private TextField toF2;

    @FXML
    private TextField toF3;

    @FXML
    private TextField toF4;

    @FXML
    private TextField toF5;

    @FXML
    private TextField toF6;

    @FXML
    private TextField toF7;

    public void setSecId(String s){
        this.secId = s;
    }

    @FXML
    void goBack(ActionEvent event) throws SQLException, IOException {
        Connection connection = Main.oracleDataSource.getConnection();
        ResultSet resultSet = connection.createStatement()
                .executeQuery("select stuff_id ,name from secres where Stuff_id = '"+secId+"'");
        resultSet.next();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MainViewForSec.fxml"));
        Stage stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
        stage.setScene(new Scene(loader.load()));
        MainViewForSec mainViewForSec = loader.getController();
        mainViewForSec.setInitial(secId, resultSet.getString("name"));
        stage.show();
        connection.close();
    }

    @FXML
    void submit(ActionEvent event) throws SQLException {
        TextField [] fromArray = {fromF1, fromF2, fromF3, fromF4, fromF5, fromF6, fromF7};
        TextField [] toArray = {toF1, toF2, toF3, toF4, toF5, toF6, toF7};
        CheckBox [] daysArray = {cB1, cB2, cB3, cB4, cB5 , cB6, cB7};
        if(!docRaBut.isSelected() && !nurRaBut.isSelected()){
            JOptionPane.showMessageDialog(null, "Please select the type ");
            return;
        }
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
        connection.setAutoCommit(false);
        if(docRaBut.isSelected()){
            ResultSet resultSet1 = connection.createStatement()
                    .executeQuery("Select stuff_id from doctors");
            while(resultSet1.next()){
                if(resultSet1.getString("stuff_id").equals(id)){
                    JOptionPane.showMessageDialog(null, "Please Don't use a past id");
                    connection.close();
                    return;
                }
            }

            PreparedStatement wholeInsert = connection.prepareStatement("insert into doctors (" +
                    "stuff_id, SSN, NAME, PHONE, ADDRESS, EMAIL, PASSWORD, DEPIDFORDOC) values(?, ?, ?, ?, ?, ?, ?, ?)");
            wholeInsert.setString(1, id);
            wholeInsert.setString(2, ssnField.getText().trim());
            wholeInsert.setString(3, nameTextField.getText().trim());
            wholeInsert.setString(4, phoneField.getText().trim());
            wholeInsert.setString(5, addressField.getText().trim());
            wholeInsert.setString(6, emailField.getText().trim());
            wholeInsert.setString(7, passWord);
            wholeInsert.setString(8, selectedId);
            wholeInsert.executeUpdate();
            connection.commit();

            int max = 0;
            ResultSet resultSet = connection.createStatement().executeQuery("select dawamid from dawamdac");
            while (resultSet.next()){
                int temp = Integer.parseInt(resultSet.getString("dawamid").trim());
                if(temp>max){
                    max = temp + 1;
                }
            }


            PreparedStatement preparedStatement = connection.prepareStatement("insert into dawamdac values (?, ?, ?, ?, ?)");
            for (int i = 0; i<7; i++) {
                if(daysArray[i].isSelected()) {
                    preparedStatement.setString(1, String.valueOf(++max));
                    preparedStatement.setInt(2, Integer.parseInt(fromArray[i].getText().trim()));
                    preparedStatement.setInt(3, Integer.parseInt(toArray[i].getText().trim()));
                    preparedStatement.setString(4, String.valueOf(i+1));
                    preparedStatement.setString(5, id);
                    preparedStatement.executeUpdate();
                }

            }
            JOptionPane.showMessageDialog(null, "Doctor added successfully");
            connection.close();
            return;



        }else if(nurRaBut.isSelected()){
            ResultSet resultSet1 = connection.createStatement()
                    .executeQuery("Select stuff_id from nurses");
            while(resultSet1.next()){
                if(resultSet1.getString("stuff_id").equals(id)){
                    JOptionPane.showMessageDialog(null, "Please Don't use a past id");
                    connection.commit();
                    connection.close();
                    return;
                }
            }

            PreparedStatement wholeInsert = connection.prepareStatement("insert into nurses (" +
                    "stuff_id, SSN, NAME, PHONE, ADDRESS, EMAIL, PASSWORD, DEPIDFORNUR) values(?, ?, ?, ?, ?, ?, ?, ?)");
            wholeInsert.setString(1, id);
            wholeInsert.setString(2, ssnField.getText().trim());
            wholeInsert.setString(3, nameTextField.getText().trim());
            wholeInsert.setString(4, phoneField.getText().trim());
            wholeInsert.setString(5, addressField.getText().trim());
            wholeInsert.setString(6, emailField.getText().trim());
            wholeInsert.setString(7, passWord);
            wholeInsert.setString(8, selectedId);
            wholeInsert.executeUpdate();
            connection.commit();

            int max = 0;
            ResultSet resultSet = connection.createStatement().executeQuery("select id from nurdawam");
            while (resultSet.next()){
                int temp = Integer.parseInt(resultSet.getString("id").trim());
                if(temp>max){
                    max = temp + 1;
                }
            }


            PreparedStatement preparedStatement = connection.prepareStatement("insert into nurdawam values (?, ?, ?, ?, ?)");
            for (int i = 0; i<7; i++) {
                if(daysArray[i].isSelected()) {
                    preparedStatement.setString(1, String.valueOf(++max));
                    preparedStatement.setInt(2, Integer.parseInt(fromArray[i].getText().trim()));
                    preparedStatement.setInt(3, Integer.parseInt(toArray[i].getText().trim()));
                    preparedStatement.setString(4, String.valueOf(i));
                    preparedStatement.setString(5, id);
                    preparedStatement.executeUpdate();
                }

            }
            JOptionPane.showMessageDialog(null, "Nurse added successfully");
            connection.commit();
            connection.close();
            return;



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

            nameTextField.setText(resultSet.getString("Nameofdep"));

            connection.close();


        }catch (Exception e){
            e.printStackTrace();
        }
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
    private String selectedId;
}
