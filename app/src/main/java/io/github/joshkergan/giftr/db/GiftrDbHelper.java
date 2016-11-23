package io.github.joshkergan.giftr.db;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.sql.Date;

import io.github.joshkergan.giftr.items.ItemContract;
import io.github.joshkergan.giftr.people.PeopleContract;

/**
 * Created by Josh on 16/10/2016.
 * Helper class for creating and using the Giftr DB
 */

public final class GiftrDbHelper extends SQLiteOpenHelper{
    // The single instance of the DB
    private static GiftrDbHelper sInstance;

    // Some helpers for Types
    public static final int DATABASE_VERSION = 1;
    // Constants to preform the normal actions on the DB.
    public static final String DATABASE_NAME = "Giftr.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String DATA_TYPE = " BLOB";
    private static final String DATE_TYPE = " DATE";
    private static final String INTEGER_TYPE = " INTEGER";

    private static final String SQL_CREATE_PEOPLE_TABLE =
            "CREATE TABLE " + PeopleContract.PeopleEntry.TABLE_NAME +
                    " (" + PeopleContract.PeopleEntry._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    PeopleContract.PeopleEntry.COLUMN_NAME_PERSON + TEXT_TYPE + " ," +
                    PeopleContract.PeopleEntry.COLUMN_NAME_PHOTO + DATA_TYPE + " );\n";

    // TODO: Remove image field
    // TODO: Change primary key to the name field (because two interests with the same name should
    // be considered the same interest, and ID numbers as the PK lose this property).
    private static final String SQL_CREATE_ITEM_TABLE =
            "CREATE TABLE " + ItemContract.ItemEntry.TABLE_NAME +
                    " (" + ItemContract.ItemEntry._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    ItemContract.ItemEntry.COLUMN_NAME_ITEM + TEXT_TYPE + " ," +
                    ItemContract.ItemEntry.COLUMN_NAME_PHOTO + DATA_TYPE + " );";

    // TODO: Add primary key constraint on (person, mapping) pair. Make this gel with the primary
    // key change on the items table.
    private static final String SQL_CREATE_MAPPING_TABLE =
            "CREATE TABLE  " + MappingContract.MappingEntry.TABLE_NAME +
                    " (" + MappingContract.MappingEntry.COLUMN_NAME_PERSON_ID + INTEGER_TYPE +
                    ", " + MappingContract.MappingEntry.COLUMN_NAME_ITEM_ID + INTEGER_TYPE +
                    ", " + MappingContract.MappingEntry.COLUMN_NAME_DATE + DATE_TYPE +
                    ", " + "FOREIGN KEY(" + MappingContract.MappingEntry.COLUMN_NAME_PERSON_ID +
                    " ) REFERENCES " + PeopleContract.PeopleEntry.TABLE_NAME + "(" + PeopleContract.PeopleEntry._ID +
                    ")" + "FOREIGN KEY(" + MappingContract.MappingEntry.COLUMN_NAME_ITEM_ID +
                    ") REFERENCES " + ItemContract.ItemEntry._ID + ");";
    // One ( closes the FOREIGN KEY statement, the other closes the CREATE TABLE statement

    private static final String SQL_CREATE_ENTRIES = SQL_CREATE_PEOPLE_TABLE +
            SQL_CREATE_ITEM_TABLE +
            SQL_CREATE_MAPPING_TABLE;

    // Use the getInstance method to get the DB instance
    private GiftrDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized GiftrDbHelper getDbInstance(Context context) {
        if(sInstance == null) {
            sInstance = new GiftrDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PEOPLE_TABLE);
        db.execSQL(SQL_CREATE_ITEM_TABLE);
        db.execSQL(SQL_CREATE_MAPPING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Logic for updating the table schema should go here. It will probably be ugly
        // spaghetti code.
    }

    public Cursor getPersonInfo(SQLiteDatabase db, int id) {
        final String PERSON_QUERY = "SELECT * FROM " + PeopleContract.PeopleEntry.TABLE_NAME +
                " LEFT JOIN " + MappingContract.MappingEntry.TABLE_NAME + " ON " +
                PeopleContract.PeopleEntry.TABLE_NAME + "." + PeopleContract.PeopleEntry._ID + " = "
                + MappingContract.MappingEntry.TABLE_NAME + "." +
                MappingContract.MappingEntry.COLUMN_NAME_PERSON_ID + " WHERE " +
                PeopleContract.PeopleEntry._ID + " = ?";

        return db.rawQuery(PERSON_QUERY, new String[]{String.valueOf(id)});
    }

    public void createPerson(SQLiteDatabase db, String name, Bitmap image) {
        ContentValues personValues = new ContentValues();
        personValues.put(PeopleContract.PeopleEntry.COLUMN_NAME_PERSON, name);
        if (image == null){
            personValues.putNull(PeopleContract.PeopleEntry.COLUMN_NAME_PHOTO);
        }else{
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            personValues.put(PeopleContract.PeopleEntry.COLUMN_NAME_PHOTO, byteStream.toByteArray());

        }
        db.insert(PeopleContract.PeopleEntry.TABLE_NAME, null, personValues);
    }

    /**
     * Adds an interest to a person in the database, using the person's ID number from the DB as the key.
     * @param db The database to update
     * @param personId The ID number (in the database db) of the person to update the interests of.
     *                 NOTE: This is assumed to be a valid ID, i.e. it exists in db somewhere.
     * @param interest The interest string to add.
     *
     * TODO: Either add image parameter to this or remove the image from the item table entirely.
     *                 Second is recommended.
     */
    public void addInterestToPersonById(SQLiteDatabase db, int personId, String interest) {
        ContentValues itemTableValues = new ContentValues();
        ContentValues mappingTableValues = new ContentValues();

        itemTableValues.put(ItemContract.ItemEntry.COLUMN_NAME_ITEM, interest);
        itemTableValues.putNull(ItemContract.ItemEntry.COLUMN_NAME_PHOTO); // TODO: Remove this later if images are removed

        //  insert into the items table first, to get the key o add to the mapping.
        long itemId = db.insertWithOnConflict (ItemContract.ItemEntry.TABLE_NAME,
                null,
                itemTableValues,
                SQLiteDatabase.CONFLICT_IGNORE); // Ignore conflicts, since that means this interest
                                                 // is already in the table.
        mappingTableValues.put(MappingContract.MappingEntry.COLUMN_NAME_PERSON_ID, personId);
        // TODO: Figure out how to put date in there. Probably new Date() and some DateFormatter wizardry.
        mappingTableValues.putNull(MappingContract.MappingEntry.COLUMN_NAME_DATE);
        mappingTableValues.put(MappingContract.MappingEntry.COLUMN_NAME_ITEM_ID, itemId);

        db.insertWithOnConflict(MappingContract.MappingEntry.TABLE_NAME,
                null,
                mappingTableValues,
                SQLiteDatabase.CONFLICT_IGNORE); // same logic as above.
    }
}
