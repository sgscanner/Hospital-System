package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.log4j.DailyRollingFileAppender;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.time.ZoneId;

public class AddPatient {
    private String secId;

    public void setSecId(String s) {
        this.secId = s;
    }


    @FXML
    private TextField addressField;

    @FXML
    private DatePicker dobSelect;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField ssnField;

    @FXML
    void goBack(ActionEvent event) throws SQLException, IOException {
        Connection connection = Main.oracleDataSource.getConnection();
        PreparedStatement preparedStatement =
                connection.prepareStatement("select stuff_id, name from secres where Stuff_id = ?");
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
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter an id");
            return;
        }
        Connection connection = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);
        ResultSet resultSet1 = connection.createStatement()
                .executeQuery("Select pat_id from patients");
        while (resultSet1.next()) {
            if (resultSet1.getString("pat_id").equals(id)) {
                JOptionPane.showMessageDialog(null, "Please Don't use a past id");
                connection.close();
                return;
            }
        }
        PreparedStatement preparedStatement = connection.prepareStatement("insert into patients values (?, ?, ?, ?, ?, ?)");
        preparedStatement.setString(1, id);
        preparedStatement.setString(2, addressField.getText().trim());
        preparedStatement.setDate(3,
                new java.sql.Date(java.util.Date.from(dobSelect.getValue().atStartOfDay(ZoneId.systemDefault())
                        .toInstant()).getTime()));
        preparedStatement.setString(4, phoneField.getText().trim());
        preparedStatement.setString(5, ssnField.getText().trim());
        preparedStatement.setString(6, nameField.getText().trim());
        preparedStatement.executeUpdate();
        JOptionPane.showMessageDialog(null, "Patient added successfully");
        connection.close();
    }

}
