package io.github.joshkergan.giftr;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import io.github.joshkergan.giftr.db.GiftrDbHelper;

/**
 * Created by Josh on 20/11/2016.
 * Activity to present the interests of a single person
 */

public final class PersonActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private int personId;
    private GiftrDbHelper pDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent infoIntent = getIntent();
        // -1 means an error occured
        personId = infoIntent.getIntExtra("PersonID", -1);
        setContentView(R.layout.person_activity);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        final RecyclerView personsItems = (RecyclerView) findViewById(R.id.person_interests);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setSupportActionBar(toolbar);


        pDbHelper = GiftrDbHelper.getDbInstance(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();
            }
        });

        if (true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.inflateHeaderView(R.layout.nav_header_people);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_people){
            Intent peopleIntent = new Intent(this, PeopleActivity.class);
            startActivity(peopleIntent);
        }else if (id == R.id.nav_items){
            // Handle navigating to items activity
            Intent itemsIntent = new Intent(this, ItemsActivityStub.class);
            startActivity(itemsIntent);
        }else if (id == R.id.nav_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }else if (id == R.id.nav_send){
            // WE CAN ADD SOMETHING HERE
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
