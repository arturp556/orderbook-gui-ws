package service.impl.bitfinex.model.websocketapi;

/**
 * Created by artur on 18.06.17.
 */
public class SubscribeOrderBookRequest {

    private final String event = "subscribe";
    private final String channel = "book";
    private TradingPair pair;
    private String prec = "P0";
    private String freq = "F0";
    private int length = 50;

    public String getEvent() {
        return event;
    }

    public String getChannel() {
        return channel;
    }

    public TradingPair getPair() {
        return pair;
    }

    public void setPair(TradingPair pair) {
        this.pair = pair;
    }

    public String getPrec() {
        return prec;
    }

    public String getFreq() {
        return freq;
    }

    public int getLength() {
        return length;
    }
}
