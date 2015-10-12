package code.managers;

import code.Defines;
import code.bus.BusEvent;
import code.bus.BusEventListener;
import code.bus.BusEventManager;
import code.bus.BusEventType;
import code.db.OrderDB;
import code.db.OrdersJDBCTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ixotum on 11.10.15
 */
public class OrderManager implements BusEventListener {
    private List<OrderDB> orders = new ArrayList<>();

    public OrderManager() {
        updateOrders();
        BusEventManager.addListener(this, BusEventType.ORDER_UPDATED);
    }

    private static List<OrderDB> readAllOrdersFromDB() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);
        OrdersJDBCTemplate ordersJDBCTemplate = (OrdersJDBCTemplate) applicationContext.getBean("ordersJDBCTemplateId");
        return ordersJDBCTemplate.readAllOrders();
    }

    public List<OrderDB> getOrders() {
        return orders;
    }

    @Override
    public void busEventDispatch(BusEvent busEvent) {
        updateOrders();
    }

    private void updateOrders() {
        orders = readAllOrdersFromDB();
    }
}
