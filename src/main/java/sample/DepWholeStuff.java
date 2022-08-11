package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.classesfortablviews.*;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DepWholeStuff {
    private String depId;

    public void setDepIdAndShow(String depId) throws SQLException {
        this.depId = depId;


        Connection connection = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);

        List<DocName> list = new LinkedList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("select name from doctors where depidfordoc =?");
        preparedStatement.setString(1, depId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            list.add(new DocName(resultSet.getString("name")));

        }
        ObservableList<DocName> observableList = FXCollections.observableList(list);

        docInfo.setCellValueFactory(new PropertyValueFactory<DocName, String>("name"));
        docsTable.setItems(observableList);

        List<NurName> list1 = new LinkedList<>();
        PreparedStatement preparedStatement1 = connection.prepareStatement("select name from nurses where depidfornur =?");
        preparedStatement1.setString(1, depId);
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        while (resultSet1.next()){
            list1.add(new NurName(resultSet1.getString("name")));

        }
        ObservableList<NurName> observableList1 = FXCollections.observableList(list1);

        nurInfo.setCellValueFactory(new PropertyValueFactory<NurName, String>("name"));
        nurTable.setItems(observableList1);


        connection.close();

    }

    @FXML
    private TableColumn<DocName, String > docInfo;

    @FXML
    private TableView<DocName> docsTable;

    @FXML
    private TableColumn<NurName, String > nurInfo;

    @FXML
    private TableView<NurName> nurTable;



}
