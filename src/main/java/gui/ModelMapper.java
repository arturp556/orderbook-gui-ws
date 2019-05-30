package gui;

import gui.model.OrderDTO;
import service.model.Order;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by artur on 21.06.17.
 */
class ModelMapper {
    private ModelMapper() {}

    static String pricePattern = "###,###.##";
    static String amountPattern = "###,###.##";
    static String totalPattern = "###,###.##";

    static DecimalFormat priceFormatter = new DecimalFormat(pricePattern);
    static DecimalFormat amountFormatter = new DecimalFormat(amountPattern);
    static DecimalFormat totalFormatter = new DecimalFormat(totalPattern);

    public static List<OrderDTO> mapToDTOs(Collection<Order> orders) {
        List<OrderDTO> results = new ArrayList<>();
        BigDecimal total = new BigDecimal(0);
        for (Order order : orders) {
            OrderDTO dto = new OrderDTO();
            total = total.add(order.getAmount());

            String price = priceFormatter.format(order.getPrice());
            String amount = amountFormatter.format(order.getAmount());
            String totalStr = totalFormatter.format(total);

            dto.setAmount(amount);
            dto.setPrice(price);
            dto.setTotal(totalStr);
            results.add(dto);
        }
        return results;
    }
}
