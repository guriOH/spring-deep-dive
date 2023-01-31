package com.example.springbatch.common.utils;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {

    public static List<String> getItems() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            items.add(i + " Hello");
        }

        return items;
    }
}
