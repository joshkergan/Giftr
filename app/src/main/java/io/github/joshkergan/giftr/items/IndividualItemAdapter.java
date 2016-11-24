package io.github.joshkergan.giftr.items;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.joshkergan.giftr.R;
import io.github.joshkergan.giftr.db.MappingContract;
import io.github.joshkergan.giftr.people.PeopleContract;

/**
 * Created by balaji on 11/23/2016.
 */

public class IndividualItemAdapter extends RecyclerView.Adapter<IndividualItemAdapter.ViewHolder> {

    private final static String rawSqlQuery =
            "SELECT * FROM " + MappingContract.MappingEntry.TABLE_NAME + " INNER JOIN " +
                    PeopleContract.PeopleEntry.TABLE_NAME + " ON " +
                    MappingContract.MappingEntry.TABLE_NAME + "." + MappingContract.MappingEntry.COLUMN_NAME_PERSON_ID +
                    "=" + PeopleContract.PeopleEntry.TABLE_NAME + "." + PeopleContract.PeopleEntry._ID +
                    " WHERE " + MappingContract.MappingEntry.COLUMN_NAME_ITEM_ID + "=? ORDER BY " +
                    MappingContract.MappingEntry.TABLE_NAME + "." + MappingContract.MappingEntry.COLUMN_NAME_DATE +
                    " ASC;";
    private final SQLiteDatabase dbConnection;
    private final Cursor c;
    private final long itemId;
    IndividualItemAdapter (SQLiteDatabase db, long itemId) {
        this.itemId = itemId;
        this.dbConnection = db;
        c = dbConnection.rawQuery(rawSqlQuery, new String[]{String.valueOf(this.itemId)});
    }
    @Override
    public IndividualItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_info, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(IndividualItemAdapter.ViewHolder vh, int position) {
        if (c == null){
            return;
        }
        c.moveToPosition(position);
        vh.mItemName.setText(
                c.getString(c.getColumnIndex(PeopleContract.PeopleEntry.COLUMN_NAME_PERSON))
        );
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
