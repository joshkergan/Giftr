package io.github.joshkergan.giftr.people;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.joshkergan.giftr.R;

/**
 * Created by Josh on 28/09/2016.
 * Adapter for the People List View
 */

public final class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder>{

    private final static String[] projection = {
            PeopleContract.PeopleEntry._ID,
            PeopleContract.PeopleEntry.COLUMN_NAME_PERSON,
            PeopleContract.PeopleEntry.COLUMN_NAME_PHOTO
    };
    private final static String order = PeopleContract.PeopleEntry.COLUMN_NAME_PERSON + " ASC";
    private SQLiteDatabase dbReadConnection;
    private boolean validData = true;
    private Cursor c;

    public PeopleAdapter(SQLiteDatabase giftrDb) {
        super();
        this.dbReadConnection = giftrDb;
        c = giftrDb.query(
                true,
                PeopleContract.PeopleEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,//order,
                null
        );
    }

    @Override
    public PeopleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.person_info, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        c.moveToPosition(position);
        byte[] blob = c.getBlob(c.getColumnIndex(PeopleContract.PeopleEntry.COLUMN_NAME_PHOTO));
        //holder.mFriendIcon.setImageBitmap(BitmapFactory.decodeByteArray(blob, 0, blob.length));
        holder.mFriendName.setText(
                c.getString(c.getColumnIndex(PeopleContract.PeopleEntry.COLUMN_NAME_PERSON))
        );
    }

    @Override
    public long getItemId(int position) {
        if (validData && c != null && c.moveToPosition(position)){
            return c.getLong(c.getColumnIndex(PeopleContract.PeopleEntry._ID));
        }
        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemCount() {
        if (validData && c != null){
            int count = c.getCount();
            return count;
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView mFriendIcon;
        public TextView mFriendName;

        public ViewHolder(ViewGroup itemView) {
            super(itemView);
            mFriendIcon = (CircleImageView) itemView.findViewById(R.id.friend_image);
            mFriendName = (TextView) itemView.findViewById(R.id.friend_name);
        }
    }
}
