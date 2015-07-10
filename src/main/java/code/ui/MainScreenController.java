package code.ui;

import code.Defines;
import code.UpdateManager;
import code.utils.LoggerManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainScreenController implements Initializable {
    private final static Logger logger = LoggerManager.getLoggerInstance();

    @FXML
    public GridPane gridOrders;

    private Stage parentStage;
    private int previousCountInRow = 0;

    public void initialize(URL location, ResourceBundle resources) {
        checkForUpdates();
        initGridOrders(gridOrders);
    }

    private void initGridOrders(GridPane gridOrders) {
        gridOrders.widthProperty().addListener((observable, oldValue, newValue) -> gridWidthChanged(gridOrders, newValue));
    }

    private void gridWidthChanged(GridPane gridOrders, Number newGridWidth) {
        final OrderCardController etalonOrderCard = new OrderCardController();
        final double etalonWidth = etalonOrderCard.getCardWidth();
        final int countInRow = (int) (newGridWidth.intValue() / etalonWidth);

        List<OrderCardController> cardList = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            OrderCardController orderCardController = new OrderCardController();
            cardList.add(orderCardController);
        }

        if (previousCountInRow != countInRow) {
            fillGrid(gridOrders, cardList, countInRow);
            previousCountInRow = countInRow;
        }
    }

    private static void fillGrid(GridPane gridOrders, List<OrderCardController> cardList, int countInRow) {
        if (countInRow == 0) {
            return;
        }

        gridOrders.getChildren().clear();

        int column = 0;
        int row = 0;
        for (OrderCardController orderCard : cardList) {
            gridOrders.add(orderCard, column, row);
            ++column;

            if ((column % countInRow) == 0) {
                column = 0;
                ++row;
            }
        }
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
    public void onClickNewOrder() {
        logger.info("Creating new order");
        createNewOrderScreen();
    }

    private void createNewOrderScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/new_order_screen.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.setScene(scene);
            NewOrderScreenController newOrderScreenController = fxmlLoader.getController();
            newOrderScreenController.setStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setParentStage(Stage stage) {
        parentStage = stage;
    }
}
