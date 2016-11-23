package io.github.joshkergan.giftr.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import io.github.joshkergan.giftr.GiftrActivity;
import io.github.joshkergan.giftr.R;
import io.github.joshkergan.giftr.db.GiftrDbHelper;

/**
 * Created by Patrick on 2016-10-17.
 */

public class ItemActivity extends GiftrActivity {
    private int personId;
    private View activityView;
    private boolean addItemActive = true;
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
        final View attachedView = attachView(R.layout.content_item, this);

        //activityView = getLayoutInflater().inflate(R.layout.content_item, null);

        final RecyclerView itemlist = (RecyclerView) attachedView.findViewById(R.id.item_list);
        ItemAdapter adapt = new ItemAdapter(dbHelper.getReadableDatabase(), personId);
        itemlist.setAdapter(adapt);
        itemlist.setHasFixedSize(false);

        // Set the onclick functionality for the FAB
        FloatingActionButton fab = (FloatingActionButton) attachedView.findViewById(R.id.fab_add_item);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final View addItem = attachView(R.layout.add_item_view);

                final AutoCompleteTextView autoComplete = (AutoCompleteTextView) addItem.findViewById(R.id.add_item_auto_complete);
                /*String[] autoFillItems = {"123123"};
                final ArrayAdapter<AmazonItem> adapter = new ArrayAdapter<AmazonItem>
                        (attachedView.getContext() ,android.R.layout.simple_list_item_1, autoFillItems);
                autoComplete.setAdapter(adapter);*/
                autoComplete.addTextChangedListener(new TextWatcher() {
                    boolean performedAPICall = false;
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() >= 2 && !performedAPICall) {
                            performedAPICall = true;
                            AsyncGetItems getItems = new AsyncGetItems();
                            getItems.textView = autoComplete;
                            getItems.context = attachedView.getContext();
                            getItems.execute(s.toString());
                        }
                        System.out.println(start);
                        System.out.println(before);

                        if (before < 2) {
                            performedAPICall = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AmazonItem selectedItem = (AmazonItem) parent.getItemAtPosition(position);
                        // add to list and do stuff
                    }
                });


                addItemActive = true;

            }
        });
    }

    @Override
    public void onBackPressed() {
        // Go back to the calling activity
        super.onBackPressed(); // TODO: fix this
    }
}
