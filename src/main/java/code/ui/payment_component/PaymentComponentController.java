package code.ui.payment_component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PaymentComponentController extends AnchorPane implements Initializable {
    private final PaymentComponentModel model;

    @FXML
    private TableView<PaymentRowData> table;
    @FXML
    private TableColumn<PaymentRowData, AnchorPane> columnDate;
    @FXML
    private TableColumn<PaymentRowData, String> columnPayment;

    public PaymentComponentController() {
        model = new PaymentComponentModel(this);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/payment_component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onButtonAdd() {
        addRow(table);
    }

    private static void addRow(TableView<PaymentRowData> table) {
        ObservableList<PaymentRowData> observableList = table.getItems();
        table.getItems().add(new PaymentRowData());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
    }

    private void initTable() {
        ObservableList<PaymentRowData> observableList = FXCollections.observableArrayList();
        table.setItems(observableList);

        initColumns();
    }

    private void initColumns() {
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }
}
