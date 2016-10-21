package io.github.joshkergan.giftr.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

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
                    PeopleContract.PeopleEntry.COLUMN_NAME_PHOTO + DATA_TYPE + " );";

    private static final String SQL_CREATE_ITEM_TABLE =
            "CREATE TABLE " + ItemContract.ItemEntry.TABLE_NAME +
                    " (" + ItemContract.ItemEntry._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    ItemContract.ItemEntry.COLUMN_NAME_ITEM + TEXT_TYPE + " ," +
                    ItemContract.ItemEntry.COLUMN_NAME_PHOTO + DATA_TYPE + " );";

    private static final String SQL_CREATE_MAPPING_TABLE =
            "CREATE TABLE  " + MappingContract.MappingEntry.TABLE_NAME +
                    " (" + MappingContract.MappingEntry.COLUMN_NAME_PERSON_ID + INTEGER_TYPE +
                    ", " + MappingContract.MappingEntry.COLUMN_NAME_ITEM_ID + INTEGER_TYPE +
                    ", " + MappingContract.MappingEntry.COLUMN_NAME_DATE + DATE_TYPE +
                    ", " + "FOREIGN KEY(" + MappingContract.MappingEntry.COLUMN_NAME_PERSON_ID +
                    ") REFERENCES " + PeopleContract.PeopleEntry.TABLE_NAME + "(" + PeopleContract.PeopleEntry._ID +
                    ")" + "FOREIGN KEY(" + MappingContract.MappingEntry.COLUMN_NAME_ITEM_ID +
                    ") REFERENCES " + ItemContract.ItemEntry._ID + "));";
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
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Logic for updating the table schema should go here. It will probably be ugly
        // spaghetti code.
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
}
