package io.github.joshkergan.giftr.db;

import android.provider.BaseColumns;
import android.provider.ContactsContract;

/**
 * Created by Patrick on 16/10/2016.
 * Contract for the Mapping Table, which maps the Item table to the People table
 */

public final class MappingTableContract{
    // No external instantiation
    private MappingTableContract() {
    }

    /* Inner class to define the table components */
    public static class MappingTableEntry implements BaseColumns{
        public static final String TABLE_NAME = "PersonToItemMap";
        // This contains the date that the item was added to a person's preferences, TYPE: DATE
        public static final String COLUMN_NAME_DATE = "AddDate";
        // This contains the (unique) ID of a person as a foreign INTEGER key.
        public static final String COLUMN_NAME_PERSON_ID = "PersonID";
        // This contains the (unique) ID of an item as a foreign INTEGER key.
        public static final String COLUMN_NAME_ITEM_ID = "ItemID";
    }
}
