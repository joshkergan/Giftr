package io.github.joshkergan.giftr.people;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.joshkergan.giftr.R;
import io.github.joshkergan.giftr.db.MappingContract;
import io.github.joshkergan.giftr.items.ItemContract;

/**
 * Created by Patrick on 2016-10-17.
 * Modified by Josh on 2016-11-23
 * <p>
 * Creates adapter for the RecyclerView for an individual person
 */

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder>{

    private final static String rawSqlQuery =
            "SELECT * FROM " + MappingContract.MappingEntry.TABLE_NAME + " INNER JOIN " +
                    ItemContract.ItemEntry.TABLE_NAME + " ON " +
                    MappingContract.MappingEntry.TABLE_NAME + "." + MappingContract.MappingEntry.COLUMN_NAME_ITEM_ID +
                    "=" + ItemContract.ItemEntry.TABLE_NAME + "." + ItemContract.ItemEntry._ID +
                    " WHERE " + MappingContract.MappingEntry.COLUMN_NAME_PERSON_ID + "=? ORDER BY " +
                    MappingContract.MappingEntry.TABLE_NAME + "." + MappingContract.MappingEntry.COLUMN_NAME_DATE +
                    " ASC;";
    private final SQLiteDatabase dbConnection;
    private final Cursor c;
    private final long personId;
    private final OnItemClickListener mlistener;

    public PersonAdapter(SQLiteDatabase db, long personId, @Nullable OnItemClickListener listener) {
        this.personId = personId;
        this.dbConnection = db;
        this.mlistener = listener;
        c = dbConnection.rawQuery(rawSqlQuery, new String[]{String.valueOf(this.personId)});
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_info, parent, false);
        return new ViewHolder(v, mlistener);
    }

    public void onBindViewHolder(ViewHolder vh, int position) {
        if (c == null){
            return;
        }
        c.moveToPosition(position);
        vh.mItemName.setText(
                c.getString(c.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME_ITEM))
        );
        vh.itemView.setOnClickListener(vh);

    }

    @Override
    public long getItemId(int position) {
        if (c.moveToPosition(position)){
            return c.getLong(c.getColumnIndex(ItemContract.ItemEntry._ID));
        }else{
            return RecyclerView.NO_ID;
        }
    }

    @Override
    public int getItemCount() {
        return c.getCount();
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mItemName;
        CircleImageView mImageView;
        @Nullable
        OnItemClickListener mListener;

        ViewHolder(ViewGroup itemView, @Nullable OnItemClickListener listener) {
            super(itemView);
            mListener = listener;
            mItemName = (TextView) itemView.findViewById(R.id.item_name);
            mImageView = (CircleImageView) itemView.findViewById(R.id.item_image);
        }

        @Override
        public void onClick(View v) {
            Log.d("RecyclerView.ViewHolder", "onClick: Item Element id: " + getAdapterPosition());
            if (mListener != null){
                mListener.OnItemClick(getAdapterPosition());
            }
        }
    }
}
