package io.github.joshkergan.giftr;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import io.github.joshkergan.giftr.db.GiftrDbHelper;

/**
 * Created by Josh on 20/11/2016.
 * Activity to present the interests of a single person
 */

public final class PersonActivity extends GiftrActivity implements NavigationView.OnNavigationItemSelectedListener{

    private int personId;
    private GiftrDbHelper pDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent infoIntent = getIntent();
        // -1 means an error occured
        personId = infoIntent.getIntExtra("PersonID", -1);
        Log.d("PersonActivity", "onCreate: PersonID:" + personId);
        attachView(R.layout.person_activity, this);

        // We deal with a person not being passed in by loading the last person that got saved
        if (personId == -1 && savedInstanceState != null){
            personId = savedInstanceState.getInt("PersonID");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("PersonID", personId);
        super.onSaveInstanceState(outState);
    }
}
