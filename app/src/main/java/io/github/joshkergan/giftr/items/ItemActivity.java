package io.github.joshkergan.giftr.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.github.joshkergan.giftr.GiftrActivity;
import io.github.joshkergan.giftr.R;
import io.github.joshkergan.giftr.db.GiftrDbHelper;

/**
 * Created by Patrick on 2016-10-17.
 */

public class ItemActivity extends GiftrActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final GiftrDbHelper dbHelper = GiftrDbHelper.getDbInstance(getApplicationContext());
        final View activityView = attachView(R.layout.content_item, this);

        final RecyclerView itemlist = (RecyclerView) activityView.findViewById(R.id.item_list);
        ItemAdapter adapt = new ItemAdapter(dbHelper.getReadableDatabase());
        itemlist.setHasFixedSize(false);
        itemlist.setLayoutManager(new GridLayoutManager(this, 1));
        itemlist.setAdapter(adapt);
    }

    @Override
    public void onBackPressed() {
        // Go back to the calling activity
        super.onBackPressed(); // TODO: fix this
    }
}
