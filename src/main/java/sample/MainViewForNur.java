package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.classesfortablviews.DawamDoc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MainViewForNur {
    private String docId;
    private String docName;
    private String depName;
    private String depId;
    @FXML
    private TableView<DawamDoc> dawamTable;

    @FXML
    private TableColumn<DawamDoc, String> dayCol;

    @FXML
    private Label depNameLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private TableColumn<DawamDoc, String> timeCol;
    public void showAll(String id, String name, String depId, String depName ) throws SQLException {
        this.docName = name;
        this.docId = id;
        this.depId = depId;
        this.depName = depName;
        nameLabel.setText("Name: "+ name);
        depNameLabel.setText("Department: "+ this.depName);
        idLabel.setText("ID: "+ docId);
        List<DawamDoc> es = new LinkedList<>();
        Connection connection = Main.oracleDataSource.getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("Select * from nurdawam where nurId = '"+docId+"'");
        while(resultSet.next()){
            ResultSet resultSet1 = connection.createStatement().executeQuery("select name from days where id ='"+
                    resultSet.getString("dayid")+"'");
            String timeFormTo = Integer.toString(resultSet.getInt("starttime")) +" to " +
                    Integer.toString(resultSet.getInt("endtime"));
            resultSet1.next();
            es.add(new DawamDoc(resultSet1.getString("name"),timeFormTo));


        }


        ObservableList<DawamDoc> observableList = FXCollections.observableList(es);
        dayCol.setCellValueFactory(new PropertyValueFactory<DawamDoc, String>("dayName"));
        timeCol.setCellValueFactory(new PropertyValueFactory<DawamDoc, String>("timeOfDawam"));
        dawamTable.setItems(observableList);


    }



    @FXML
    void logOut(ActionEvent event) {
        try {
            ( (Stage)   (       ((Node)event.getSource()).getScene().getWindow())).setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/mainView.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void openDepWholeStuff(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/DepWholeStuff.fxml"));
        Parent parent = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();

        DepWholeStuff depWholeStuff = loader.getController();

        depWholeStuff.setDepIdAndShow(depId);





    }
}

