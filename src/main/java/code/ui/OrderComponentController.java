package code.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by ixotum on 7/11/15
 */
public class OrderComponentController extends VBox {
    @FXML
    private Label labelOrderId;
    @FXML
    private TextArea textAreaStructure;
    @FXML
    private TextField textFieldPrice;
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
}
