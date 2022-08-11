package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.classesfortablviews.ManagerInformation;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class PastMangers implements Initializable {


    @FXML
    private TableColumn<ManagerInformation, String> endDateCol;


    @FXML
    private TableColumn<ManagerInformation, String> idCol;

    @FXML
    private TableColumn<ManagerInformation, String> nameCol;

    @FXML
    private TableColumn<ManagerInformation, String> stDateCol;

    @FXML
    private TableView<ManagerInformation> table;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void fillData() throws SQLException {
        table.getItems().clear();
        Connection connection = Main.oracleDataSource.getConnection();
        ResultSet resultSet = connection.createStatement()
                .executeQuery("Select * from managers where currently = 0");
        List<ManagerInformation> list = new LinkedList<>();
        while (resultSet.next()) {
            list.add(new ManagerInformation(
                    resultSet.getString("manager_id"),
                    resultSet.getString("manager_name"),
                    dateFormat.format(resultSet.getDate("startdate")),
                    dateFormat.format(resultSet.getDate("enddate"))
            ));

        }
        ObservableList<ManagerInformation> observableList = FXCollections.observableList(list);
        idCol.setCellValueFactory(new PropertyValueFactory<ManagerInformation, String>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<ManagerInformation, String>("Name"));
        stDateCol.setCellValueFactory(new PropertyValueFactory<ManagerInformation, String>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<ManagerInformation, String>("endDate"));
        table.setItems(observableList);


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void goBack(ActionEvent event) throws SQLException, IOException {
        ResultSet resultSet = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD).createStatement()
                .executeQuery("Select * from managers where currently = 1");
        resultSet.next();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MainViewForManager.fxml"));
        Parent root = loader.load();
        MainViewForManager mainViewForManager = loader.getController();
        mainViewForManager.setNameAndId(resultSet.getString("manager_name"),
                resultSet.getString("manager_id"));
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(new Scene(root));
    }

    @FXML
    public void deleteSelections() throws SQLException {
        if (JOptionPane.showConfirmDialog(null, "Are you sure?") == JOptionPane.YES_OPTION) {
            ManagerInformation managerInformation = table.getSelectionModel().getSelectedItem();
            String id = managerInformation.getId();
            Connection connection = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "delete from managers where Manager_id = ?"
            );
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Deleted Successfully");
            fillData();
            connection.close();

        }
    }
}
