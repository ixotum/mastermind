package code.ui;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import code.Defines;
import code.db.order.order_structure_component.OrderStructureComponentDB;
import code.db.order.payment_component.PaymentComponentDB;
import code.ui.order_structure_component.OrderStructureComponentController;
import code.ui.payment_component.PaymentComponentController;
import code.utils.UITools;
import code.ui.models.OrderComponentModel;

/**
 * Created by ixotum on 7/11/15
 */
public class OrderComponentController extends VBox implements Initializable {
    @FXML
    private Label labelOrderId;
    @FXML
    private OrderStructureComponentController orderStructureComponentController;
    @FXML
    private TextArea textAreaCustomer;
    @FXML
    private TextArea textAreaAddress;
    @FXML
    private TextField textFieldVK;
    @FXML
    private DatePicker datePickerEventDate;
    @FXML
    private TextField textFieldName;
    @FXML
    private DatePicker datePickerDueDate;
    @FXML
    private TextArea textAreaDescription;
    @FXML
    private TextArea textAreaNotes;
    @FXML
    private PaymentComponentController paymentComponentController;
    @FXML
    private ComboBox<String> comboBoxStatus;
    @FXML
    private GridPane gridThumbnails;

    private Stage stage;
    private List<String> thumbnailNames = new ArrayList<>();

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDatePickers();
        initComboStatus();
        updateGridThumbnails();
    }

    @FXML
    public void onAddPicture() {
        File imageFile = openPictureDialog();

        if (imageFile == null) {
            return;
        }

        String thumbnailName = OrderComponentModel.processImage(imageFile);
        thumbnailNames.add(thumbnailName);
        updateGridThumbnails();
    }

    private File openPictureDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Picture");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extensionFilter);

        return fileChooser.showOpenDialog(stage);
    }

    private void updateGridThumbnails() {
        gridThumbnails.getChildren().clear();

        final int pictureLimit = 6;
        int imageIndex = 0;
        int thumbnailsCount = thumbnailNames.size();

        while (imageIndex < pictureLimit) {
            Image image = null;

            if (imageIndex < thumbnailsCount) {
                String thumbnailName = thumbnailNames.get(imageIndex);
                String fileName = UITools.findFile(thumbnailName);

                try {
                    InputStream inputStream = new FileInputStream(fileName);
                    image = new Image(inputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                image = new Image("/images/empty.png");
            }

            if (image == null) {
                continue;
            }

            PictureCardController pictureCardController = new PictureCardController(image);
            int columnIndex = imageIndex % 3;
            int rowIndex = imageIndex / 3;
            gridThumbnails.add(pictureCardController, columnIndex, rowIndex);
            ++imageIndex;
        }
    }

    public void initOrderStructureComponentController(OrderStructureComponentDB orderStructureComponentDB) {
        orderStructureComponentController.setOrderStructureComponentDB(orderStructureComponentDB);
    }

    private void initComboStatus() {
        comboBoxStatus.setItems(FXCollections.observableArrayList(Defines.orderStatuses));
    }

    private void initDatePickers() {
        Date todayDate = new Date();
        LocalDate localDate = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        UITools.initDatePicker(datePickerDueDate);
        UITools.initDatePicker(datePickerEventDate);
        datePickerDueDate.setValue(localDate);
        datePickerEventDate.setValue(localDate);
    }

    public void initPaymentComponentController(PaymentComponentDB paymentComponentDB) {
        paymentComponentController.setPaymentComponentDB(paymentComponentDB);
    }

    public Label getLabelOrderId() {
        return labelOrderId;
    }

    public TextField getTextFieldName() {
        return textFieldName;
    }

    public TextArea getTextAreaCustomer() {
        return textAreaCustomer;
    }

    public TextArea getTextAreaAddress() {
        return textAreaAddress;
    }

    public TextField getTextFieldVK() {
        return textFieldVK;
    }

    public DatePicker getDatePickerDueDate() {
        return datePickerDueDate;
    }

    public DatePicker getDatePickerEventDate() {
        return datePickerEventDate;
    }

    public TextArea getTextAreaDescription() {
        return textAreaDescription;
    }

    public OrderStructureComponentController getOrderStructureComponentController() {
        return orderStructureComponentController;
    }

    public TextArea getTextAreaNotes() {
        return textAreaNotes;
    }

    public PaymentComponentController getPaymentComponentController() {
        return paymentComponentController;
    }

    public void setOrderStatus(int statusCode) {
        comboBoxStatus.setValue(Defines.orderStatuses.get(statusCode));
    }

    public ComboBox<String> getComboBoxStatus() {
        return comboBoxStatus;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
