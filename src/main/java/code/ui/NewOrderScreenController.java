package code.ui;

import code.utils.LoggerManager;
import javafx.event.ActionEvent;

import java.util.logging.Logger;

/**
 * Created by ixotum on 7/5/15.
 */
public class NewOrderScreenController {
    final Logger logger = LoggerManager.getLoggerInstance();

    public void onClickSaveOrderButton(ActionEvent actionEvent) {
        logger.info("Saving order number: ");
    }
}
