package code.ui;

import code.Defines;
import code.UpdateManager;
import code.utils.LoggerManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainScreenController implements Initializable {
    private final Logger logger = LoggerManager.getLoggerInstance();
    private Stage parentStage;

    public void initialize(URL location, ResourceBundle resources) {
        checkForUpdates();
    }

    private void checkForUpdates() {
        logger.info("checking for updates...");
        UpdateManager updateManager = new UpdateManager();
        if (updateManager.isNewVersionAvailable()) {
            logger.info("new version was found");
            showUpdateQuestionPopup(updateManager);
        } else {
            logger.info("new version not found");
        }
    }

    private static void showUpdateQuestionPopup(UpdateManager updateManager) {
        Alert questionDialog = new Alert(Alert.AlertType.CONFIRMATION);
        questionDialog.setTitle(Defines.UPDATE_APP_TITLE);
        questionDialog.setHeaderText(Defines.UPDATE_HEADER_TEXT);
        String contentText = "Current version is: " + Defines.APP_VERSION +
                "\nAvailable version is: " + updateManager.getNewVersion() + "\n" +
                Defines.UPDATE_CONTENT_TEXT;
        questionDialog.setContentText(contentText);
        Optional<ButtonType> result = questionDialog.showAndWait();

        if (result.get() == ButtonType.OK) {
            updateManager.updateToNewest();
        }
    }

    @FXML
    public void onClickNewOrder(ActionEvent actionEvent) {
        logger.info("Creating new order");
        createNewOrderScreen();
    }

    private void createNewOrderScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/new_order_screen.fxml"));
        Pane pane = null;
        try {
            pane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
        stage.setScene(scene);
        stage.show();
    }

    public void setParentStage(Stage stage) {
        parentStage = stage;
    }
}
