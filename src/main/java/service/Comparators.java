package service;

import service.model.Order;

import java.util.Comparator;

/**
 * Created by artur on 23.06.17.
 */
public class Comparators {
    private Comparators() {}

    public static Comparator<Order> sortByOrderPrice = (o1, o2) -> o1.getPrice().compareTo(o2.getPrice());
}
