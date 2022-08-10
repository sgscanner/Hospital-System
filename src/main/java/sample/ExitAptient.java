package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.log4j.DailyRollingFileAppender;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ExitAptient implements Initializable {

    @FXML
    private ChoiceBox<String> patChoice;

    @FXML
    private Label patLabel;
    private String secId;

    public void setSecid(String id) {
        secId = id;
    }

    @FXML
    void exitThePat(ActionEvent event) throws SQLException {
        boolean fromWhere = false;
        if(depVisIds.contains(tempId)) fromWhere = true;
        Connection connection = Main.oracleDataSource.getConnection();
        if(!fromWhere){
            PreparedStatement preparedStatement = connection.prepareStatement("update emervis set ava = 0, endtime = ? where patid ='"+
                    tempId+"'");
            preparedStatement.setDate(1,new java.sql.Date(new java.util.Date().getTime()));
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Exited successfully from Emergency ");
            initialize(null, null );
            return;
        } else if(fromWhere){
            PreparedStatement preparedStatement = connection.prepareStatement("update departvis set ava = 0, endtime = ? where patid ='"+
                    tempId+"'");
            preparedStatement.setDate(1,new java.sql.Date(new java.util.Date().getTime()));
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Exited successfully from department ");
            initialize(null, null );
            return;
        }


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
    ArrayList<String> emerVisIds = new ArrayList<>();

    ArrayList<String> depVisIds = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        patChoice.getItems().clear();
        emerVisIds.clear();
        depVisIds.clear();
        try{
            Connection connection = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);
            ResultSet resultSet = connection.createStatement().executeQuery("select patid from emervis where ava =1");
            while (resultSet.next()){
                emerVisIds.add(resultSet.getString("patid"));
            }
            ResultSet resultSet1 = connection.createStatement().executeQuery("select patid from departvis where ava =1");
            while (resultSet1.next()){
                depVisIds.add(resultSet1.getString("patid"));
            }
            connection.close();

            patChoice.getItems().addAll(depVisIds);
            patChoice.getItems().addAll( emerVisIds);
            patChoice.setOnAction(this::comboAction);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    String tempId;
    public void comboAction(ActionEvent event)  {
        tempId = patChoice.getValue();
        try {


            Connection connection = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);
            ResultSet resultSet = connection.createStatement().executeQuery("Select name from patients where pat_id = '" +
                    tempId + "'");
            resultSet.next();
            patLabel.setText(resultSet.getString("name"));
            connection.close();
        }
        catch (Exception e ){

        }

    }
}
