package code.ui;

import code.db.order_structure_component.OrderStructureComponentDB;
import code.ui.order_structure_component.OrderStructureComponentController;
import code.ui.order_structure_component_old.OrderStructureComponentController_old;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by ixotum on 7/11/15
 */
public class OrderComponentController extends VBox implements Initializable {
    @FXML
    private Label labelOrderId;
    @FXML
    private OrderStructureComponentController_old orderStructureComponentControllerOld;
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

    @Deprecated
    public void initOrderStructureComponentController_old(OrderStructureComponentDB orderStructureComponentDBOld) {
        orderStructureComponentControllerOld.setOrderStructureComponentDB(orderStructureComponentDBOld);
    }

    @Deprecated
    public OrderStructureComponentController_old getOrderStructureComponentControllerOld() {
        return orderStructureComponentControllerOld;
    }

    public void initOrderStructureComponentController(OrderStructureComponentDB orderStructureComponentDB) {
        orderStructureComponentController.setOrderStructureComponentDB(orderStructureComponentDB);
    }

    public OrderStructureComponentController getOrderStructureComponentController() {
        return orderStructureComponentController;
    }

    public TextArea getTextAreaNotes() {
        return textAreaNotes;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Date todayDate = new Date();
        LocalDate localDate = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        initDatePicker(datePickerDueDate);
        initDatePicker(datePickerEventDate);
        datePickerDueDate.setValue(localDate);
        datePickerEventDate.setValue(localDate);
    }

    private static void initDatePicker(DatePicker datePicker) {
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            String pattern = "dd/MM/yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        datePicker.setConverter(converter);
    }
}
