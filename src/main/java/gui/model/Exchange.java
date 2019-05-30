package gui.model;

/**
 * Created by artur on 24.06.17.
 */
public enum Exchange {

    BITFINEX("Bitfinex");

    private String name;

    Exchange(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
