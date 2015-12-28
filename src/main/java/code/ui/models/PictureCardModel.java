package code.ui.models;

import javafx.scene.control.CheckBox;

/**
 * Created by ixotum on 28.12.15
 */
public class PictureCardModel {
    public void initCheckBoxHandler(CheckBox checkBox, OrderComponentModel orderComponentModel) {
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            orderComponentModel.updateDeleteButtonState();
        });
    }
}
