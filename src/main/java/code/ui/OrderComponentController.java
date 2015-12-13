package code.ui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import code.Defines;
import code.db.order.order_structure_component.OrderStructureComponentDB;
import code.db.order.payment_component.PaymentComponentDB;
import code.ui.order_structure_component.OrderStructureComponentController;
import code.ui.payment_component.PaymentComponentController;
import code.utils.UITools;

/**
 * Created by ixotum on 7/11/15
 */
public class OrderComponentController extends VBox implements Initializable {
    @FXML
    private Label labelOrderId;
    @FXML
    private OrderStructureComponentController orderStructureComponentController;
    @FXML
    private TextArea textAreaCustomer;
    @FXML
    private TextArea textAreaAddress;
    @FXML
    private TextField textFieldVK;
    @FXML
    private DatePicker datePickerEventDate;
    @FXML
    private TextField textFieldName;
    @FXML
    private DatePicker datePickerDueDate;
    @FXML
    private TextArea textAreaDescription;
    @FXML
    private TextArea textAreaNotes;
    @FXML
    private PaymentComponentController paymentComponentController;
    @FXML
    private ComboBox<String> comboBoxStatus;
    @FXML
    private GridPane gridThumbnails;

    public OrderComponentController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/order_component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDatePickers();
        initComboStatus();
        initGridThumbnails();
    }

    private void initGridThumbnails() {
        gridThumbnails.getChildren().clear();

        final int pictureLimit = 6;
        int allocatedPictures = 0;

        while (allocatedPictures < pictureLimit) {
            PictureCardController emptyPicture = new PictureCardController();
            int columnIndex = allocatedPictures % 3;
            int rowIndex = allocatedPictures / 3;
            gridThumbnails.add(emptyPicture, columnIndex, rowIndex);
            ++allocatedPictures;
        }
    }

    public void initOrderStructureComponentController(OrderStructureComponentDB orderStructureComponentDB) {
        orderStructureComponentController.setOrderStructureComponentDB(orderStructureComponentDB);
    }

    private void initComboStatus() {
        comboBoxStatus.setItems(FXCollections.observableArrayList(Defines.orderStatuses));
    }

    private void initDatePickers() {
        Date todayDate = new Date();
        LocalDate localDate = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        UITools.initDatePicker(datePickerDueDate);
        UITools.initDatePicker(datePickerEventDate);
        datePickerDueDate.setValue(localDate);
        datePickerEventDate.setValue(localDate);
    }

    public void initPaymentComponentController(PaymentComponentDB paymentComponentDB) {
        paymentComponentController.setPaymentComponentDB(paymentComponentDB);
    }

    public Label getLabelOrderId() {
        return labelOrderId;
    }

    public TextField getTextFieldName() {
        return textFieldName;
    }

    public TextArea getTextAreaCustomer() {
        return textAreaCustomer;
    }

    public TextArea getTextAreaAddress() {
        return textAreaAddress;
    }

    public TextField getTextFieldVK() {
        return textFieldVK;
    }

    public DatePicker getDatePickerDueDate() {
        return datePickerDueDate;
    }

    public DatePicker getDatePickerEventDate() {
        return datePickerEventDate;
    }

    public TextArea getTextAreaDescription() {
        return textAreaDescription;
    }

    public OrderStructureComponentController getOrderStructureComponentController() {
        return orderStructureComponentController;
    }

    public TextArea getTextAreaNotes() {
        return textAreaNotes;
    }

    public PaymentComponentController getPaymentComponentController() {
        return paymentComponentController;
    }

    public void setOrderStatus(int statusCode) {
        comboBoxStatus.setValue(Defines.orderStatuses.get(statusCode));
    }

    public ComboBox<String> getComboBoxStatus() {
        return comboBoxStatus;
    }
}
