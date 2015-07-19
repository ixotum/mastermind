package code.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    public Label labelOrderId;
    @FXML
    public TextArea textAreaStructure;
    @FXML
    public TextArea textAreaCustomer;
    @FXML
    private TextField textFieldName;

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
}
