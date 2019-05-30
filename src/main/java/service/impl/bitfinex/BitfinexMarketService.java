package service.impl.bitfinex;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.MarketService;
import service.model.Order;
import service.model.UpdateOrderBookCommand;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by artur on 21.06.17.
 */
public class BitfinexMarketService implements MarketService {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<UpdateOrderBookCommand> handleOrderBookChanelMessage(String message) {

        List<UpdateOrderBookCommand> results = new ArrayList<>();

        try {

            JsonNode jsonNode = objectMapper.readTree(message);

            if (isSnapchotMessage(jsonNode)) {
                jsonNode.get(1).forEach(jsonNode1 -> {
                    UpdateOrderBookCommand command = parseSnapshotOrder(jsonNode1);
                    results.add(command);
                });
            }

            else if (isHearbeatMessage(jsonNode)) {
                //TODO handle heartbeat messages
            }

            else if (isUpdateMessage(jsonNode)) {
                UpdateOrderBookCommand command = parseOrderUpdate(jsonNode);
                results.add(command);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return results;
    }

    private boolean isUpdateMessage(JsonNode jsonNode) {
        return jsonNode.size() == 4 && jsonNode.get(1).isNumber();
    }

    private boolean isHearbeatMessage(JsonNode jsonNode) {
        return jsonNode.size() == 2 && jsonNode.isArray() && jsonNode.get(1).asText().equals("hb");
    }

    private boolean isSnapchotMessage(JsonNode jsonNode) {
        return jsonNode.size() == 2 && jsonNode.isArray() && jsonNode.get(1).isArray();
    }

    private static UpdateOrderBookCommand parseSnapshotOrder(JsonNode jsonNode) {
        double price = jsonNode.get(0).asDouble();
        double count =  jsonNode.get(1).asDouble();
        double amount = jsonNode.get(2).asDouble();

        UpdateOrderBookCommand command = new UpdateOrderBookCommand();

        if (amount < 0) {
            command.setOrderType(Order.OrderType.ASK);
            amount *= -1;
        } else {
            command.setOrderType(Order.OrderType.BID);
        }

        command.setPrice(new BigDecimal(price));
        command.setAmount(new BigDecimal(amount));
        command.setCommandType(UpdateOrderBookCommand.UpdateOrderBookCommandType.PLACE_OR_UPDATE_ORDER);
        return command;
    }

    private static UpdateOrderBookCommand parseOrderUpdate(JsonNode jsonNode) {
        double price = jsonNode.get(1).asDouble();
        double count =  jsonNode.get(2).asDouble();
        double amount = jsonNode.get(3).asDouble();

        UpdateOrderBookCommand command = new UpdateOrderBookCommand();

        if (amount < 0) {
            command.setOrderType(Order.OrderType.ASK);
            amount *= -1;
        } else {
            command.setOrderType(Order.OrderType.BID);
        }

        command.setPrice(new BigDecimal(price));
        command.setAmount(new BigDecimal(amount));

        if (count == 0) {
            command.setCommandType(UpdateOrderBookCommand.UpdateOrderBookCommandType.DELETE_ORDER_WITH_EQUAL_PRICE);
        } else {
            command.setCommandType(UpdateOrderBookCommand.UpdateOrderBookCommandType.PLACE_OR_UPDATE_ORDER);
        }
        return command;
    }

    /*
    public void testOrderBookWSconnection() {

        // open websocket
        String url = "wss://api.bitfinex.com/ws";
        WebsocketClientEndpoint clientEndPoint = null;
        try {
            clientEndPoint = new WebsocketClientEndpoint(new URI(url));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // add listener
        clientEndPoint.addMessageHandler(message -> {
            handleOrderBookChanelMessage(message);
        });

        clientEndPoint.sendMessage("{\"event\": \"ping\"}");

        SubscribeOrderBookRequest subscribeOrderBookRequest = new SubscribeOrderBookRequest();
        clientEndPoint.sendAsJson(subscribeOrderBookRequest);

        try {Thread.sleep(5000);} catch (InterruptedException e) {}

        UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest();
        clientEndPoint.sendAsJson(unsubscribeRequest);

    }
    */
}
