package code.ui;

import code.Defines;
import code.UpdateManager;
import code.bus.BusEvent;
import code.bus.BusEventListener;
import code.bus.BusEventManager;
import code.db.OrderDB;
import code.db.OrdersJDBCTemplate;
import code.utils.LoggerManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainScreenController implements Initializable, BusEventListener {
    private final static Logger logger = LoggerManager.getLoggerInstance();
    private int gridOrdersRowCount;

    @FXML
    public GridPane gridOrders;
    @FXML
    public ScrollPane gridOrdersScrollPane;

    private Stage parentStage;
    private int previousCountInRow = 0;
    private double standardOrderCardWidth;
    private double standardOrderCardHeight;

    public void initialize(URL location, ResourceBundle resources) {
        checkForUpdates();
        calculateGridParameters();
        initGridOrders();
        BusEventManager.addListener(this, BusEvent.ORDER_UPDATED);
    }

    private void calculateGridParameters() {
        final OrderCardController standardOrderCard = new OrderCardController();
        standardOrderCardWidth = standardOrderCard.getCardWidth();
        standardOrderCardHeight = standardOrderCard.getCardHeight();
    }

    private void initGridOrders() {
        List<OrderCardController> cardList = createOrderCardsFromDB();
        gridOrdersScrollPane.widthProperty().addListener((observable, oldValue, newValue) -> gridOrdersScrollPaneWidthChanged(newValue, cardList));
        gridOrdersScrollPane.heightProperty().addListener((observable, oldValue, newValue) -> gridOrdersScrollPaneHeightChanged());
    }

    private void gridOrdersScrollPaneHeightChanged() {
        gridOrders.setPrefHeight((standardOrderCardHeight + gridOrders.getVgap()) * gridOrdersRowCount);
    }

    private void gridOrdersScrollPaneWidthChanged(Number newScrollPaneWidth, List<OrderCardController> cardList) {
        gridOrders.setPrefWidth(newScrollPaneWidth.doubleValue());
        gridOrders.setPrefHeight((standardOrderCardHeight + gridOrders.getVgap()) * gridOrdersRowCount);

        final int countInRow = (int) (newScrollPaneWidth.intValue() / standardOrderCardWidth);

        if (previousCountInRow != countInRow) {
            gridOrdersRowCount = fillGrid(gridOrders, cardList, countInRow);
            previousCountInRow = countInRow;
        }
    }

    private List<OrderCardController> createOrderCardsFromDB() {
        List<OrderDB> orderDBList = readAllOrdersFromDB();
        List<OrderCardController> orderCardList = new ArrayList<>();

        for (OrderDB orderDB : orderDBList) {
            OrderCardController orderCard = new OrderCardController();
            orderCard.init(orderDB);
            orderCard.setParentStage(parentStage);
            orderCardList.add(orderCard);
        }

        return orderCardList;
    }

    private static List<OrderDB> readAllOrdersFromDB() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);
        OrdersJDBCTemplate ordersJDBCTemplate = (OrdersJDBCTemplate) applicationContext.getBean("ordersJDBCTemplateId");
        return ordersJDBCTemplate.readAllOrders();
    }

    private static int fillGrid(GridPane gridOrders, List<OrderCardController> cardList, int countInRow) {
        int gridRowCount = 0;

        if (countInRow == 0) {
            return gridRowCount;
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

        gridRowCount = row + 1;
        return gridRowCount;
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

    @Override
    public void busEventDispatch(BusEvent event) {
        if (event == BusEvent.ORDER_UPDATED) {
            redrawGridOrders();
        }
    }

    private void redrawGridOrders() {
        List<OrderCardController> cardList = createOrderCardsFromDB();
        fillGrid(gridOrders, cardList, previousCountInRow);
    }
}
