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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MainViewForDoctor {
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

    public void showAll(String id, String name, String depId, String depName) throws SQLException {
        this.docName = name;
        this.docId = id;
        this.depId = depId;
        this.depName = depName;
        nameLabel.setText("Name: " + name);
        depNameLabel.setText("Department: " + this.depName);
        idLabel.setText("ID: " + docId);
        List<DawamDoc> es = new LinkedList<>();
        Connection connection = Main.oracleDataSource.getConnection();
        PreparedStatement preparedStatement =
                connection.prepareStatement("Select * from dawamdac where DocId = ?");
        preparedStatement.setString(1, docId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            preparedStatement = connection.prepareStatement("select name from days where id =?");
            preparedStatement.setString(1, resultSet.getString("dayid"));
            ResultSet resultSet1 = preparedStatement.executeQuery();
            String timeFormTo = Integer.toString(resultSet.getInt("starttime")) + " to " +
                    Integer.toString(resultSet.getInt("fintime"));
            resultSet1.next();
            es.add(new DawamDoc(resultSet1.getString("name"), timeFormTo));


        }


        ObservableList<DawamDoc> observableList = FXCollections.observableList(es);
        dayCol.setCellValueFactory(new PropertyValueFactory<DawamDoc, String>("dayName"));
        timeCol.setCellValueFactory(new PropertyValueFactory<DawamDoc, String>("timeOfDawam"));
        dawamTable.setItems(observableList);


    }

    @FXML
    void goToTreatPat(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/giveMedView.fxml"));
        Parent parent = loader.load();
        GiveMedView giveMedView = loader.getController();
        giveMedView.setDocId(docId);
        ((Stage) (((Node) (event.getSource())).getScene().getWindow())).setScene(new Scene(parent));


    }

    @FXML
    void logOut(ActionEvent event) {
        try {
            ((Stage) (((Node) event.getSource())
                    .getScene().getWindow()))
                    .setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/mainView.fxml"))));
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

