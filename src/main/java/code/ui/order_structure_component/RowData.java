package code.ui.order_structure_component;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;


public class RowData {
    private final SimpleStringProperty columnItem;
    private final SimpleStringProperty columnPrice;
    private int rowIndex;
    private final Button buttonDelete;

    public RowData(int rowIndex, EventHandler<Event> deleteHandler) {
        this.rowIndex = rowIndex;
        columnItem = new SimpleStringProperty();
        columnPrice = new SimpleStringProperty();
        buttonDelete = new Button("X");
        buttonDelete.setOnAction(event -> {
            deleteHandler.handle(new ActionEvent(this.rowIndex, null));
        });
    }

    public String getColumnItem() {
        return columnItem.get();
    }

    public void setColumnItem(String string) {
        columnItem.set(string);
    }

    public String getColumnPrice() {
        return columnPrice.get();
    }

    public void setColumnPrice(String price) {
        columnPrice.set(price);
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public Button getColumnButtonDelete() {
        return buttonDelete;
    }
}
