package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.DriverManager;

public class Main extends Application {
    public static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    public static final String USER_NAME = "c##project";
    public static final String PASSWORD = "654321";
    public static OracleDataSource oracleDataSource;
    public static void main(String[] args) {
        try {





            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            oracleDataSource = new OracleDataSource();
            oracleDataSource.setURL(URL);
            oracleDataSource.setUser(USER_NAME);
            oracleDataSource.setPassword(PASSWORD);



            //outputStream.close();





        } catch (Throwable e) {
            e.printStackTrace();
        }

       launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("../fxmls/mainView.fxml"));
        primaryStage.setTitle("Hospital Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    //linear-gradient(to bottom right, #353a5f, #9ebaf3);
}
