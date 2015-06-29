package code;

import code.utils.LoggerManager;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainScreenController implements Initializable {
    private final Logger logger = LoggerManager.getLogger();

    public void initialize(URL location, ResourceBundle resources) {
        checkForUpdates();
    }

    private void checkForUpdates() {
        logger.info("checking for updates...");
    }
}
