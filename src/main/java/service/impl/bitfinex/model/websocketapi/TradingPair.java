package service.impl.bitfinex.model.websocketapi;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by artur on 23.06.17.
 */
public enum TradingPair {
    BTCUSD("BTC/USD", "BTCUSD"), LTCUSD("LTC/USD", "LTCUSD"), LTCBTC("LTC/BTC", "LTCBTC"), ETHUSD("ETH/USD", "ETHUSD");

    private String name;
    private String apiField;

    TradingPair(String name, String apiField) {
        this.name = name;
        this.apiField = apiField;
    }

    @JsonValue
    public String getApiField() {
        return apiField;
    }

    @Override
    public String toString() {
        return name;
    }
}
