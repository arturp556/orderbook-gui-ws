package gui;

//import javax.swing.*;
//import java.awt.*;
import service.impl.bitfinex.BitfinexMarketService;
import service.impl.bitfinex.model.websocketapi.TradingPair;
import gui.model.Exchange;
import gui.model.OrderDTO;
import service.MarketService;
import service.model.Order;
import service.model.OrderBook;
import service.model.UpdateOrderBookCommand;
import service.impl.bitfinex.model.websocketapi.SubscribeOrderBookRequest;
import websocket.WebsocketClientEndpoint;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;

/**
 * Created by artur on 20.06.17.
 */
public class OrderBookGUI {

    // Constants
    private static final String frameTitle = "Real-time order book";
    private static final String[] bidsColumnNames = {"Amount", "Total", "Price"};
    private static final String[] asksColumnNames = {"Price", "Total", "Amount"};
    private static final String exchangeLabel = "Exchange";
    private static final String tradingPairLabel = "Trading pair";
    private static final int tableHeight = 26;
    private static final int tableWidth = 3;

    // GUI Compomnents
    private JFrame mainFrame = null;
    private Object[][] bidsTableData = new Object[tableHeight][tableWidth];
    private Object[][] asksTableData = new Object[tableHeight][tableWidth];
    private JTable bidsTable = new JTable(bidsTableData, bidsColumnNames);
    private JTable asksTable = new JTable(asksTableData, asksColumnNames);
    private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, bidsTable, asksTable);
    private JScrollPane scrollPane = new JScrollPane(splitPane);
    private JPanel upperPanel = new JPanel();
    private JComboBox<TradingPair> tradingPairComboBox = new JComboBox<>(TradingPair.values());
    private JComboBox<Exchange> exchangeComboBox = new JComboBox(Exchange.values());

    public OrderBookGUI() {

    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void createAndShowGUI() {
        mainFrame = new JFrame(frameTitle);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        upperPanel.add(new JLabel(exchangeLabel));
        upperPanel.add(exchangeComboBox);
        upperPanel.add(new JLabel(tradingPairLabel));
        upperPanel.add(tradingPairComboBox);

        mainFrame.add(scrollPane, BorderLayout.CENTER);
        mainFrame.add(upperPanel, BorderLayout.PAGE_START);

        for (int i = 0; i < bidsColumnNames.length; i++) {
            bidsTableData[0][i] = bidsColumnNames[i];
            asksTableData[0][i] = asksColumnNames[i];
        }

        // set a table non-editable
        bidsTable.setEnabled(false);
        asksTable.setEnabled(false);

        //Display the window.
        mainFrame.pack();
        mainFrame.setVisible(true);

        //Schedules for execution on a worker thread.
        new WSTask().execute();
    }

    private class WSTask extends SwingWorker<Void, UpdateOrderBookCommand> {

        private String url = "wss://api.bitfinex.com/ws";
        private WebsocketClientEndpoint clientEndPoint = null;
        private MarketService marketService = new BitfinexMarketService();
        private OrderBook orderBook = new OrderBook();

        @Override
        protected Void doInBackground() {

            try {
                clientEndPoint = new WebsocketClientEndpoint(new URI(url));
            } catch (URISyntaxException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            // add listener
            clientEndPoint.addMessageHandler(message -> {
                List<UpdateOrderBookCommand> updates = marketService.handleOrderBookChanelMessage(message);
                updates.forEach(updCommand -> publish(updCommand));
            });

            SubscribeOrderBookRequest subscribeOrderBookRequest = new SubscribeOrderBookRequest();
            TradingPair tradingPair = (TradingPair) tradingPairComboBox.getSelectedItem();
            subscribeOrderBookRequest.setPair(tradingPair);
            clientEndPoint.sendAsJson(subscribeOrderBookRequest);

            return null;
        }


        @Override
        protected void process(List<UpdateOrderBookCommand> updates) {
            for (UpdateOrderBookCommand update : updates) {
                orderBook.processUpdate(update);
            }

            updateDataTable(bidsTableData, orderBook.getBids());
            updateDataTable(asksTableData, orderBook.getAsks());
            mainFrame.repaint();
        }

        private void updateDataTable(Object[][] tableData, Collection<Order> orders) {
            int amountColumnNr = 0;
            int totalColumnNr = 1;
            int pricetColumnNr = 2;

            if (tableData.equals(asksTableData)) {
                amountColumnNr = 2;
                totalColumnNr = 1;
                pricetColumnNr = 0;
            }

            int y = 1;
            Collection<OrderDTO> orderDTOs = ModelMapper.mapToDTOs(orders);
            for (OrderDTO dto : orderDTOs) {
                tableData[y][amountColumnNr] = dto.getAmount();
                tableData[y][totalColumnNr] = dto.getTotal();
                tableData[y][pricetColumnNr] = dto.getPrice();
                y++;
            }
        }

    }

}
