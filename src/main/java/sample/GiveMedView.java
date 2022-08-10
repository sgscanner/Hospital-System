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
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GiveMedView  implements Initializable {
    private String docId;
    public void setDocId(String id){
        docId = id;
    }
    @FXML
    private TextField daysField;

    @FXML
    private TextField dozenField;

    @FXML
    private TextField medecineField;

    @FXML
    private ChoiceBox<String> medecineSelect;

    @FXML
    private TextField patField;

    @FXML
    private ChoiceBox<String> patSelect;

    @FXML
    private TextField perDayField;

    @FXML
    void goBack(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MainViewForDoctor.fxml"));
        Stage stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
        stage.setScene(new Scene(loader.load()));
        Connection connection = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);
        ResultSet resultSet = connection.createStatement().executeQuery("select depidfordoc, name from doctors where stuff_id ='"+
                docId+"'");
        resultSet.next();
        MainViewForDoctor mainViewForDoctor = loader.getController();
        String depId = resultSet.getString("depidfordoc");
        ResultSet rs = connection.createStatement().executeQuery("select nameofdep from departments where depart_id = '"+
                depId+"'");
        rs.next();
        mainViewForDoctor.showAll(docId, resultSet.getString("name"),
                depId, rs.getString("nameofdep"));


    }

    @FXML
    void goToAddNewMed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/newMedView.fxml"));
        Parent parent = loader.load();
        NewMedView giveMedView = loader.getController();
        giveMedView.setDocId(docId);
        ((Stage)(((Node)(event.getSource())).getScene().getWindow())).setScene(new Scene(parent));

    }
    private String tempMedId;
    private String tempPatId;

    @FXML
    void submit(ActionEvent event) throws SQLException {
        Connection connection = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);

        PreparedStatement preparedStatement = connection.prepareStatement("insert into treats values(?, ?, ?, ?, ?, ?, ?, ?)");
        int max= 0;
        ResultSet resultSet = connection.createStatement().executeQuery("select tid from treats ");
        while (resultSet.next()){
            int temp = Integer.parseInt(resultSet.getString("tid").trim());
            if(temp> max) max = temp+1;
        }
        preparedStatement.setString(1,String.valueOf(++max));
        preparedStatement.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
        preparedStatement.setInt(3, Integer.parseInt(perDayField.getText().trim()));
        preparedStatement.setInt(4,Integer.parseInt(daysField.getText().trim()));
        preparedStatement.setInt(5, Integer.parseInt(dozenField.getText().trim()));
        preparedStatement.setString(6, tempPatId);
        preparedStatement.setString(7, docId);
        preparedStatement.setString(8, tempMedId);
        preparedStatement.executeUpdate();

        connection.close();
        JOptionPane.showMessageDialog(null, "Medecine given successfully");
        initialize(null, null);



    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            patSelect.getItems().clear();
            medecineSelect.getItems().clear();
            Connection connection = Main.oracleDataSource.getConnection();
            ArrayList<String> arrayList = new ArrayList<>();
            ResultSet resultSet = connection.createStatement().executeQuery("select pat_id from patients ");
            while (resultSet.next()){
                arrayList.add(resultSet.getString("pat_id"));
            }
            patSelect.getItems().addAll(arrayList);
            arrayList.clear();
            ResultSet resultSet1 = connection.createStatement().executeQuery("select medid from medecines ");
            while (resultSet1.next()){
                arrayList.add(resultSet1.getString("medid"));
            }
            medecineSelect.getItems().addAll(arrayList);
            connection.close();
            patSelect.setOnAction(this::patComboAction);
            medecineSelect.setOnAction(this::medComboAction);

        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
    public void patComboAction(ActionEvent event){
        try {
            tempPatId = patSelect.getValue();
            Connection connection = Main.oracleDataSource.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("select name from patients where pat_id ='"+
                    tempPatId+"'");
            resultSet.next();
            patField.setText(resultSet.getString("name"));
            connection.close();

        }catch (Exception e){

        }

    }
    public void medComboAction(ActionEvent event){
        try {
            tempMedId = medecineSelect.getValue();
            Connection connection = Main.oracleDataSource.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("select medname from medecines where medid ='"+
                    tempMedId+"'");
            resultSet.next();
            medecineField.setText(resultSet.getString("medname"));
            connection.close();
        }catch (Exception e){

        }

    }
}
