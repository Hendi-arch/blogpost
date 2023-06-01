package com.example.blogpost.blogpost;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class BlogPostHelper {

    private BlogPostHelper() {
    }

    public static List<Order> configureSortParameters(String[] sort) {
        List<Order> orders = new ArrayList<>();
        for (String sortOrder : sort) {
            String[] sortData = sortOrder.split(":");
            String property = sortData[0];
            Direction direction;
            if (sortData.length > 1) {
                direction = Direction.fromOptionalString(sortData[1]).orElse(Direction.ASC);
            } else {
                direction = Direction.ASC;
            }
            orders.add(new Order(direction, property));
        }

        return orders;
    }

}
