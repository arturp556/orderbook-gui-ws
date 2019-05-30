package service.model;

import java.math.BigDecimal;

/**
 * Created by artur on 21.06.17.
 */
public class Order {
    private BigDecimal price;
    private BigDecimal amount;
    private OrderType orderType;

    public enum OrderType {
        BID, ASK;
    }

    public Order(BigDecimal price, BigDecimal amout) {
        this.price = price;
        this.amount = amout;
    }

    public Order(BigDecimal price, BigDecimal amout, OrderType orderType) {
        this.price = price;
        this.amount = amout;
        this.orderType = orderType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
