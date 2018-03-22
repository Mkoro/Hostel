package sample;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by user1 on 5/30/2017.
 */
public class MainController implements Initializable {
    @FXML
    private StackPane rootPane;

    @FXML
    private Pane popUpHolder;

    @FXML
    private JFXButton btnLogoff;

    @FXML
    private JFXButton btnClose;

    @FXML
    private VBox groupCompany;

    @FXML
    private AnchorPane menuCompany;

    @FXML
    private AnchorPane menuSynch;

    @FXML
    private VBox groupTraining;

    @FXML
    private AnchorPane menuTraining;

    @FXML
    private VBox groupReports;

    @FXML
    private AnchorPane menuReports;

    @FXML
    private VBox groupAbout;

    @FXML
    private JFXButton btnAccount;

    @FXML
    void close(ActionEvent event) {

    }

    @FXML
    void loggOff(ActionEvent event) {

    }

    @FXML
    void showActions(ActionEvent event) {

    }

    @FXML
    void showAdmission(MouseEvent event) {
        setStage("student.fxml");
    }

    @FXML
    void showHostel(MouseEvent event) {
        setStage("hostel.fxml");
    }

    @FXML
    void showPayment(MouseEvent event) {
        setStage("payment.fxml");
    }

    @FXML
    void showStudents(MouseEvent event) {
        setStage("stud_view.fxml");
    }

    @FXML
    void synchroniseOnline(MouseEvent event) {
        setStage("Cloud.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void setStage(String fxml) {
        try {
            //dim overlay on new stage opening
            Region veil = new Region();
            veil.setPrefSize(1100, 650);
            veil.setStyle("-fx-background-color:rgba(0,0,0,0.3)");
            Stage newStage = new Stage();
            Parent parent = FXMLLoader.load(getClass().getResource(fxml));

            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);
            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.initStyle(StageStyle.TRANSPARENT);
            newStage.getScene().getRoot().setEffect(new DropShadow());
            newStage.showingProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    rootPane.getChildren().add(veil);
                } else if (rootPane.getChildren().contains(veil)) {
                    rootPane.getChildren().removeAll(veil);
                }

            });
            newStage.centerOnScreen();
            newStage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
