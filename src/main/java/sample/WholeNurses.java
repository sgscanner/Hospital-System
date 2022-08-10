package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.classesfortablviews.DoctorInfoForView;
import sample.classesfortablviews.NurseInfoForView;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class WholeNurses implements Initializable {
    private String secId;
    public void setSecIdI(String id){
        secId = id;
    }
    @FXML
    private TableColumn<NurseInfoForView, String> departCol;

    @FXML
    private TableColumn<NurseInfoForView, String> idCol;

    @FXML
    private TableColumn<NurseInfoForView, String> nameCol;

    @FXML
    private TableView<NurseInfoForView> table;
    @FXML
    public void deleteSel(ActionEvent event) throws SQLException {
        String id = table.getSelectionModel().getSelectedItem().getId();
        Connection connection = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);
        connection.createStatement().executeUpdate("delete from nurses where stuff_id ='"+
                id+"'");
        JOptionPane.showMessageDialog(null, "Deleted Successfully");
        fillData();
        connection.close();

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

    public void fillData(){
        try{
            table.getItems().clear();
            List<NurseInfoForView> list = new LinkedList<>();
            Connection connection = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);
            ResultSet resultSet = connection.createStatement().executeQuery("select name, stuff_id, depidfornur from nurses");
            while (resultSet.next()){
                //String depId = resultSet.getString("depidfordoc");
                ResultSet resultSet1 = connection.createStatement().executeQuery("select nameofdep from departments where depart_id ='"+
                        resultSet.getString("depidfornur")+"'");
                resultSet1.next();
                list.add(
                        new NurseInfoForView(resultSet.getString("stuff_id"), resultSet.getString("name"),
                                resultSet1.getString("nameofdep"))
                );

            }
            connection.close();
            ObservableList<NurseInfoForView> observableList = FXCollections.observableList(list);

            idCol.setCellValueFactory(new PropertyValueFactory<NurseInfoForView,String>("id"));
            nameCol.setCellValueFactory(new PropertyValueFactory<NurseInfoForView, String>("name"));
            departCol.setCellValueFactory(new PropertyValueFactory<NurseInfoForView, String>("depart"));

            table.setItems(observableList);



        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillData();

    }
}
