package io.github.joshkergan.giftr.people;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Josh on 16/10/2016.
 * Helper class for creating and using the people DB
 */

public final class PeopleDbHelper extends SQLiteOpenHelper{
	// Some helpers for Types
	public static final int DATABASE_VERSION = 1;
	// Constants to preform the normal actions on the DB.
	public static final String DATABASE_NAME = "People.db";
	private static final String TEXT_TYPE = " TEXT";
	private static final String DATA_TYPE = " BLOB";
	private static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + PeopleContract.PeopleEntry.TABLE_NAME +
					" (" + PeopleContract.PeopleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					PeopleContract.PeopleEntry.COLUMN_NAME_PERSON + TEXT_TYPE + ", " +
					PeopleContract.PeopleEntry.COLUMN_NAME_PHOTO + DATA_TYPE + " )";

	public PeopleDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
}
