package service.model;

import java.math.BigDecimal;

/**
 * Created by artur on 21.06.17.
 */
public class UpdateOrderBookCommand {
    private BigDecimal price;
    private BigDecimal amount;
    private Order.OrderType orderType;
    private UpdateOrderBookCommandType commandType;

    public static enum UpdateOrderBookCommandType {
        PLACE_OR_UPDATE_ORDER, DELETE_ORDER_WITH_EQUAL_PRICE;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UpdateOrderBookCommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(UpdateOrderBookCommandType commandType) {
        this.commandType = commandType;
    }

    public Order.OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(Order.OrderType orderType) {
        this.orderType = orderType;
    }
}
