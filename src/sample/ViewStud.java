package sample;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * Created by user1 on 6/5/2017.
 */

public class ViewStud implements Initializable {

    private DbHandler handler;
    private Connection conn;

    @FXML
    private JFXTextField txtFirst;

    @FXML
    private JFXTextField txtLast;

    @FXML
    private JFXTextField txtReg;

    @FXML
    private JFXTextField txtCourse;

    @FXML
    private JFXTextField txtAge;

    @FXML
    private JFXRadioButton txtmale;

    @FXML
    private ToggleGroup gender;

    @FXML
    private JFXRadioButton txtfemale;

    @FXML
    private TableView<orderModel> studtable;

    @FXML
    private TableColumn<orderModel, String> StudId;

    @FXML
    private TableColumn<orderModel, String> firstcol;

    @FXML
    private TableColumn<orderModel, String> lastcol;

    @FXML
    private TableColumn<orderModel, String> regcol;

    @FXML
    private TableColumn<orderModel, String> coursecol;

    @FXML
    private TableColumn<orderModel, String> agecol;

    ObservableList<orderModel> List = FXCollections.observableArrayList();

    @FXML
    void Delete(ActionEvent event) {

    }

    @FXML
    void Preview(ActionEvent event) {

    }

    @FXML
    void Update(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        handler = new DbHandler();
        loadTableColumn();
        loadData();

    }

    private void loadTableColumn() {
        StudId.setCellValueFactory(new PropertyValueFactory<orderModel, String>("Stud_id"));
        firstcol.setCellValueFactory(new PropertyValueFactory<orderModel, String>("First_Name"));
    }

    private void loadData() {
        try
        {
            String getData = "SELECT * FROM studentdetails ";
            conn = handler.getConnection();
            PreparedStatement ps = conn.prepareStatement(getData);
            ResultSet result = ps.executeQuery();
            while (result.next())
            {

                String Stud_id  = result.getString(1);
                String First_Name = result.getString(2);
                System.out.println(Stud_id + ',' +First_Name);
                List.add(new orderModel(Stud_id,First_Name));
                studtable.getItems().setAll(List);


            }

        }catch (Exception e)
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            //System.exit(0);
        }
    }

    private class orderModel {

       private final SimpleStringProperty Stud_id;
       private final SimpleStringProperty First_Name;


        public orderModel(String studid, String first_name) {

            this.Stud_id = new SimpleStringProperty(studid);
            this.First_Name = new SimpleStringProperty(first_name);
        }
    }
}
