package io.github.joshkergan.giftr.items;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.github.joshkergan.giftr.BuildConfig;
import io.github.joshkergan.giftr.R;
import io.github.joshkergan.giftr.db.GiftrDbHelper;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Patrick on 2016-11-20.
 */

/** TODO: It may be better (it probably is) to have the view for this be a ListView with n embedded
 * EditText boxes (where n is 3, say) and then have a ... button after the n EditText boxes for the
 * user to add more if more are required, in a dynamic way. For now there are 10 fields hardcoded
 * there, which is fine enough for a prototype.
  */

// TODO: Make DB write an AsyncTask

public class AddItemActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final GiftrDbHelper dbHelper = GiftrDbHelper.getDbInstance(getApplicationContext());
        setContentView(R.layout.add_item_view);
        Intent intent = getIntent();

        // personId should NEVER EVER be -1, this filtering was done in the calling activity.
        // If it is, something has gone very very catastrophically wrong.
        final int personId = intent.getIntExtra("PERSON_ID", -1);
        if (BuildConfig.DEBUG && personId == -1) {
            throw new AssertionError("PersonId was -1 in AddItemActivity! This should never happen!");
        }

        final String personName = intent.getStringExtra("PERSON_NAME");

        // Append the person's name to the message field
        TextView message = (TextView) findViewById(R.id.add_item_message);
        message.setText(message.getText() + personName + ":");

        // Set button callback, which will invoke the DB to save the interests
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read the fields and add to DB
                // TODO: implement the dynamic view from the top TODO so this
                // is made nicer (i.e. a loop). As it is this is crap code.
                String[] interests = new String[10];

                for(String s : interests) {
                    if(!s.isEmpty()) {
                        dbHelper.addInterestToPersonById(dbHelper.getWritableDatabase(), personId, s);
                    }
                }

                // Go back to previous activity
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
