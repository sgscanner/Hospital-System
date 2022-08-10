package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

public class MainViewForManager {
    private String name;
    private String id;
    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;


    public void setNameAndId(String name, String id) {
        this.name = name;
        this.id = id;
        nameLabel.setText("Name: " + name);
        idLabel.setText("ID: " + id);

    }
    @FXML
    void goToMakeSuccesor(ActionEvent event) throws IOException {
        ((Stage) (((Node) (event.getSource())).getScene().getWindow()))
                .setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/MakeASuccessorForManager.fxml"))));
    }

    @FXML
    public void logOut(ActionEvent event) throws IOException {
        ((Stage) (((Node) (event.getSource())).getScene().getWindow()))
                .setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/mainView.fxml"))));
    }
    @FXML
    void goToAddDep(ActionEvent event) throws IOException {
        ((Stage) (((Node) (event.getSource())).getScene().getWindow()))
                .setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/AddDepart.fxml"))));
    }
    @FXML
    void goToModifyDep(ActionEvent event) throws IOException {
        ((Stage) (((Node) (event.getSource())).getScene().getWindow()))
                .setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/ModifyDep.fxml"))));
    }
    @FXML
    void goToAddSec(ActionEvent event) throws IOException {
        ((Stage) (((Node) (event.getSource())).getScene().getWindow()))
                .setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/AddSec.fxml"))));
    }
    @FXML
    void viewDocsRep(ActionEvent event) throws SQLException, JRException, IOException {
        Connection connection = Main.oracleDataSource.getConnection();
        InputStream inputStream = new FileInputStream(new File("doctors_rep.jrxml"));
        //OutputStream outputStream = new FileOutputStream(new File("Haha.pdf") );

        JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null , connection);
        //JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        net.sf.jasperreports.swing.JRViewer jv = new net.sf.jasperreports.swing.JRViewer(jasperPrint);
//Insert viewer to a JFrame to make it showable
        JFrame jf = new JFrame();
        jf.getContentPane().add(jv);
        jf.validate();
        jf.setVisible(true);
        jf.setSize(new Dimension(1024,768));
        jf.setLocation(1,1);
        jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        connection.close();
        inputStream.close();
    }

    @FXML
    void viewPatsRep(ActionEvent event) throws SQLException, JRException, IOException{
        Connection connection = Main.oracleDataSource.getConnection();
        InputStream inputStream = new FileInputStream(new File("pat_rep1.jrxml"));
        //OutputStream outputStream = new FileOutputStream(new File("Haha.pdf") );

        JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null , connection);
        //JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        net.sf.jasperreports.swing.JRViewer jv = new net.sf.jasperreports.swing.JRViewer(jasperPrint);

        JFrame jf = new JFrame();
        jf.getContentPane().add(jv);
        jf.validate();
        jf.setVisible(true);
        jf.setSize(new Dimension(1024,768));
        jf.setLocation(1,1);
        jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        connection.close();
        inputStream.close();

    }
    public void visitPastMans(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/PastMangers.fxml"));
        Parent parent = loader.load();
        PastMangers pastMangers = loader.getController();
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).setScene(new Scene(parent));
        pastMangers.fillData();

    }
}
