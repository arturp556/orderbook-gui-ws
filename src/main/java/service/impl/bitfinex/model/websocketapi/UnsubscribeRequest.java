package service.impl.bitfinex.model.websocketapi;

/**
 * Created by artur on 19.06.17.
 */
public class UnsubscribeRequest {

    private final String event = "unsubscribe";
    private final String channel = "book";

    public String getEvent() {
        return event;
    }

    public String getChannel() {
        return channel;
    }
}
