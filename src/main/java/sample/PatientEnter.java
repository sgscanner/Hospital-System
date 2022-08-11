package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.opc.PackageRelationship;


import javax.swing.JOptionPane;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;


public class PatientEnter implements Initializable {

    @FXML
    private VBox visitsVBox;
    @FXML
    private VBox treatsVBox;
    @FXML
    private TextField numTF;
    @FXML
    private RadioButton ssnRaBut;
    @FXML
    private RadioButton patRaBut;

    @FXML

    public void returnToMainView(ActionEvent event) throws IOException {
        ((Stage) (((Node) (event.getSource())).getScene().getWindow()))
                .setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/mainView.fxml"))));

    }

    public void clearData() {
        visitsVBox.getChildren().clear();
        treatsVBox.getChildren().clear();
    }

    public void clearAll() {
        clearData();
        numTF.setText("");
        ssnRaBut.setSelected(false);
        patRaBut.setSelected(false);
    }

    @FXML
    public void find() throws SQLException, IOException {
        clearData();
        if (!patRaBut.isSelected() && !ssnRaBut.isSelected()) {
            JOptionPane.showMessageDialog(null, "Please Select type of your ID");

            return;
        }
        String Id = numTF.getText().trim();

        Connection connection = Main.oracleDataSource.getConnection();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        if (ssnRaBut.isSelected()) {
            ResultSet resultSet = connection.createStatement().executeQuery("select ssn, pat_id from patients");
            while (resultSet.next()) {
                if (resultSet.getString("ssn").equals(Id)) {
                    String patId = resultSet.getString("pat_id");
                    PreparedStatement preparedStatement =
                            connection.prepareStatement("select * from departvis where patid = ?");
                    preparedStatement.setString(1, patId);
                    ResultSet trs = preparedStatement.executeQuery();
                    while (trs.next()) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/VisCard.fxml"));
                        String depId = trs.getString("depid");
                        preparedStatement =
                                connection.prepareStatement("select name from departments where depart_id = ?");
                        preparedStatement.setString(1, depId);
                        ResultSet s = preparedStatement.executeQuery();
                        s.next();
                        Node n = loader.load();
                        Date StartDate = trs.getDate("starttime");
                        String endString = "Until: ";
                        if (trs.getInt("ava") == 0) {
                            Date endDate = trs.getDate("endtime");
                            endString += dateFormat.format(endDate);
                        } else endString += "now";
                        VisitCard control = loader.getController();
                        control.setAll("Department:" +
                                s.getString("name"), "From: " + dateFormat.format(StartDate), endString);
                        visitsVBox.getChildren().add(n);


                    }

                    ///////////////////////////////////
                    preparedStatement = connection.prepareStatement("select * from emervis where patid = ?");
                    preparedStatement.setString(1, patId);
                    ResultSet trs1 = preparedStatement.executeQuery();
                    while (trs1.next()) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/EmerVisitCard.fxml"));

                        Node n = loader.load();
                        Date StartDate = trs1.getDate("startdate");
                        String endString = "Until: ";
                        if (trs1.getInt("ava") == 0) {
                            Date endDate = trs1.getDate("endtime");
                            endString += dateFormat.format(endDate);
                        } else endString += "now";
                        EmergencyVisitCard control = loader.getController();
                        control.setData("From: " + dateFormat.format(StartDate), endString);
                        visitsVBox.getChildren().add(n);


                    }
                    //////////////////////////////////
                    preparedStatement = connection.prepareStatement("select * from treats where patid =?");
                    preparedStatement.setString(1, patId);
                    ResultSet trs2 = preparedStatement.executeQuery();
                    while (trs2.next()) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MedCard.fxml"));
                        String medId = trs2.getString("medid");
                        preparedStatement = connection.prepareStatement("select medname from medecines where medid = ?");
                        preparedStatement.setString(1, medId);
                        ResultSet s = preparedStatement.executeQuery();
                        String docId = trs2.getString("docid");
                        preparedStatement = connection.prepareStatement("select name from doctors where stuff_id = ?");
                        preparedStatement.setString(1, docId);
                        ResultSet s2 = preparedStatement.executeQuery();
                        s2.next();
                        s.next();
                        Node n = loader.load();
                        Date StartDate = trs2.getDate("Dateofgive");

                        MedCard control = loader.getController();
                        control.setAll(s.getString("medname") + " " +
                                        trs2.getInt("dozen"), "By: " + s2.getString("name")
                                , "From: " + dateFormat.format(StartDate),
                                trs2.getInt("noofdozen") +
                                        " times for " + trs2.getInt("nodays") + " days");
                        treatsVBox.getChildren().add(n);


                    }

                    connection.close();
                    return;
                }
            }
        }


        ////////////////////////////////
        else if (patRaBut.isSelected()) {
            ResultSet resultSet = connection.createStatement().executeQuery("select pat_id from patients");
            while (resultSet.next()) {
                String patId = resultSet.getString("pat_id");
                if (patId.equals(Id)) {
                    PreparedStatement preparedStatement =
                            connection.prepareStatement("select * from departvis where patid = ?");
                    preparedStatement.setString(1, patId);
                    ResultSet trs = preparedStatement.executeQuery();
                    while (trs.next()) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/VisCard.fxml"));
                        String depId = trs.getString("depid");
                        preparedStatement =
                                connection.prepareStatement("select nameofdep from departments where depart_id = ?");
                        preparedStatement.setString(1, depId);
                        ResultSet s = preparedStatement.executeQuery();
                        s.next();
                        Node n = loader.load();
                        Date StartDate = trs.getDate("starttime");
                        String endString = "Until: ";
                        if (trs.getInt("ava") == 0) {
                            Date endDate = trs.getDate("endtime");
                            endString += dateFormat.format(endDate);
                        } else endString += "now";
                        VisitCard control = loader.getController();
                        control.setAll("Department:" +
                                s.getString("nameofdep"), "From: " + dateFormat.format(StartDate), endString);
                        visitsVBox.getChildren().add(n);


                    }

                    ///////////////////////////////////
                    preparedStatement = connection.prepareStatement("select * from emervis where patid = ?");
                    preparedStatement.setString(1, patId);
                    ResultSet trs1 = preparedStatement.executeQuery();
                    while (trs1.next()) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/EmerVisitCard.fxml"));

                        Node n = loader.load();
                        Date StartDate = trs1.getDate("startdate");
                        String endString = "Until: ";
                        if (trs1.getInt("ava") == 0) {
                            Date endDate = trs1.getDate("endtime");
                            endString += dateFormat.format(endDate);
                        } else endString += "now";
                        EmergencyVisitCard control = loader.getController();
                        control.setData("From: " + dateFormat.format(StartDate), endString);
                        visitsVBox.getChildren().add(n);


                    }
                    //////////////////////////////////
                    preparedStatement = connection.prepareStatement("select * from treats where patid = ?");
                    preparedStatement.setString(1, patId);
                    ResultSet trs2 = preparedStatement.executeQuery();
                    while (trs2.next()) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MedCard.fxml"));
                        String medId = trs2.getString("medid");
                        preparedStatement =
                                connection.prepareStatement("select medname from medecines where medid = ?");
                        preparedStatement.setString(1, medId);
                        ResultSet s = preparedStatement.executeQuery();
                        String docId = trs2.getString("docid");
                        preparedStatement =
                                connection.prepareStatement("select name from doctors where stuff_id = ?");
                        preparedStatement.setString(1, docId);
                        ResultSet s2 = preparedStatement.executeQuery();
                        s2.next();
                        s.next();
                        Node n = loader.load();
                        Date StartDate = trs2.getDate("Dateofgive");

                        MedCard control = loader.getController();
                        control.setAll(s.getString("medname") + " " +
                                        trs2.getInt("dozen"), "By: " + s2.getString("name")
                                , "From: " + dateFormat.format(StartDate),
                                trs2.getInt("noofdozen") +
                                        " times for " + trs2.getInt("nodays") + " days");
                        treatsVBox.getChildren().add(n);


                    }

                    connection.close();
                    return;
                }
            }
        }

        JOptionPane.showMessageDialog(null, "The id isn't Available");
        connection.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ssnRaBut.setSelected(true);
    }
}
