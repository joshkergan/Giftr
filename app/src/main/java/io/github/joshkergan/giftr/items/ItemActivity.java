package io.github.joshkergan.giftr.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.github.joshkergan.giftr.GiftrActivity;
import io.github.joshkergan.giftr.R;
import io.github.joshkergan.giftr.db.GiftrDbHelper;

/**
 * Created by Patrick on 2016-10-17.
 */

public class ItemActivity extends GiftrActivity {
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
        // Set the onclick functionality for the FAB
        FloatingActionButton fab = (FloatingActionButton) activityView.findViewById(R.id.fab_add_item);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent itemsIntent = new Intent(activityView.getContext(), AddItemActivity.class);
                startActivity(itemsIntent);
                //final View addItem = attachView(R.layout.add_item_view);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Go back to the calling activity
        super.onBackPressed(); // TODO: fix this
    }
}
