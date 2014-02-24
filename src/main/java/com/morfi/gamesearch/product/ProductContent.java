package com.morfi.gamesearch.product;

import com.morfi.gamesearch.MainActivity;
import com.morfi.gamesearch.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces
 */
public class ProductContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<ProductItem> ITEMS = new ArrayList<ProductItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, ProductItem> ITEM_MAP = new HashMap<String, ProductItem>();

    public static void addItem(ProductItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.pid + "_" + item.baseID, item);
    }

    public static void removeItem(ProductItem item) {
        ITEMS.remove(item);
        ITEM_MAP.remove(item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class ProductItem {
        public String pid;
        public String title;
        public String genre;
        public int price;
        public String producer;
        public String platform;
        public String baseID;

        public ProductItem(String bID, String id, String title, String genre, int price, String producer, String platform) {
            this.baseID = bID;
            this.pid = id;
            this.title = title;
            this.genre = genre;
            this.price = price;
            this.producer = producer;
            this.platform = platform;
        }

        /* method to stringify object, it allows to display objects in item list*/
        @Override
        public String toString() {
            return title + "\n" + MainActivity.appcontext.getString(R.string.price_label) + " " + price + MainActivity.appcontext.getString(R.string.price_currency);
        }
    }
}
