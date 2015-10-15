package code.ui.models;

import code.Defines;
import code.bus.BusEvent;
import code.bus.BusEventListener;
import code.bus.BusEventManager;
import code.bus.BusEventType;
import code.db.OrderDB;
import code.db.OrdersJDBCTemplate;
import code.db.SettingsJDBCTemplate;
import code.db.order_structure_component.OrderStructureComponentDB;
import code.db.payment_component.PaymentComponentDB;
import code.ui.NewOrderScreenController;
import code.ui.OrderComponentController;
import code.ui.order_structure_component.OrderStructureComponentController;
import code.ui.payment_component.PaymentComponentController;
import code.utils.LoggerManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Date;
import java.time.LocalDate;
import java.util.logging.Logger;

public class NewOrderScreenModel implements BusEventListener {
    private final Logger logger = LoggerManager.getLoggerInstance();
    private final NewOrderScreenController controller;
    private SettingsJDBCTemplate settingsJDBCTemplate;
    private final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);

    public NewOrderScreenModel(NewOrderScreenController newOrderScreenController) {
        this.controller = newOrderScreenController;
    }

    public OrderDB createOrderDB(int orderId) {
        OrderComponentController orderComponent = controller.getOrderComponent();
        OrderDB orderDB = new OrderDB(orderId);
        orderDB.setName(orderComponent.getTextFieldName().getText());

        OrderStructureComponentController orderStructureComponentController = orderComponent.getOrderStructureComponentController();
        OrderStructureComponentDB orderStructureComponentDB = OrderComponentModel.createOrderStructureComponentDB(orderStructureComponentController);
        orderDB.setOrderStructureComponentDB(orderStructureComponentDB);

        orderDB.setCustomer(orderComponent.getTextAreaCustomer().getText());
        orderDB.setAddress(orderComponent.getTextAreaAddress().getText());
        orderDB.setVK(orderComponent.getTextFieldVK().getText());
        LocalDate localDueDate = orderComponent.getDatePickerDueDate().getValue();
        orderDB.setDueDate(Date.valueOf(localDueDate));
        LocalDate localEventDate = orderComponent.getDatePickerEventDate().getValue();
        orderDB.setEventDate(Date.valueOf(localEventDate));
        orderDB.setDescription(orderComponent.getTextAreaDescription().getText());
        orderDB.setNotes(orderComponent.getTextAreaNotes().getText());

        PaymentComponentController paymentComponentController = orderComponent.getPaymentComponentController();
        PaymentComponentDB paymentComponentDB = OrderComponentModel.createPaymentComponentDB(paymentComponentController);
        orderDB.setPaymentComponentDB(paymentComponentDB);

        int orderStatus = OrderComponentModel.getOrderStatus(orderComponent.getComboBoxStatus().getValue());
        orderDB.setStatus(orderStatus);

        return orderDB;
    }

    public void saveOrderComponent() {
        OrderComponentController orderComponent = controller.getOrderComponent();
        int orderId = Integer.parseInt(orderComponent.getLabelOrderId().getText());
        logger.info("Saving order with number: " + orderId);
        settingsJDBCTemplate.saveLastOrderId(orderId);

        OrdersJDBCTemplate ordersJDBCTemplate = (OrdersJDBCTemplate) applicationContext.getBean("ordersJDBCTemplateId");
        OrderDB orderDB = createOrderDB(orderId);
        ordersJDBCTemplate.saveNewOrder(orderDB);

        BusEvent busEvent = new BusEvent(BusEventType.ORDER_UPDATED, null);
        BusEventManager.dispatch(busEvent);
    }

    public void initOrderId() {
        settingsJDBCTemplate = (SettingsJDBCTemplate) applicationContext.getBean("settingsJDBCTemplateId");
        int lastOrderId = settingsJDBCTemplate.readLastOrderId();
        ++lastOrderId;
        OrderComponentController orderComponent = controller.getOrderComponent();
        orderComponent.getLabelOrderId().setText(String.valueOf(lastOrderId));
    }

    public void initListener() {
        BusEventManager.addListener(this, BusEventType.ESC_PRESSED);
    }

    @Override
    public void busEventDispatched(BusEvent busEvent) {
        BusEventType busEventType = busEvent.getType();
        if (busEventType == BusEventType.ESC_PRESSED) {
            controller.hide();
        }
    }
}
