package code.ui.models;

import java.io.File;
import java.sql.Date;

import javafx.stage.FileChooser;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import code.Defines;
import code.bus.BusEvent;
import code.bus.BusEventListener;
import code.bus.BusEventManager;
import code.bus.BusEventType;
import code.db.order.OrderDB;
import code.db.order.OrdersJDBCTemplate;
import code.db.order.order_structure_component.OrderStructureComponentDB;
import code.db.order.payment_component.PaymentComponentDB;
import code.ui.EditOrderScreenController;
import code.ui.OrderComponentController;
import code.ui.order_structure_component.OrderStructureComponentController;
import code.ui.payment_component.PaymentComponentController;

/**
 * Created by ixotum on 15.10.15
 */
public class EditOrderScreenModel implements BusEventListener {
    private final EditOrderScreenController controller;
    private OrderDB orderDB;
    private boolean orderChanging = false;

    public EditOrderScreenModel(EditOrderScreenController editOrderScreenController) {
        this.controller = editOrderScreenController;
    }

    public void initListeners() {
        BusEventManager.addListener(this, BusEventType.ESC_PRESSED);
    }

    public void init(OrderDB orderDB) {
        this.orderDB = orderDB;
        OrderComponentController orderComponent = controller.getOrderComponent();
        orderComponent.getLabelOrderId().setText(String.valueOf(orderDB.getOrderId()));
        orderComponent.getTextFieldName().setText(orderDB.getName());
        orderComponent.initOrderStructureComponentController(orderDB.getOrderStructureComponentDB());
        orderComponent.getTextAreaCustomer().setText(orderDB.getCustomer());
        orderComponent.getTextAreaAddress().setText(orderDB.getAddress());
        orderComponent.getTextFieldVK().setText(orderDB.getVk());

        Date dueDate = orderDB.getDueDate();
        if (dueDate != null) {
            orderComponent.getDatePickerDueDate().setValue(dueDate.toLocalDate());
        }

        Date eventDate = orderDB.getEventDate();
        if (eventDate != null) {
            orderComponent.getDatePickerEventDate().setValue(eventDate.toLocalDate());
        }

        orderComponent.getTextAreaDescription().setText(orderDB.getDescription());
        orderComponent.getTextAreaNotes().setText(orderDB.getNotes());
        orderComponent.initPaymentComponentController(orderDB.getPaymentComponentDB());
        orderComponent.setOrderStatus(orderDB.getStatus());

        orderComponent.initThumbnails(orderDB.getThumbnailNames());

        initChangeHandlers();
    }

    private void initChangeHandlers() {
        OrderComponentController orderComponent = controller.getOrderComponent();
        orderComponent.getTextFieldName().textProperty().addListener(observable -> orderChanging());
        orderComponent.getTextAreaCustomer().textProperty().addListener(observable -> orderChanging());
        orderComponent.getTextAreaAddress().textProperty().addListener(observable -> orderChanging());
        orderComponent.getTextFieldVK().textProperty().addListener(observable -> orderChanging());
        orderComponent.getDatePickerDueDate().valueProperty().addListener(observable -> orderChanging());
        orderComponent.getDatePickerEventDate().valueProperty().addListener(observable -> orderChanging());
        orderComponent.getTextAreaDescription().textProperty().addListener(observable -> orderChanging());
        orderComponent.getTextAreaNotes().textProperty().addListener(observable -> orderChanging());
        orderComponent.getComboBoxStatus().valueProperty().addListener(observable -> orderChanging());
    }

    private void orderChanging() {
        if (!orderChanging) {
            orderChanging = true;
            controller.setTitleAsterisk();
        }
    }

    @Override
    public void busEventDispatched(BusEvent busEvent) {
        BusEventType busEventType = busEvent.getType();
        if (busEventType == BusEventType.ESC_PRESSED) {
            controller.askAction();
        }
    }

    public void updateOrder() {
        OrderComponentController orderComponent = controller.getOrderComponent();
        updateOrderDB(orderComponent, orderDB);
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);
        OrdersJDBCTemplate ordersJDBCTemplate = (OrdersJDBCTemplate) applicationContext.getBean("ordersJDBCTemplateId");
        ordersJDBCTemplate.updateExistedOrder(orderDB);

        BusEvent busEvent = new BusEvent(BusEventType.ORDER_UPDATED, null);
        BusEventManager.dispatch(busEvent);
    }

    private static void updateOrderDB(OrderComponentController orderComponent, OrderDB orderDB) {
        orderDB.setName(orderComponent.getTextFieldName().getText());

        OrderStructureComponentController orderStructureComponentController = orderComponent.getOrderStructureComponentController();
        OrderStructureComponentDB orderStructureComponentDB = OrderComponentModel.createOrderStructureComponentDB(orderStructureComponentController);
        orderDB.setOrderStructureComponentDB(orderStructureComponentDB);

        orderDB.setCustomer(orderComponent.getTextAreaCustomer().getText());
        orderDB.setAddress(orderComponent.getTextAreaAddress().getText());
        orderDB.setVK(orderComponent.getTextFieldVK().getText());
        orderDB.setDueDate(Date.valueOf(orderComponent.getDatePickerDueDate().getValue()));
        orderDB.setEventDate(Date.valueOf(orderComponent.getDatePickerEventDate().getValue()));
        orderDB.setDescription(orderComponent.getTextAreaDescription().getText());
        orderDB.setNotes(orderComponent.getTextAreaNotes().getText());

        PaymentComponentController paymentComponentController = orderComponent.getPaymentComponentController();
        PaymentComponentDB paymentComponentDB = OrderComponentModel.createPaymentComponentDB(paymentComponentController);
        orderDB.setPaymentComponentDB(paymentComponentDB);

        int orderStatus = OrderComponentModel.getOrderStatus(orderComponent.getComboBoxStatus().getValue());
        orderDB.setStatus(orderStatus);
    }

    public boolean isChanging() {
        return orderChanging;
    }

    public void setChanging(boolean changing) {
        this.orderChanging = changing;
    }

    public void exportOrder() {
        FileChooser saveFileDialog = new FileChooser();
        saveFileDialog.setTitle("Export order");
        String fileName = combineName(controller);
        saveFileDialog.setInitialFileName(fileName);
        File file = saveFileDialog.showSaveDialog(controller.getStage());
        if (file != null) {
            OrderComponentModel.exportOrder(controller.getOrderComponent(), file);
        }
    }

    private static String combineName(EditOrderScreenController controller) {
        OrderComponentController orderComponent = controller.getOrderComponent();
        return "order_" + orderComponent.getLabelOrderId().getText() + "_" + orderComponent.getTextFieldName().getText() + ".txt";
    }
}
