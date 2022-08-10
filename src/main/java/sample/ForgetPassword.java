package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ForgetPassword implements Initializable {

    Random r = new Random();
    @FXML
    private Button verifyButton;

    @FXML
    ImageView myImage;
    public int whereDidICameFrom = 0;

    @FXML
    RadioButton docRaBut;
    @FXML
    RadioButton nurRaBut;

    @FXML
    private ToggleGroup docOrNur;


    @FXML
    private TextField idField;


    @FXML
    private AnchorPane mypane;


    @FXML
    private PasswordField passField;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        verifyButton.setDisable(true);
        myImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            try {
                if (whereDidICameFrom == 0) {
                    ((Stage) (((Node) (event.getSource())).getScene().getWindow())).setScene(new Scene(FXMLLoader.load(ForgetPassword.this.getClass().getResource("../fxmls/secreEnter.fxml"))));
                }
                if (whereDidICameFrom == 1) {
                    ((Stage) (((Node) (event.getSource())).getScene().getWindow())).setScene(new Scene(FXMLLoader.load(ForgetPassword.this.getClass().getResource("../fxmls/doctorLogin.fxml"))));
                }
            } catch (IOException ex) {
                Logger.getLogger(ForgetPassword.class.getName()).log(Level.SEVERE, null, ex);
            }
            event.consume();
        });
    }

    private String Id;
    private int verifyPass;
    int type;

    @FXML
    void sendVerMail(ActionEvent event) throws SQLException, MessagingException {
        verifyPass = r.nextInt(999999);
        System.out.println(verifyPass);
        if (whereDidICameFrom == 1 && !(docRaBut.isSelected() || nurRaBut.isSelected())) {
            JOptionPane.showMessageDialog(null, "Please choose what you are");
            return;

        }
        String teteid = idField.getText().trim();
        if (teteid.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter your Id");
            return;
        }
        if (whereDidICameFrom == 1) {


            Connection connection = DriverManager.getConnection(Main.URL, Main.USER_NAME, Main.PASSWORD);
            if (docRaBut.isSelected()) {
                type = 0;
                ResultSet resultSet = connection.createStatement().executeQuery("select  * from doctors where stuff_id ='" +
                        teteid + "'");
                if (!resultSet.next()) {
                    JOptionPane.showMessageDialog(null, "Your id isn't found");
                    connection.close();
                    return;
                } else {
                    SendEmail.sendemail(resultSet.getString("email"), verifyPass);
                    JOptionPane.showMessageDialog(null, "Please check your email");
                    Id = teteid;
                    verifyButton.setDisable(false);
                    connection.close();
                    return;
                }

            } else if (nurRaBut.isSelected()) {
                type = 1;
                ResultSet resultSet = connection.createStatement().executeQuery("select  * from nurses where stuff_id ='" +
                        teteid + "'");
                if (!resultSet.next()) {
                    JOptionPane.showMessageDialog(null, "Your id isn't found");
                    connection.close();
                    return;
                } else {
                    SendEmail.sendemail(resultSet.getString("email"), verifyPass);
                    JOptionPane.showMessageDialog(null, "Please check your email");
                    Id = teteid;
                    verifyButton.setDisable(false);
                    connection.close();
                    return;
                }

            }

        } else if (whereDidICameFrom == 0) {
            Connection connection = Main.oracleDataSource.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("select  * from secres where stuff_id ='" +
                    teteid + "'");
            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Your id isn't found");
                connection.close();
                return;
            } else {
                SendEmail.sendemail(resultSet.getString("email"), verifyPass);
                JOptionPane.showMessageDialog(null, "Please check your email");
                Id = teteid;
                verifyButton.setDisable(false);
                connection.close();
                return;
            }

        }

    }

    @FXML
    void verify(ActionEvent event) throws SQLException, IOException {
        if (!(Integer.parseInt(passField.getText()) == verifyPass)) {
            JOptionPane.showMessageDialog(null, "The password is wrong");
            return;
        }
        Connection connection = Main.oracleDataSource.getConnection();
        if (whereDidICameFrom == 0) {
            ResultSet resultSet = connection.createStatement().executeQuery("select stuff_id ,name from secres where stuff_id ='"+
                    Id+"'");
            resultSet.next();
            String secId = resultSet.getString("stuff_id");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MainViewForSec.fxml"));
            Stage stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
            stage.setScene(new Scene(loader.load()));
            MainViewForSec mainViewForSec = loader.getController();
            mainViewForSec.setInitial(secId, resultSet.getString("name"));
            stage.show();
            connection.close();
            return;


        }
        if (whereDidICameFrom == 1) {
            if(type == 1) {
                ResultSet resultSet = connection.createStatement().executeQuery("select * from nurses where Stuff_id ='"+ Id +"'");
                resultSet.next();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MainViewForNur.fxml"));
                Stage stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
                stage.setScene(new Scene(loader.load()));
                MainViewForNur mainViewForDoctor = loader.getController();
                String depId = resultSet.getString("depidfornur");
                ResultSet rs = connection.createStatement().executeQuery("select nameofdep from departments where depart_id = '"+
                        depId+"'");
                rs.next();
                mainViewForDoctor.showAll(resultSet.getString("stuff_id"), resultSet.getString("name"),
                        depId, rs.getString("nameofdep"));

                stage.show();
                connection.close();
                return;

            }
            else if(type ==0){
                ResultSet resultSet = connection.createStatement().executeQuery("select stuff_id, name, depidfordoc, password from doctors where stuff_id ='"+Id +"'");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/MainViewForDoctor.fxml"));
                Stage stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
                stage.setScene(new Scene(loader.load()));
                MainViewForDoctor mainViewForDoctor = loader.getController();
                resultSet.next();
                String depId = resultSet.getString("depidfordoc");
                ResultSet rs = connection.createStatement().executeQuery("select nameofdep from departments where depart_id = '"+
                        depId+"'");
                rs.next();
                mainViewForDoctor.showAll(resultSet.getString("stuff_id"), resultSet.getString("name"),
                        depId, rs.getString("nameofdep"));

                stage.show();
                connection.close();
                return;

            }
        }

    }


}
