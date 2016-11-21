package io.github.joshkergan.giftr.items;

/**
 * Created by balaji on 11/20/2016.
 */

public class AmazonItem {
    // left these public for easyness sake
    String name;
    String url;
    String imageUrl;
    Double price;

    AmazonItem(String Name, String Url, String ImageUrl, Double Price) {
        name = Name;
        url = Url;
        imageUrl = ImageUrl;
        price = Price;
    }
}
