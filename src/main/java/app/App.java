package app;

import gui.OrderBookGUI;

/**
 * Created by artur on 19.06.17.
 */
public class App {

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            OrderBookGUI orderBookGUI = new OrderBookGUI();
            orderBookGUI.createAndShowGUI();
        });
    }
}
