package service;

import service.model.UpdateOrderBookCommand;

import java.util.List;

/**
 * Created by artur on 23.06.17.
 */
public interface MarketService {
    List<UpdateOrderBookCommand> handleOrderBookChanelMessage(String message);
}
