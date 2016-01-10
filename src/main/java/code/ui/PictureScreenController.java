package code.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import code.utils.UITools;

/**
 * Created by ixotum on 10.01.16
 */
public class PictureScreenController {
    @FXML
    private ImageView pictureImageView;

    public void init(String pictureName) {
        String fileName = UITools.findFile(pictureName);
        try {
            InputStream inputStream = new FileInputStream(fileName);
            pictureImageView.setImage(new Image(inputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
