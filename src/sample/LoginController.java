package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * Created by user1 on 5/29/2017.
 */
public class LoginController implements Initializable{


    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXTextField Username;

    @FXML
    private JFXPasswordField Password;

    private DbHandler handler;
    private Connection conn;

    @FXML
    void doLogin(ActionEvent event) {

        try
        {
           String User =  Username.getText();
            String pass = Password.getText();

            System.out.println(User);
            System.out.println(pass);


            Boolean flag1 = User.isEmpty() || pass.isEmpty();
            if (flag1) {

                TrayNotification tn = new TrayNotification("Fail", "Fill all the fields ", NotificationType.WARNING);
                tn.setAnimationType(AnimationType.POPUP);
                tn.showAndDismiss(Duration.seconds(2));
            }else
            {
                String getAdmin = "SELECT * FROM admin WHERE Username = " + "'"
                        +User+"'" +" AND Password = "+"'" +pass +"'";
                conn = handler.getConnection();
                PreparedStatement ps = conn.prepareStatement(getAdmin);
                ResultSet result = ps.executeQuery();

                if(result.next())
                {
                    if(result.getString("Username") !=null && result.getString("Password") != null)
                    {
                        String  Username = result.getString("Username");
                        System.out.println( "Username = " + Username );
                        String password = result.getString("Password");
                        System.out.println("Password = " + password);

                        TrayNotification tn = new TrayNotification("Success", "Successfully logged in ", NotificationType.SUCCESS);
                        tn.setAnimationType(AnimationType.POPUP);
                        tn.showAndDismiss(Duration.seconds(2));

                        //btnLogin.getScene().getWindow().hide();
                        Parent root= FXMLLoader.load(getClass().getResource("Main.fxml"));
                        Stage mainStage=new Stage();
                        Scene scene=new Scene(root);
                        mainStage.setScene(scene);
                        mainStage.show();
                    }
                }
                else if (!result.next())
                {
                    //showError("username or password is invalid ");
                    TrayNotification tn = new TrayNotification("Fail", "Wrong Username/Password ", NotificationType.ERROR);
                    tn.setAnimationType(AnimationType.POPUP);
                    tn.showAndDismiss(Duration.seconds(2));
                }


            }

        }catch (Exception e)
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            //System.exit(0);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DbHandler();
    }
}
