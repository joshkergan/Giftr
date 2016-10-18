package io.github.joshkergan.giftr.people;

import android.provider.BaseColumns;

/**
 * Created by Josh on 16/10/2016.
 * Contract for the People Table
 */

public final class PeopleContract{
	// No external instantiation
	private PeopleContract() {}

	/* Inner class to define the table components */
	public static class PeopleEntry implements BaseColumns{
		public static final String TABLE_NAME = "People";
		// This contains the person's full name, TYPE: STRING
		public static final String COLUMN_NAME_PERSON = "personName";
		// Contains a reference to the photo stored for this person, TYPE: BLOB
		public static final String COLUMN_NAME_PHOTO = "photo";
	}
}
