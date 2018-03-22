package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Created by user1 on 5/30/2017.
 */
public class PaymentController implements Initializable {

    private DbHandler handler;
    private Connection conn;

    @FXML
    private MaterialDesignIconView iconClose;

    @FXML
    private JFXTextField txtRegNo;

    @FXML
    private JFXTextField txtStudentNames;

    @FXML
    private JFXTextField txtLast;

    @FXML
    private JFXTextField txtCourse;

    @FXML
    private JFXTextField txtAge;

    @FXML
    private JFXTextField txtAmount;

    @FXML
    private JFXTextField txtMode;

    @FXML
    private JFXDatePicker txtDate;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton Student_id;

    @FXML
    void SavePayment(ActionEvent event) {

        try
        {
            String Amount =  txtAmount.getText();
            String Mode = txtMode.getText();
            LocalDate Date = txtDate.getValue();
            String S_Id = Student_id.getText();

            System.out.print(S_Id);

            try {
                if (Amount.isEmpty() || Mode.isEmpty()|S_Id.isEmpty())
                {

                    TrayNotification tn = new TrayNotification("Warning", "Fill all the fields", NotificationType.WARNING);
                    tn.setAnimationType(AnimationType.POPUP);
                    tn.showAndDismiss(Duration.seconds(2));


                } else {

                    insert(Amount.toString(),Mode.toString(),Date.toString(), S_Id.toString());

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

    private void insert(String Amount, String Mode, String Date, String S_Id)
    {
        try
        {

            conn = handler.getConnection();
            String InsertProduct = "INSERT INTO hostel.Payment(Amount,Payment_mode,PayDate,student_id) " +
                    "VALUES (?,?,?,?)";

            PreparedStatement result = conn.prepareStatement(InsertProduct , Statement.RETURN_GENERATED_KEYS);

            // PreparedStatement result = conn.prepareStatement(InsertProduct);

            result.setString(1, Amount);
            result.setString(2,Mode );
            result.setString(3, Date);
            result.setString(4, S_Id);
            System.out.print(Amount+ "," +Mode+ "," +Date+ ","+S_Id);
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

    @FXML
    void closeStage(MouseEvent event) {

    }

    @FXML
    void searchStudent(ActionEvent event) {

        try
        {

            String reg_no = txtRegNo.getText();
            boolean exists = false;
            String search = "SELECT * FROM studentdetails WHERE  Reg_No=?";

            conn = handler.getConnection();
            PreparedStatement ps = conn.prepareStatement(search);
            ps.setString(1,reg_no);
            ResultSet result = ps.executeQuery();

            if(result.next())
            {
                txtStudentNames.setText(result.getString(2));
                txtCourse.setText(result.getString(4));
                txtLast.setText(result.getString(3));
                txtAge.setText(result.getString(7));
                Student_id.setText(result.getString(1));
            }
            else if (!result.next())
            {
                //showError("username or password is invalid ");
                TrayNotification tn = new TrayNotification("Fail", "No student With that Registration number ", NotificationType.ERROR);
                tn.setAnimationType(AnimationType.POPUP);
                tn.showAndDismiss(Duration.seconds(2));
            }

        }catch (Exception e)
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DbHandler();
    }
}
