package io.github.joshkergan.giftr.items;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.joshkergan.giftr.R;
import io.github.joshkergan.giftr.db.MappingContract;

/**
 * Created by Patrick on 2016-10-17.
 * Modified by Josh on 2016-11-23
 *
 * Creates adapter for the RecyclerView for an individual person
 */

// TODO: Investigate using a CursorLoader for nice async queries.

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private final static String rawSqlQuery =
            "SELECT * FROM " + MappingContract.MappingEntry.TABLE_NAME + " INNER JOIN " +
            ItemContract.ItemEntry.TABLE_NAME + " ON " +
            MappingContract.MappingEntry.TABLE_NAME + "." + MappingContract.MappingEntry.COLUMN_NAME_ITEM_ID +
            "=" + ItemContract.ItemEntry.TABLE_NAME + "." + ItemContract.ItemEntry._ID +
            " WHERE " + MappingContract.MappingEntry.COLUMN_NAME_PERSON_ID + "=? ORDER BY " +
            MappingContract.MappingEntry.TABLE_NAME + "." + MappingContract.MappingEntry.COLUMN_NAME_DATE +
            " ASC;";
    private final Cursor c;
    private final SQLiteDatabase dbConnection;
    // the database ID that identifies the person we're showing items for.
    // We expect this to be a valid integer value that corresponds to a person ID in the database.
    private final int personId;

    public ItemAdapter(SQLiteDatabase db, int personId) {
        this.personId = personId;
        this.dbConnection = db;
        c = db.rawQuery(rawSqlQuery, new String[]{String.valueOf(personId)});
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_info, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder vh, int position) {
        c.moveToPosition(position);
        String itemName = c.getString(c.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME_ITEM));
        vh.mItemName.setText(itemName);
        // Will also eventually need to set the photo for this
    }

    @Override
    public long getItemId(int position) {
        if(c.moveToPosition(position)) {
            return c.getLong(c.getColumnIndex(ItemContract.ItemEntry._ID));
        } else {
            return RecyclerView.NO_ID;
        }
    }

    @Override
    public int getItemCount() {
        return c.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mItemName;
        public ViewHolder(ViewGroup itemView) {
            super(itemView);
            mItemName = (TextView)itemView.findViewById(R.id.item_name);
        }
    }
}
