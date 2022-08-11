package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EnterAPatient implements Initializable {

    @FXML
    private ChoiceBox<String> Department;

    @FXML
    private ChoiceBox<String> Patient;


    @FXML
    private RadioButton depRaButton;

    @FXML
    private Label departLabel;

    @FXML
    private RadioButton emerRaButton;

    @FXML
    private Label patLabel;
    private String secId;

    public void setSecid(String id) {
        secId = id;
    }

    @FXML
    void goBack(ActionEvent event) throws SQLException, IOException {
        Connection connection = Main.oracleDataSource.getConnection();
        PreparedStatement preparedStatement =
                connection.prepareStatement("select stuff_id ,name from secres where Stuff_id = ?");
        preparedStatement.setString(1, secId);
        ResultSet resultSet = preparedStatement.executeQuery();
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
        if(!(emerRaButton.isSelected()|| depRaButton.isSelected())){
            JOptionPane.showMessageDialog(null, "Please choose the type of entry");
            return;
        }

        Connection connection = Main.oracleDataSource.getConnection();
        if(emerRaButton.isSelected()){
            int max = 0;
            ResultSet resultSet = connection.createStatement().executeQuery("select id from emervis");
            while (resultSet.next()){
                int temp = Integer.parseInt(resultSet.getString("id"));
                if(temp>max){
                    max = temp+1;
                }
            }
            System.out.println(max);
            PreparedStatement preparedStatement =
                    connection.prepareStatement("insert into emervis values(?,?,?,?,?)");

            preparedStatement.setString(1, String.valueOf(++max));
            preparedStatement.setDate(2,new java.sql.Date(new java.util.Date().getTime()));
            preparedStatement.setInt(3, 1);
            preparedStatement.setNull(4, Types.DATE);
            preparedStatement.setString(5, patIdTemp);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Entered Successfully");
            initialize(null, null);
        }
        else if(depRaButton.isSelected()){
            int max = 0;
            ResultSet resultSet = connection.createStatement().executeQuery("select id from departvis");
            while (resultSet.next()){
                int temp = Integer.parseInt(resultSet.getString("id"));
                if(temp>max){
                    max = temp+1;
                }
            }
            PreparedStatement preparedStatement =
                    connection.prepareStatement("insert into departvis values(?,?,?,?,?,?)");

            preparedStatement.setString(1, String.valueOf(++max));
            preparedStatement.setDate(2,new java.sql.Date(new java.util.Date().getTime()));
            preparedStatement.setInt(3, 1);
            preparedStatement.setNull(4, Types.DATE);
            preparedStatement.setString(6, patIdTemp);
            preparedStatement.setString(5, depIdTemp);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Entered Successfully");
            initialize(null, null);
        }


    }

    private String patIdTemp;
    private String depIdTemp;



    public void getTempPatId(ActionEvent event) {
        try {



            this.patIdTemp = Patient.getValue();
            Connection connection = DriverManager.getConnection(Main.URL,Main.USER_NAME, Main.PASSWORD);
            PreparedStatement preparedStatement =
                    connection.prepareStatement("Select name from patients where pat_id ?");
            preparedStatement.setString(1, patIdTemp);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            patLabel.setText(resultSet.getString("name"));
            connection.close();


        } catch (Exception e) {

        }
    }
    public void getTempDepId(ActionEvent event ){
        try {


            this.depIdTemp = Department.getValue();
            Connection connection = DriverManager.getConnection(Main.URL,Main.USER_NAME, Main.PASSWORD);
            PreparedStatement preparedStatement =
                    connection.prepareStatement("Select nameofdep from departments where depart_id = ?");
            preparedStatement.setString(1, depIdTemp);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            departLabel.setText(resultSet.getString("nameofdep"));
            connection.close();


        } catch (Exception e) {

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = null;

            connection = Main.oracleDataSource.getConnection();
            Patient.setOnAction(this::getTempPatId);
            ResultSet resultSet = connection.createStatement().executeQuery("select pat_id from patients");
            ArrayList<String> patsArray = new ArrayList<String>();
           While1: while (resultSet.next()) {
                String patIdd = resultSet.getString("pat_id");
                PreparedStatement preparedStatement =
                        connection.prepareStatement("select * from departvis where patid =?");
                preparedStatement.setString(1, patIdd);
                ResultSet resultSet1 = preparedStatement.executeQuery();
                while (resultSet1.next()){
                    if (resultSet1.getInt("AVA") == 1) continue While1;
                }
                PreparedStatement preparedStatement1 =
                        connection.prepareStatement("select * from emervis where patid =?");
                preparedStatement1.setString(1, patIdd);
                ResultSet resultSet2 = preparedStatement1.executeQuery();
               while (resultSet2.next()){
                   if (resultSet2.getInt("AVA") == 1) continue While1;
               }
                patsArray.add(patIdd);

            }
            Patient.getItems().clear();
            Patient.getItems().addAll(patsArray);

            ResultSet resultSet1 = connection.createStatement().executeQuery("select depart_id from departments");
            ArrayList<String> depsArray = new ArrayList<String>();
            while (resultSet1.next()) {
                depsArray.add(resultSet1.getString("depart_id"));

            }
            Department.getItems().clear();
            Department.getItems().addAll(depsArray);
            Department.setOnAction(this::getTempDepId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
