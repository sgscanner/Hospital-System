package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DoctorLogIn {
    @FXML
    private RadioButton DocRaButton;
    @FXML
    private RadioButton NurRaButton;
    @FXML
    private TextField IDTextField;
    @FXML
    private TextField passTextField;

    public void logIn(ActionEvent event){
        Connection con = null;
        String stuffID = IDTextField.getText();
        String password = passTextField.getText();
        if(stuffID.trim().isEmpty() || password.isEmpty()||(!DocRaButton.isSelected() && !NurRaButton.isSelected())){
            JOptionPane.showMessageDialog(null, "You must choose and fill the fields");
            return;
        }

        if(DocRaButton.isSelected()) {
            try {
                con = Main.oracleDataSource.getConnection();
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("select stuff_id, name, depidfordoc, password from doctors");
                while (resultSet.next()) {
                    if (resultSet.getString("stuff_id").equals(stuffID.trim())) {
                        if (password.trim().equals(resultSet.getString("password"))) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MainViewForDoctor.fxml"));
                            Stage stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
                            stage.setScene(new Scene(loader.load()));
                            MainViewForDoctor mainViewForDoctor = loader.getController();
                            String depId = resultSet.getString("depidfordoc");
                            ResultSet rs = con.createStatement().executeQuery("select nameofdep from departments where depart_id = '"+
                                    depId+"'");
                            rs.next();
                            mainViewForDoctor.showAll(resultSet.getString("stuff_id"), resultSet.getString("name"),
                            depId, rs.getString("nameofdep"));

                            stage.show();
                            con.close();
                            return;
                        } else {
                            JOptionPane.showMessageDialog(null, "The ID/PassWord is invalid");
                            con.close();
                            return;
                        }

                    }
                }
                JOptionPane.showMessageDialog(null, "The Id/PassWord is invalid");
                con.close();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        /////////////////////////////////////nurse
        if(NurRaButton.isSelected()) {
            try {
                con = Main.oracleDataSource.getConnection();
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("select stuff_id, name, depidfornur, password from nurses");
                while (resultSet.next()) {
                    if (resultSet.getString("stuff_id").equals(stuffID.trim())) {
                        if (password.trim().equals(resultSet.getString("password"))) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MainViewForNur.fxml"));
                            Stage stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
                            stage.setScene(new Scene(loader.load()));
                            MainViewForNur mainViewForDoctor = loader.getController();
                            String depId = resultSet.getString("depidfornur");
                            ResultSet rs = con.createStatement().executeQuery("select nameofdep from departments where depart_id = '"+
                                    depId+"'");
                            rs.next();
                            mainViewForDoctor.showAll(resultSet.getString("stuff_id"), resultSet.getString("name"),
                                    depId, rs.getString("nameofdep"));

                            stage.show();
                            con.close();
                            return;
                        } else {
                            JOptionPane.showMessageDialog(null, "The ID/PassWord is invalid");
                            con.close();
                            return;
                        }

                    }
                }
                JOptionPane.showMessageDialog(null, "The Id/PassWord is invalid");
                con.close();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

    }
    ForgetPassword x=new ForgetPassword();
    public  void forgotpass(ActionEvent event) throws IOException {

        FXMLLoader loader =new FXMLLoader(getClass().getResource("../fxmls/ForgetPassword.fxml"));
        Scene scene =new  Scene(loader.load());
        x=loader.getController();
        x.whereDidICameFrom =1;
        ((Stage) ( ( (Node)(event.getSource())).getScene().getWindow())).setScene(scene);

    }
    public  void returntomainview(ActionEvent event) throws IOException{
        ((Stage) ( ( (Node)(event.getSource())).getScene().getWindow())).setScene(new Scene(FXMLLoader.load(getClass().getResource("../fxmls/mainView.fxml"))));

}}
