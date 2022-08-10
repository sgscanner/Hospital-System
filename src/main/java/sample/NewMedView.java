package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class NewMedView {
    @FXML
    private TextField medNo;
    @FXML
    private TextField medName;
    private String docId;
    public void setDocId(String id){
        docId = id;
    }
    public void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/giveMedView.fxml"));
        Parent parent = loader.load();
        GiveMedView giveMedView = loader.getController();
        giveMedView.setDocId(docId);
        ((Stage)(((Node)(event.getSource())).getScene().getWindow())).setScene(new Scene(parent));

    }

    public void submitMed() {
        String tempMedId = medNo.getText().trim();
        if (tempMedId.length() > 20) {
            JOptionPane.showMessageDialog(null, "The Id mustn't exceed 20 digits");

            return;
        }
        Connection connection = null;

        try {
            connection = Main.oracleDataSource.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("Select * from medecines");
            while (resultSet.next()) {
                if (resultSet.getString("medId").equals(tempMedId)) {
                    JOptionPane.showMessageDialog(null, "The Id is already available");
                    connection.close();
                    return;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if(connection!= null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        }
        try {
            connection.createStatement().executeUpdate("insert into medecines values( '" + tempMedId + "','" + medName.getText() + "')");
            JOptionPane.showMessageDialog(null, "added successfully");
            connection.close();
        } catch (SQLException e) {
            if(connection!= null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        }


    }
}
