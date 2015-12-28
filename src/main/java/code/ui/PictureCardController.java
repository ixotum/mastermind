package code.ui;

import java.io.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Created by ixotum on 08.12.15
 */
public class PictureCardController extends VBox {
    @FXML
    private ImageView imageView;
    @FXML
    private CheckBox checkBox;

    public PictureCardController(Image image, boolean checkBoxDisabled) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/picture_card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImage(image);
        checkBox.setDisable(checkBoxDisabled);
    }
}
