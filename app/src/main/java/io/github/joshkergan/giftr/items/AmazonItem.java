package io.github.joshkergan.giftr.items;

/**
 * Created by balaji on 11/20/2016.
 */

public class AmazonItem {
    // left these public for easyness sake
    public String name;
    public String url;
    public String imageUrl;
    public Double price;

    AmazonItem(String Name, String Url, String ImageUrl, Double Price) {
        name = Name;
        url = Url;
        imageUrl = ImageUrl;
        price = Price;
    }

    // we have this for the arrayadapter to work with!
    @Override
    public String toString() {
        return name;
    }
}
