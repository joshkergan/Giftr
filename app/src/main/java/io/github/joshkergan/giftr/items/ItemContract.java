package io.github.joshkergan.giftr.items;

import android.provider.BaseColumns;

/**
 * Created by Patrick on 16/10/2016.
 * Contract for the Item Table
 */

public final class ItemContract{
    // No external instantiation
    private ItemContract() {
    }

    /* Inner class to define the table components */
    public static class ItemEntry implements BaseColumns{
        public static final String TABLE_NAME = "Item";
        // This contains the item's full name, TYPE: STRING
        public static final String COLUMN_NAME_ITEM = "itemName";
        // Contains a reference to the photo stored for this item, TYPE: STRING
        public static final String COLUMN_NAME_PHOTO = "photo";
        // Contains a reference to the amazon url for this item, TPYE:STRING
        public static final String COLUMN_NAME_AMAZON_URL = "amazonUrl";
    }
}