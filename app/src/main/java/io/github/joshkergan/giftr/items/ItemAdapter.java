package io.github.joshkergan.giftr.items;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.joshkergan.giftr.R;

/**
 * Created by Patrick on 2016-10-17.
 */


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    // the database ID that identifies the person we're showing items for.
    // We expect this to be a valid integer value that corresponds to a person ID in the database.
    private final static String rawSqlQuery =
            "SELECT * FROM " +
                    ItemContract.ItemEntry.TABLE_NAME + "; ";
    private final Cursor c;
    private final SQLiteDatabase dbConnection;

    public ItemAdapter(SQLiteDatabase db) {
        this.dbConnection = db;
        c = db.rawQuery(rawSqlQuery, null);
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
