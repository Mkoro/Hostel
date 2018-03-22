package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
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
public class BookHostel implements Initializable {

    private DbHandler handler;
    private Connection conn;

    @FXML
    private JFXTextField txtRegNo;

    @FXML
    private FontAwesomeIconView btnSearch;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtReg_No;

    @FXML
    private JFXTextField txtCourse;

    @FXML
    private JFXTextField txtAge;

    @FXML
    private Text stud_id;

    @FXML
    private JFXCheckBox txtinternal;

    @FXML
    private JFXCheckBox txtExternal;

    @FXML
    private JFXComboBox<String> txtRoom;

    @FXML
    private JFXComboBox<String> txtHostel;

    @FXML
    private JFXComboBox<String> txtBedNumber;

    @FXML
    private JFXButton Status;

    @FXML
    private Label hostel_no;

    @FXML
    void Clear(ActionEvent event) {

    }

    @FXML
    void Preview(ActionEvent event) {

    }

    @FXML
    void Revoke(ActionEvent event) {

    }

    @FXML
    void Save(ActionEvent event) {

            try
            {
                String StudentId =  stud_id.getText();
                String HostelId = hostel_no.getText();
                String RoomNo = txtRoom.getValue();
                String BedNo = txtBedNumber.getValue();

                try {
                    if (StudentId.isEmpty() || HostelId.isEmpty() || RoomNo.isEmpty()
                            ||BedNo.isEmpty() )
                    {

                        TrayNotification tn = new TrayNotification("Warning", "Fill all the fields", NotificationType.WARNING);
                        tn.setAnimationType(AnimationType.POPUP);
                        tn.showAndDismiss(Duration.seconds(2));


                    } else {

                        insert(StudentId.toString(),HostelId.toString(),RoomNo.toString(), BedNo.toString());

                    }
                } catch (NumberFormatException c) {
                    TrayNotification tn = new TrayNotification("UPDATE", "Failed to get Fields", NotificationType.INFORMATION);
                    tn.setAnimationType(AnimationType.POPUP);
                    tn.showAndDismiss(Duration.seconds(2));

                }

            }catch (Exception e)
            {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            }

    }

    private void insert(String studentid, String HosId, String RoomNo, String BedNo) {

        try
        {

            conn = handler.getConnection();
            String InsertProduct = "INSERT INTO hostel.allocate(hostel_id,Room_No,Bed_No,student_id) " +
                    "VALUES (?,?,?,?)";

            PreparedStatement result = conn.prepareStatement(InsertProduct , Statement.RETURN_GENERATED_KEYS);

            // PreparedStatement result = conn.prepareStatement(InsertProduct);

            result.setString(1, HosId);
            result.setString(2, RoomNo);
            result.setString(3, BedNo);
            result.setString(4, studentid);
            result.executeUpdate();
            ResultSet rs = result.getGeneratedKeys();

            if(rs.next())
            {
                TrayNotification tn = new TrayNotification("Success", "Room allocation Details Saved Successfully ", NotificationType.SUCCESS);
                tn.setAnimationType(AnimationType.SLIDE);
                tn.showAndDismiss(Duration.seconds(2));

            } else if (!rs.next())
            {
                TrayNotification tn = new TrayNotification("Unable to Save", "Could Not Save Allocation Detail ", NotificationType.ERROR);
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
    void searchStudent(ActionEvent event) {
       try
        {

            String reg_no = txtRegNo.getText();

            System.out.println(reg_no);

            boolean exists = false;
            String search = "SELECT * FROM studentdetails WHERE  Reg_No=?";

            conn = handler.getConnection();
            PreparedStatement ps = conn.prepareStatement(search);
            ps.setString(1,reg_no);
            ResultSet result = ps.executeQuery();

            if(result.next())
            {
                txtName.setText(result.getString(2) + result.getString(3));
                txtCourse.setText(result.getString(4));
                txtReg_No.setText(result.getString(5));
                txtAge.setText(result.getString(7));

                String Stud_id = result.getString(1);

                stud_id.setText(Stud_id);

                getFees(Stud_id);
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

    private void getFees(String stud_id) {

        try
        {

            String getFee = "SELECT * FROM payment WHERE  student_id=?";
            conn = handler.getConnection();
            PreparedStatement ps = conn.prepareStatement(getFee);
            ps.setString(1,stud_id);
            ResultSet result = ps.executeQuery();
            if(result.next())
            {
                //txtName.setText(result.getString(2) + result.getString(3));

                int Fees = Integer.parseInt(result.getString(2));

                System.out.print(Fees);
                 //CalculateFees(Fees);

                 int Stud_Amount = 30000 ;

                    if((Fees /30000) * 100 <= 50 )
                    {
                        Status.setText("Pay 60% of Fee book");
                        Hostel();
                    }if((Fees /30000) * 100 >= 50 )
                    {
                        Status.setText("Fully Paid");

                        Hostel();
                    }
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
            //System.exit(0);
        }
    }

    private void Hostel() {
        try
        {
            String getHostel = "SELECT * FROM Hostel";
            conn = handler.getConnection();
            PreparedStatement ps = conn.prepareStatement(getHostel);
            ResultSet result = ps.executeQuery();

            if(result.next())
            {

                String Hostel_Name = result.getString(2);
               // String Rooms = result.getString(3);

                txtHostel.getItems().addAll(Hostel_Name);
                //txtRoom.getItems().addAll(Rooms);

                //String house;
                txtHostel.setOnAction(e ->
                {
                    String house= txtHostel.getValue();
                    System.out.print("lambda" +house);
                    RoomsAndBeds(house);

                });

                        // house = txtHostel.getValue());

               // ObservableList<String> house = txtHostel.getItems();
               // String house = txtHostel.getSelectedItem

               // System.out.print(house);

                //RoomsAndBeds(house);



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

    private void RoomsAndBeds(String house) {

        try
        {

            System.out.print("Rooms"+house);

            String getFee = "SELECT * FROM hostel WHERE  Hostel_Name=?";
            conn = handler.getConnection();
            PreparedStatement ps = conn.prepareStatement(getFee);
            ps.setString(1, String.valueOf(house));
            ResultSet result = ps.executeQuery();
            if(result.next())
            {


                int Hostel_id = result.getInt(1);
                int  Rooms = Integer.parseInt(result.getString(3));

                hostel_no.setText(String.valueOf(Hostel_id));

                int roomNo = 1;
                char hse = getFee.toUpperCase().charAt(0);

                for (roomNo = 1; roomNo <= Rooms; roomNo++) {
                    txtRoom.getItems().addAll(hse + "R" + roomNo);
                }

                //final String[] selectbed = {txtRoom.getSelectedItem().toString()};
                int  Beds_Room = Integer.parseInt(result.getString(4));

                txtRoom.setOnAction(e ->
                {
                    String selectbed = txtRoom.getValue();

                    int beds = 1;

                    for (beds = 1; beds <= Beds_Room; beds++) {
                        txtBedNumber.getItems().addAll( selectbed +"/B"+ beds);

                    }
                });


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
            //System.exit(0);
        }
    }


    public void initialize(URL location, ResourceBundle resources) {
        handler = new DbHandler();
    }

}
