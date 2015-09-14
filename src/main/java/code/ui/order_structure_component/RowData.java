package code.ui.order_structure_component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class RowData {
    private final SimpleStringProperty columnItem;
    private final SimpleStringProperty columnPrice;

    public RowData() {
        columnItem = new SimpleStringProperty("");
        columnPrice = new SimpleStringProperty("0.0");
    }

    public void setColumnItem(String value) {
        columnItem.set(value);
    }

    public String getColumnItem() {
        return columnItem.get();
    }

    public SimpleStringProperty columnItemProperty() {
        return columnItem;
    }

    public void setColumnPrice(String value) {
        columnPrice.set(value);
    }

    public String getColumnPrice() {
        return columnPrice.get();
    }

    public SimpleStringProperty columnPriceProperty() {
        return columnPrice;
    }
}
