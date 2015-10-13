package code.ui.models;

import code.Defines;
import code.Main;
import code.db.OrderDB;
import code.managers.OrderManager;
import code.managers.UpdateManager;
import code.ui.MainScreenController;
import code.ui.OrderCardController;
import code.utils.LoggerManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalDatePicker;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by ixotum on 13.10.15
 */
public class MainScreenModel {
    private final static Logger logger = LoggerManager.getLoggerInstance();
    private final MainScreenController controller;

    private int previousCountInRow = 0;
    private int gridOrdersRowCount;

    private double standardOrderCardWidth;
    private double standardOrderCardHeight;

    public MainScreenModel(MainScreenController controller) {
        this.controller = controller;
    }

    public static void initCalendar(LocalDatePicker calendar) {
        List<Date> dates = extractDates();
        highlightDates(dates, calendar);
    }

    private static void highlightDates(List<Date> dates, LocalDatePicker calendar) {
        List<LocalDate> localDates = dates.stream().map(MainScreenModel::convertToLocalDate).collect(Collectors.toList());
        ObservableList<LocalDate> obsLocalDates = FXCollections.observableArrayList(localDates);
        calendar.highlightedLocalDates().setAll(obsLocalDates);
    }

    private static LocalDate convertToLocalDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return LocalDate.of(calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH));
    }

    private static List<Date> extractDates() {
        OrderManager orderManager = Main.getOrderManager();
        List<OrderDB> orderDBList = orderManager.getOrders();
        List<Date> dates = new ArrayList<>();

        for (OrderDB orderDB : orderDBList) {
            Date date = orderDB.getDueDate();
            saveDate(date, dates);
            date = orderDB.getEventDate();
            saveDate(date, dates);
        }

        return dates;
    }

    private static void saveDate(Date date, List<Date> dates) {
        if (!dates.contains(date)) {
            dates.add(date);
        }
    }

    public static void checkForUpdates() {
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

    public void initGridOrders() {
        calculateGridParameters();

        List<OrderCardController> cardList = createOrderCards();
        sortByOrderId(cardList);

        ScrollPane gridOrdersScrollPane = controller.getScrollPane();
        gridOrdersScrollPane.widthProperty().addListener((observable, oldValue, newValue) -> gridOrdersScrollPaneWidthChanged(newValue, cardList));
        gridOrdersScrollPane.heightProperty().addListener((observable, oldValue, newValue) -> gridOrdersScrollPaneHeightChanged());
    }

    private void calculateGridParameters() {
        final OrderCardController standardOrderCard = new OrderCardController();
        standardOrderCardWidth = standardOrderCard.getCardWidth();
        standardOrderCardHeight = standardOrderCard.getCardHeight();
    }

    private List<OrderCardController> createOrderCards() {
        OrderManager orderManager = Main.getOrderManager();
        List<OrderDB> orderDBList = orderManager.getOrders();
        List<OrderCardController> orderCardList = new ArrayList<>();
        Stage parentStage = controller.getParentStage();

        for (OrderDB orderDB : orderDBList) {
            OrderCardController orderCard = new OrderCardController();
            orderCard.init(orderDB);
            orderCard.setParentStage(parentStage);
            orderCardList.add(orderCard);
        }

        return orderCardList;
    }

    private static void sortByOrderId(List<OrderCardController> cardList) {
        cardList.sort(MainScreenModel::orderIdComparator);
    }

    private static int orderIdComparator(OrderCardController o1, OrderCardController o2) {
        if (o1.getOrderDB().getOrderId() > o2.getOrderDB().getOrderId()) {
            return 1;
        }
        return -1;
    }

    private void gridOrdersScrollPaneHeightChanged() {
        GridPane gridOrders = controller.getGridPane();
        gridOrders.setPrefHeight((standardOrderCardHeight + gridOrders.getVgap()) * gridOrdersRowCount);
    }

    private void gridOrdersScrollPaneWidthChanged(Number newScrollPaneWidth, List<OrderCardController> cardList) {
        GridPane gridOrders = controller.getGridPane();
        gridOrders.setPrefWidth(newScrollPaneWidth.doubleValue());
        gridOrders.setPrefHeight((standardOrderCardHeight + gridOrders.getVgap()) * gridOrdersRowCount);

        final int countInRow = (int) (newScrollPaneWidth.intValue() / standardOrderCardWidth);

        if (previousCountInRow != countInRow) {
            gridOrdersRowCount = fillGrid(gridOrders, cardList, countInRow);
            previousCountInRow = countInRow;
        }
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

    public void redrawGridOrders() {
        List<OrderCardController> cardList = createOrderCards();
        sortByOrderId(cardList);
        GridPane gridOrders = controller.getGridPane();
        fillGrid(gridOrders, cardList, previousCountInRow);
    }
}
