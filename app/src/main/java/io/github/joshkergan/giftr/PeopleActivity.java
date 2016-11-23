package io.github.joshkergan.giftr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import io.github.joshkergan.giftr.db.GiftrDbHelper;
import io.github.joshkergan.giftr.items.ItemActivity;
import io.github.joshkergan.giftr.people.AddPersonActivity;
import io.github.joshkergan.giftr.people.PeopleAdapter;

final public class PeopleActivity extends GiftrActivity{

    private boolean addPersonActive = false;
    private View activityView;
    private RecyclerView peopleList;
    private PeopleAdapter.OnItemClickListener peopleAction;
    private PeopleAdapter listAdapter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityView = attachView(R.layout.people_activity, this);
        peopleList = (RecyclerView) findViewById(R.id.list_people);

        pDbHelper = GiftrDbHelper.getDbInstance(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent addPersonIntent = new Intent(getApplicationContext(), AddPersonActivity.class);
                startActivity(addPersonIntent);
            }
        });

        peopleAction = new PeopleAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(int position) {
                Intent personIntent = new Intent(getApplicationContext(), PersonActivity.class);
                personIntent.putExtra("PersonID", listAdapter.getItemId(position));
                startActivity(personIntent);
            }
        };

        listAdapter = new PeopleAdapter(pDbHelper.getReadableDatabase(), peopleAction);
        peopleList.setHasFixedSize(false);
        peopleList.setLayoutManager(new GridLayoutManager(this, 2));
        peopleList.setAdapter(listAdapter);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_people) {
            // This is the current item
        } else if (id == R.id.nav_items){
            // Handle navigating to items activity
            Intent itemsIntent = new Intent(this, ItemActivity.class);
            startActivity(itemsIntent);
        }else if (id == R.id.nav_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }else if (id == R.id.nav_send){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (addPersonActive){
            attachView(activityView);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listAdapter = new PeopleAdapter(pDbHelper.getReadableDatabase(), peopleAction);
        peopleList.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.people, menu);
        return true;
    }
}
