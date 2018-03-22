package sample;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user1 on 5/31/2017.
 */
public class onlinebackup implements Initializable {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView imageviewLoader;

    @FXML
    private Text txtStatus;

    @FXML
    private MaterialDesignIconView iconClose;


    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtStatus.setText("Synching ... ");
        PauseTransition pauseTransition = new PauseTransition(javafx.util.Duration.seconds(4));
        pauseTransition.setOnFinished((ActionEvent event) -> {
            txtStatus.setText("Synch completed.");
            imageviewLoader.setImage(new Image("images/success.png"));
        });
        pauseTransition.play();

    }

    @FXML
    private void closeStage(MouseEvent event)
    {
        txtStatus.getScene().getWindow().hide();
    }
}
