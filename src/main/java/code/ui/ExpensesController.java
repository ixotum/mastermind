package code.ui;

import code.ui.models.ExpensesModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ixotum on 16.10.15
 */
public class ExpensesController implements Initializable {
    private Stage stage;
    private ExpensesModel model;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TableView table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new ExpensesModel(this);
        model.initTable();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public TableView getTable() {
        return table;
    }

    @FXML
    public void onClickCancelButton() {
        close();
    }

    public void close() {
        stage.hide();
    }
}
