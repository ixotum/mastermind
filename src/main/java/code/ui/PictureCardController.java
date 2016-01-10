package code.ui;

import java.io.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javafx.stage.Modality;
import javafx.stage.Stage;

import code.ui.models.OrderComponentModel;
import code.ui.models.PictureCardModel;

/**
 * Created by ixotum on 08.12.15
 */
public class PictureCardController extends VBox {
    private final Stage parentStage;
    private final String pictureName;

    @FXML
    private ImageView imageView;
    @FXML
    private CheckBox checkBox;

    public PictureCardController(Image image,
                                 boolean checkBoxDisabled,
                                 OrderComponentModel orderComponentModel,
                                 Stage stage, String pictureName) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/picture_card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PictureCardModel model = new PictureCardModel();

        imageView.setImage(image);
        checkBox.setDisable(checkBoxDisabled);

        model.initCheckBoxHandler(checkBox, orderComponentModel);
        this.parentStage = stage;
        this.pictureName = pictureName;
    }

    @FXML
    public void onPictureClick() {
        openPicture();
    }

    public boolean isSelected() {
        return checkBox.isSelected();
    }

    private void openPicture() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/picture_screen.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setTitle("Picture View");
            stage.resizableProperty().setValue(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.setScene(scene);
            PictureScreenController pictureScreenController = fxmlLoader.getController();
            pictureScreenController.init(pictureName);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
