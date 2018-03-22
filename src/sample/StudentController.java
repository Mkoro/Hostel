package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Created by user1 on 5/30/2017.
 */
public class StudentController implements Initializable {

    private DbHandler handler;
    private Connection conn;

    @FXML
    private MaterialDesignIconView iconClose;

    @FXML
    private JFXTextField txtFirst;

    @FXML
    private JFXTextField txtLast;

    @FXML
    private JFXTextField txtReg;

    @FXML
    private JFXTextField txtCourse;

    @FXML
    private JFXTextField txtAge;;

    @FXML
    private JFXRadioButton txtMale;

    @FXML
    private JFXRadioButton txtFemale;

    @FXML
    private JFXButton btnSaveStudent;

    @FXML
    private ToggleGroup Gendertoogle;

    @FXML
    void closeStage(MouseEvent event) {
       // Platform.exit();
        iconClose.getScene().getWindow().hide();
    }

    @FXML
    void saveStudent(ActionEvent event) {

        try
        {
            String First =  txtFirst.getText();
            String Last = txtLast.getText();
            String Reg = txtReg.getText();
            String Course = txtCourse.getText();
            String Age = txtAge.getText();

            String Gender = "";

            if(txtMale.isSelected())
            {
                Gender += txtMale.getText() +"\n";
            }if(txtFemale.isSelected())
            {
                Gender += txtFemale.getText() +"\n";
            }

            System.out.print(Gender);

            try {
                if (First.isEmpty() || Last.isEmpty() || Reg.isEmpty()
                        ||Course.isEmpty() || Age.isEmpty()  || Gender.isEmpty())
                {

                    TrayNotification tn = new TrayNotification("Warning", "Fill all the fields", NotificationType.WARNING);
                    tn.setAnimationType(AnimationType.POPUP);
                    tn.showAndDismiss(Duration.seconds(2));


                } else {

                    insert(First.toString(),Last.toString(),Reg.toString(), Course.toString(),
                            Age.toString(), Gender.toString());

                }
            } catch (NumberFormatException c) {
                TrayNotification tn = new TrayNotification("UPDATE", "Failed to get Fields", NotificationType.INFORMATION);
                tn.setAnimationType(AnimationType.POPUP);
                tn.showAndDismiss(Duration.seconds(2));

            }


        }catch(Exception e)
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    private void insert(String First, String Last, String Reg, String Course, String Age, String Gender) {
       try
       {

           conn = handler.getConnection();

           String InsertProduct = "INSERT INTO hostel.studentdetails(First_Name,Last_Name,Course,Reg_No," +
                   "Gender,Age) " +
                   "VALUES (?,?,?,?,?,?)";

           PreparedStatement result = conn.prepareStatement(InsertProduct ,Statement.RETURN_GENERATED_KEYS);

          // PreparedStatement result = conn.prepareStatement(InsertProduct);

           result.setString(1, First);
           result.setString(2, Last);
           result.setString(3, Course);
           result.setString(4, Reg);
           result.setString(5, Gender);
           result.setString(6, Age);
           System.out.print(First+ "," +Last+ "," +Course+ ","+Reg+","+Gender+","+Age);
           result.executeUpdate();
           ResultSet rs = result.getGeneratedKeys();

           if(rs.next())
           {
               TrayNotification tn = new TrayNotification("Success", "Student Details Saved Successfully ", NotificationType.SUCCESS);
               tn.setAnimationType(AnimationType.SLIDE);
               tn.showAndDismiss(Duration.seconds(2));

           } else if (!rs.next())
           {
               TrayNotification tn = new TrayNotification("Unable to Save", "Could Not Save Student Detaisl ", NotificationType.ERROR);
               tn.setAnimationType(AnimationType.POPUP);
               tn.showAndDismiss(Duration.seconds(2));
           }

       }catch(Exception e)
       {
           e.printStackTrace();
           TrayNotification tn = new TrayNotification("Warning", "Failed to upload ", NotificationType.WARNING);
           tn.setAnimationType(AnimationType.POPUP);
           tn.showAndDismiss(Duration.seconds(2));
       }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DbHandler();
    }
}
