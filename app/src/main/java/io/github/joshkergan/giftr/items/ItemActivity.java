package io.github.joshkergan.giftr.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.github.joshkergan.giftr.R;
import io.github.joshkergan.giftr.db.GiftrDbHelper;

/**
 * Created by Patrick on 2016-10-17.
 */

public class ItemActivity extends AppCompatActivity {
    private int personId;
    private View activityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the person ID from the launching intent
        Intent intent = getIntent();
        // Should NEVER use the -1 default value because if the passed-in personId is not valid,
        // something has gone extremely and terribly wrong.
        // TODO: Figure out what the official pass through name for this will be
        personId = intent.getIntExtra("PERSON_ID_NAME", -1);
        GiftrDbHelper dbHelper = GiftrDbHelper.getDbInstance(getApplicationContext());

        activityView = getLayoutInflater().inflate(R.layout.content_item, null);

        final RecyclerView itemlist = (RecyclerView)activityView.findViewById(R.id.item_list);
        ItemAdapter adapt = new ItemAdapter(dbHelper.getReadableDatabase(), personId);
        itemlist.setAdapter(adapt);
        itemlist.setHasFixedSize(false);

        // Set the onclick functionality for the FAB
        FloatingActionButton fab = (FloatingActionButton) activityView.findViewById(R.id.add_item_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ItemActivity.this, AddItemActivity.class);
                intent.putExtra("PERSON_ID", personId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Go back to the calling activity
        super.onBackPressed(); // TODO: fix this
    }
}
