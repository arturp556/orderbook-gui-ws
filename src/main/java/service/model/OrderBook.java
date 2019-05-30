package service.model;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by artur on 23.06.17.
 */
public class OrderBook {

    private SortedMap<BigDecimal, Order> bids = new TreeMap<>(Comparator.reverseOrder());
    private SortedMap<BigDecimal, Order> asks = new TreeMap<>();

    public void processUpdate(UpdateOrderBookCommand update) {

        //TODO create command classes implementing abstract execute method
        switch (update.getCommandType()) {
            case PLACE_OR_UPDATE_ORDER:
                Order newOrder = new Order(update.getPrice(), update.getAmount(), update.getOrderType());
                Map<BigDecimal, Order> orders = getOrdersToUpdate(update.getOrderType());
                orders.put(newOrder.getPrice(), newOrder);

                break;
            case DELETE_ORDER_WITH_EQUAL_PRICE:
                orders = getOrdersToUpdate(update.getOrderType());
                orders.remove(update.getPrice());
                break;
        }
    }

    private Map<BigDecimal, Order> getOrdersToUpdate(Order.OrderType orderType) {

        Map<BigDecimal, Order> target = null;
        switch (orderType) {
            case BID:
                target = this.bids;
                break;
            case ASK:
                target = this.asks;
                break;
            default:
        }
        return target;
    }

    public Collection<Order> getBids() {
        return Collections.unmodifiableCollection(this.bids.values()) ;
    }

    public Collection<Order> getAsks() {
        return Collections.unmodifiableCollection(this.asks.values()) ;
    }
}
