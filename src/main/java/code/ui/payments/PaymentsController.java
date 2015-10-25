package code.ui.payments;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ixotum on 25.10.15
 */
public class PaymentsController implements Initializable {
    @FXML
    private TableView table;

    private Stage stage;
    private PaymentsModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new PaymentsModel(this);
        model.initTable();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TableView getTable() {
        return table;
    }

    public void close() {
        stage.close();
    }
}
