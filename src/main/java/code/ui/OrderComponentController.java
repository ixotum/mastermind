package code.ui;

import code.db.order_structure_component.OrderComponentModel;
import code.db.order_structure_component.OrderStructureComponentDB;
import code.ui.order_structure_component.OrderStructureComponentController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by ixotum on 7/11/15
 */
public class OrderComponentController extends VBox implements Initializable {
    @FXML
    private Label labelOrderId;
    @FXML
    private TextArea textAreaStructure;
    @FXML
    private TextField textFieldPrice;
    @FXML
    private OrderStructureComponentController orderStructureComponentController;
    @FXML
    private TextArea textAreaCustomer;
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

    public Label getLabelOrderId() {
        return labelOrderId;
    }

    public TextField getTextFieldName() {
        return textFieldName;
    }

    public TextArea getTextAreaStructure() {
        return textAreaStructure;
    }

    public TextArea getTextAreaCustomer() {
        return textAreaCustomer;
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

    public TextField getTextFieldPrice() {
        return textFieldPrice;
    }

    public void initOrderStructureComponentController(OrderStructureComponentDB orderStructureComponentDB) {
        orderStructureComponentController.setOrderStructureComponentDB(orderStructureComponentDB);
    }

    public OrderStructureComponentController getOrderStructureComponentController() {
        return orderStructureComponentController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Date todayDate = new Date();
        LocalDate localDate = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        datePickerDueDate.setValue(localDate);
        datePickerEventDate.setValue(localDate);
    }

    public static OrderStructureComponentDB createOrderStructureComponentDB(OrderStructureComponentController orderStructureComponentController) {
        return OrderComponentModel.createOrderStructureComponentDB(orderStructureComponentController);
    }
}
