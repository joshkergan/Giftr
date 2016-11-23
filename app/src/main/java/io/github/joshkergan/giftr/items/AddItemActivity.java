package io.github.joshkergan.giftr.items;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.github.joshkergan.giftr.BuildConfig;
import io.github.joshkergan.giftr.GiftrActivity;
import io.github.joshkergan.giftr.R;
import io.github.joshkergan.giftr.db.GiftrDbHelper;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Patrick on 2016-11-20.
 */

/** TODO: It may be better (it probably is) to have the view for this be a ListView with n embedded
 * EditText boxes (where n is 3, say) and then have a ... button after the n EditText boxes for the
 * user to add more if more are required, in a dynamic way. For now there are 10 fields hardcoded
 * there, which is fine enough for a prototype.
  */


public class AddItemActivity extends GiftrActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final GiftrDbHelper dbHelper = GiftrDbHelper.getDbInstance(getApplicationContext());
        final View activityView = attachView(R.layout.add_item_view, this);
        Intent intent = getIntent();
        final int personId = intent.getIntExtra("PERSON_ID", -1);


        final ArrayList<AmazonItem> addedItems = new ArrayList<AmazonItem>();
        final AutoCompleteTextView autoComplete = (AutoCompleteTextView) findViewById(R.id.add_item_auto_complete);
        ListView addedItemsView = (ListView) activityView.findViewById(R.id.added_items_layout);
        final ArrayAdapter<AmazonItem> addedItemsAdapter =
                new ArrayAdapter<AmazonItem>(activityView.getContext(), android.R.layout.simple_list_item_1);
        addedItemsView.setAdapter(addedItemsAdapter);



        autoComplete.addTextChangedListener(new TextWatcher() {
            boolean performedAPICall = false;
            int prevLength = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                prevLength = s.length();
                if (s.length() >= 2 && !performedAPICall) {
                    performedAPICall = true;
                    AsyncGetItems getItems = new AsyncGetItems();
                    getItems.textView = autoComplete;
                    getItems.context = activityView.getContext();
                    getItems.execute(s.toString());
                }
                System.out.println(before);

                if (prevLength < 2) {
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
                addedItems.add(selectedItem);
                addedItemsAdapter.add(selectedItem);
                // we're auto-adding so we can empty the text bar
                autoComplete.setText("");
            }
        });

        Button saveItemsButton = (Button) activityView.findViewById(R.id.save_button);
        saveItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (AmazonItem item: addedItems) {
                    dbHelper.addInterestToPersonById(dbHelper.getWritableDatabase(), 1, item);
                }
                Intent itemsIntent = new Intent(activityView.getContext(), ItemActivity.class);
                startActivity(itemsIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
