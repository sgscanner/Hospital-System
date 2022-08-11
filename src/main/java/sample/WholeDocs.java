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

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class WholeDocs implements Initializable {
    private String secId;

    public void setSecIdI(String id) {
        secId = id;
    }

    @FXML
    private TableColumn<DoctorInfoForView, String> departCol;

    @FXML
    private TableColumn<DoctorInfoForView, String> idCol;

    @FXML
    private TableColumn<DoctorInfoForView, String> nameCol;

    @FXML
    private TableView<DoctorInfoForView> table;

    @FXML
    public void deleteSel(ActionEvent event) throws SQLException {
        String id = table.getSelectionModel().getSelectedItem().getId();
        Connection connection = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement("delete from doctors where stuff_id = ?");
        preparedStatement.setString(1, id);
        preparedStatement.executeUpdate();
        JOptionPane.showMessageDialog(null, "Deleted Successfully");
        fillData();
        connection.close();

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

    public void fillData() {
        try {
            table.getItems().clear();
            List<DoctorInfoForView> list = new LinkedList<>();
            Connection connection = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);
            ResultSet resultSet =
                    connection.createStatement().executeQuery("select name, stuff_id, depidfordoc from doctors");
            while (resultSet.next()) {
                //String depId = resultSet.getString("depidfordoc");
                PreparedStatement preparedStatement =
                        connection.prepareStatement("select nameofdep from departments where depart_id = ?");
                preparedStatement.setString(1, resultSet.getString("depidfordoc"));
                ResultSet resultSet1 = preparedStatement.executeQuery();
                resultSet1.next();
                list.add(
                        new DoctorInfoForView(resultSet.getString("stuff_id"),
                                resultSet.getString("name"), resultSet1.getString("nameofdep")));

            }
            connection.close();
            ObservableList<DoctorInfoForView> observableList = FXCollections.observableList(list);

            idCol.setCellValueFactory(new PropertyValueFactory<DoctorInfoForView, String>("id"));
            nameCol.setCellValueFactory(new PropertyValueFactory<DoctorInfoForView, String>("name"));
            departCol.setCellValueFactory(new PropertyValueFactory<DoctorInfoForView, String>("depart"));

            table.setItems(observableList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillData();

    }
}
